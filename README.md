# Darshan A M — Portfolio (Java 17 + Animated Frontend)

A complete rebuild of the portfolio:
- **Backend:** Pure Java 17 (no frameworks, no Maven/Gradle needed) using the built-in
  `com.sun.net.httpserver.HttpServer`. Serves the static frontend and a small REST API
  (`POST /api/chat`) that powers a chatbot which **only answers questions about Darshan**
  (skills, projects, education, experience, achievements) using a keyword-matched knowledge
  base sourced from his resume — no external AI API keys required, so it runs completely offline.
- **Frontend:** Vanilla HTML/CSS/JS, dark "lab notebook" theme (ink-navy + signal amber),
  with an animated SVG avatar (idle blink, "talks" while the chatbot is replying), scroll-reveal
  animations, animated skill bars & stat counters, a typing hero effect, and a floating chat widget.

## Folder structure
```
darshan-portfolio/
├── backend/
│   └── src/main/java/com/darshan/portfolio/
│       ├── PortfolioServer.java   (HTTP server + static file serving + /api/chat)
│       ├── ChatEngine.java        (keyword-matching chat logic)
│       └── KnowledgeBase.java     (facts about Darshan, from his resume)
└── frontend/
    ├── index.html
    ├── style.css
    ├── script.js
    └── assets/
        ├── profile.jpg
        ├── Resume.pdf
        └── certificates/
```

## Requirements
- **JDK 17+** installed (check with `java -version` / `javac -version`).
  No Maven, Gradle, or internet access needed to build or run — it's 100% standard library.

## Build & Run

```bash
cd darshan-portfolio/backend

# Compile (the -encoding UTF-8 flag is a safety net; JDK 17 doesn't default
# to UTF-8 source encoding the way JDK 18+ does, so this avoids any garbled
# characters if your system's default charset isn't UTF-8, e.g. on Windows)
javac -encoding UTF-8 -d out src/main/java/com/darshan/portfolio/*.java

# Run (serves frontend from ../frontend automatically)
java -cp out com.darshan.portfolio.PortfolioServer
```

> **Windows PowerShell users:** PowerShell doesn't wildcard-expand `*.java` for
> external programs like `javac.exe` the way Linux/Mac shells do, and pasting
> multiple commands at once can trigger PowerShell's line-continuation mode
> and mangle flags. Run each line separately, and list the files explicitly:
> ```powershell
> cd darshan-portfolio\backend
> javac -encoding UTF-8 -d out src\main\java\com\darshan\portfolio\ChatEngine.java src\main\java\com\darshan\portfolio\KnowledgeBase.java src\main\java\com\darshan\portfolio\PortfolioServer.java
> java -cp out com.darshan.portfolio.PortfolioServer
> ```

Then open **http://localhost:8080** in your browser.

### Custom port
```bash
PORT=9000 java -cp out com.darshan.portfolio.PortfolioServer
```

### Running from a different working directory
If you're not running the `java` command from inside `backend/`, point it at the frontend folder explicitly:
```bash
FRONTEND_DIR=/absolute/path/to/darshan-portfolio/frontend java -cp out com.darshan.portfolio.PortfolioServer
```

## Customizing the chatbot
All chatbot facts live in `KnowledgeBase.java` as a list of `(keywords, answer)` entries.
Add a new `Entry` to teach it a new topic, or edit an existing `answer` string to update facts —
no rebuild tooling needed beyond `javac`.

## Deploying
Because the backend has zero external dependencies, you can:
- Run it directly on any machine with a JDK (`java -cp out com.darshan.portfolio.PortfolioServer`)
- Containerize it with a minimal `eclipse-temurin:17-jre` Docker image
- Package it as a single runnable jar:
  ```bash
  cd backend/out
  jar --create --file ../portfolio.jar --main-class com.darshan.portfolio.PortfolioServer .
  cd ..
  java -jar portfolio.jar
  ```
  (Set `FRONTEND_DIR` env var when running the jar from a different location.)

## Notes
- The chatbot is intentionally scoped — it will politely decline and redirect for anything
  not related to Darshan, per the original request.
- All content (skills, projects, education, experience, achievements, certificates, links) is
  taken directly from the resume/screenshots you provided; GitHub links preserved from your
  previous portfolio where available.
- Education and Experience are now separate sections (Education = degrees/schooling, Experience
  = the SAP/Edunet ML traineeship).
- A "Currently Learning — Java Full-Stack & DevOps" block lists the roadmap from your course
  screenshot (Core Java through Terraform); it's flagged as in-progress, not yet on the resume.
- Java is now presented as the primary language; C++ is labeled as DSA-practice only.
- The hero avatar uses your actual photo (`assets/profile.jpg`) with an animated glow ring and
  orbiting tech chips (JAVA / SPRING / CNN) around it; it pulses while the chatbot is replying.
- Certificates are no longer a separate section — each relevant award card in "Competitions &
  Awards" now shows its certificate thumbnail directly and links to the full-size image when
  clicked (IDEATHON-2K24, BRIN-HACK 2025, and the 0-1 Hackathon each have one; LUMINUS doesn't
  have a certificate on file, so its card stays text-only).
- The "Currently Learning" chip section has been removed from the site.
- All Java source strings are plain ASCII (no em-dashes/smart punctuation) to avoid the
  mojibake/garbled-character bug that can happen on JDK 17 when the system's default charset
  isn't UTF-8. Combined with the `-encoding UTF-8` build flag above, this is now double-safe.
