package com.darshan.portfolio;

import java.util.*;
import java.util.regex.*;

/**
 * Very small, dependency-free intent matcher. Scores each KnowledgeBase entry
 * by counting keyword hits inside the user's message and returns the best
 * match. Falls back to a scoped "I only answer about Darshan" response.
 */
final class ChatEngine {

    private static final Pattern WORD = Pattern.compile("[a-zA-Z0-9+#\\-]+");

    private static final String FALLBACK =
        "I'm just Darshan's portfolio assistant, so I can only answer questions about him - " +
        "his skills, projects, education, experience, certificates, or achievements. Try asking " +
        "something like \"What projects has Darshan built?\" or \"What is he learning right now?\"";

    static String reply(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return FALLBACK;
        }
        String normalized = userMessage.toLowerCase(Locale.ROOT);

        KnowledgeBase.Entry best = null;
        int bestScore = 0;

        for (KnowledgeBase.Entry entry : KnowledgeBase.ENTRIES) {
            int score = 0;
            for (String kw : entry.keywords()) {
                if (kw.contains(" ")) {
                    // phrase match
                    if (normalized.contains(kw)) {
                        score += 3;
                    }
                } else if (containsWord(normalized, kw)) {
                    score += 2;
                }
            }
            if (score > bestScore) {
                bestScore = score;
                best = entry;
            }
        }

        if (best != null && bestScore > 0) {
            return best.answer();
        }
        return FALLBACK;
    }

    private static boolean containsWord(String text, String word) {
        Matcher m = WORD.matcher(text);
        while (m.find()) {
            if (m.group().equals(word)) return true;
        }
        return false;
    }

    private ChatEngine() {}
}
