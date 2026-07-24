package com.darshan.portfolio;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Zero-dependency Java 17 backend for Darshan's portfolio.
 * - Serves the static frontend (HTML/CSS/JS/assets) from ../frontend
 * - Exposes POST /api/chat  { "message": "..." } -> { "reply": "..." }
 *
 * Build:  javac -d out src/main/java/com/darshan/portfolio/*.java
 * Run:    java -cp out com.darshan.portfolio.PortfolioServer
 */
public final class PortfolioServer {

    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
    private static final Path FRONTEND_ROOT = resolveFrontendRoot();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.setExecutor(Executors.newFixedThreadPool(8));

        server.createContext("/api/chat", PortfolioServer::handleChat);
        server.createContext("/", PortfolioServer::handleStatic);

        server.start();
        System.out.println("========================================");
        System.out.println(" Darshan's Portfolio Server (Java 17)");
        System.out.println(" Running at: http://localhost:" + PORT);
        System.out.println(" Serving frontend from: " + FRONTEND_ROOT.toAbsolutePath());
        System.out.println("========================================");
    }

    // ---------- /api/chat ----------

    private static void handleChat(HttpExchange exchange) throws IOException {
        addCors(exchange);
        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }
        if (!"POST".equalsIgnoreCase(method)) {
            sendJson(exchange, 405, "{\"error\":\"Use POST\"}");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        String message = extractJsonStringField(body, "message");
        String reply = ChatEngine.reply(message);

        String json = "{\"reply\":" + jsonEscape(reply) + "}";
        sendJson(exchange, 200, json);
    }

    /** Minimal JSON string field extractor to avoid any external JSON dependency. */
    private static String extractJsonStringField(String json, String field) {
        if (json == null) return "";
        String key = "\"" + field + "\"";
        int idx = json.indexOf(key);
        if (idx < 0) return "";
        int colon = json.indexOf(':', idx + key.length());
        if (colon < 0) return "";
        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;
        if (i >= json.length() || json.charAt(i) != '"') return "";
        i++;
        StringBuilder sb = new StringBuilder();
        while (i < json.length()) {
            char c = json.charAt(i);
            if (c == '\\' && i + 1 < json.length()) {
                char next = json.charAt(i + 1);
                switch (next) {
                    case 'n' -> sb.append('\n');
                    case 't' -> sb.append('\t');
                    case 'r' -> sb.append('\r');
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    default -> sb.append(next);
                }
                i += 2;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
                i++;
            }
        }
        return sb.toString();
    }

    private static String jsonEscape(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
                }
            }
        }
        sb.append("\"");
        return sb.toString();
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void addCors(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    // ---------- static file serving ----------

    private static void handleStatic(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        if (requestPath.equals("/")) requestPath = "/index.html";

        Path filePath = FRONTEND_ROOT.resolve("." + requestPath).normalize();

        if (!filePath.startsWith(FRONTEND_ROOT) || !Files.exists(filePath) || Files.isDirectory(filePath)) {
            byte[] notFound = "404 Not Found".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, notFound.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(notFound); }
            return;
        }

        String contentType = guessContentType(filePath.toString());
        byte[] bytes = Files.readAllBytes(filePath);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String guessContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".css")) return "text/css; charset=utf-8";
        if (path.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (path.endsWith(".json")) return "application/json; charset=utf-8";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".svg")) return "image/svg+xml";
        if (path.endsWith(".pdf")) return "application/pdf";
        if (path.endsWith(".ico")) return "image/x-icon";
        if (path.endsWith(".webmanifest") || path.endsWith(".json")) return "application/manifest+json";
        return "application/octet-stream";
    }

    private static Path resolveFrontendRoot() {
        String override = System.getenv("FRONTEND_DIR");
        Path candidate = override != null
            ? Paths.get(override)
            : Paths.get("").toAbsolutePath().resolve("../frontend");
        if (Files.exists(candidate)) return candidate.normalize();
        // fallback: try ./frontend (in case run from project root)
        Path alt = Paths.get("").toAbsolutePath().resolve("frontend");
        return Files.exists(alt) ? alt.normalize() : candidate.normalize();
    }

    private PortfolioServer() {}
}
