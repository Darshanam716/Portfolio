package com.darshan.portfolio;

import java.util.*;

/**
 * Static knowledge base about Darshan A M, sourced from his resume and his
 * own updates. Each entry: a topic id, trigger keywords, and the answer text.
 * This keeps the chatbot's answers scoped strictly to facts about Darshan.
 *
 * NOTE: Only plain ASCII characters are used in the answer strings on purpose.
 * JDK 17's javac does not default to UTF-8 source encoding (that only became
 * the default in JDK 18+), so non-ASCII punctuation like em-dashes can get
 * garbled ("mojibake") when compiled on a machine whose platform default
 * charset isn't UTF-8. Sticking to ASCII avoids the problem entirely.
 */
final class KnowledgeBase {

    record Entry(String id, List<String> keywords, String answer) {}

    static final List<Entry> ENTRIES = List.of(

        new Entry("greeting",
            List.of("hi", "hello", "hey", "yo", "sup", "greetings"),
            "Hey! I'm Darshan's portfolio assistant. Ask me about his skills, projects, " +
            "education, experience, certifications, or achievements - I can only talk about " +
            "Darshan though, not general topics :)"),

        new Entry("who",
            List.of("who is darshan", "who are you", "about darshan", "introduce", "tell me about him", "tell me about darshan"),
            "Darshan A M is an aspiring engineering graduate from Brindavan College of Engineering " +
            "(VTU), Bengaluru, with a CGPA of 8.34. He's a Java Developer at heart - strong in Core " +
            "Java and the Java full-stack ecosystem - while also working across Python, machine " +
            "learning, and computer vision. He's looking for an entry-level Java Developer or AI/ML " +
            "role."),

        new Entry("education",
            List.of("education", "college", "degree", "cgpa", "university", "study", "school", "10th", "12th", "puc", "qualification"),
            "Darshan is pursuing a Bachelor of Technology in Computer Science at Brindavan College " +
            "of Engineering, Bengaluru (VTU) with a CGPA of 8.34. He completed his 11th & 12th grade " +
            "at Brilliant P U College, Karnataka with 86.4%, and his 10th grade at Chirantana High " +
            "School, Karnataka with 83.36%. (His work experience/training is separate - ask me " +
            "about his 'experience' if you want that!)"),

        new Entry("experience",
            List.of("experience", "apprenticeship", "trainee", "sap", "edunet", "internship", "work experience", "training"),
            "Darshan's professional training experience: Machine Learning Trainee under SAP, via " +
            "the Edunet Foundation, Karnataka (Sep 2025 - Feb 2026). He trained in algorithms like " +
            "Linear and Logistic Regression, SVM, Decision Trees, Random Forest, and KNN, plus deep " +
            "learning concepts (ANN, CNN, LSTM, VGG) and worked on NLP tasks and ML pipeline " +
            "development. This is listed separately from his education on the site."),

        new Entry("skills_lang",
            List.of("programming language", "languages", "coding language", "main language", "primary language"),
            "Darshan's primary language is Java - he's built strong Core Java foundations and is " +
            "now going deep into the Java full-stack ecosystem (Spring Boot, Hibernate, REST APIs, " +
            "and more). He also uses Python for ML/AI work, SQL for databases, and JavaScript. He " +
            "uses C++ specifically for practicing Data Structures & Algorithms, not as a main " +
            "development language."),

        new Entry("skills_java",
            List.of("java", "core java", "spring", "spring boot", "hibernate", "jdbc", "servlet", "jsp", "maven", "gradle", "junit"),
            "Java is Darshan's main language. He has strong fundamentals in Core Java and is " +
            "currently deep in the Java full-stack track - Maven, Gradle, JUnit, JDBC, Servlets & " +
            "JSP, REST APIs, ORM concepts, Hibernate, the Spring Framework, Spring Boot (REST APIs, " +
            "JDBC, Data JPA, MVC), Spring Security with JWT and OAuth2, and Log4j for logging. Ask " +
            "me what he's currently learning for the full roadmap!"),

        new Entry("currently_learning",
            List.of("currently learning", "learning now", "in progress", "roadmap", "devops", "kafka", "docker", "microservices", "cloud", "terraform", "ansible", "jenkins", "spring ai", "spring boot mongodb"),
            "Right now Darshan is deep in a Java full-stack + DevOps roadmap: Core Java, Maven, " +
            "Gradle, JUnit, Git, DSA, JDBC, Servlets & JSP, REST APIs, ORM/Hibernate, Spring " +
            "Framework, Spring Boot, Spring JDBC, Spring Data JPA, Spring Security, JWT, OAuth2, " +
            "Log4j, Spring Boot with MongoDB, Docker, Cloud Deployment, Spring AI, Microservices, " +
            "Spring Boot with Kafka, Linux, Ansible, Jenkins, and Terraform. It's an active, " +
            "ongoing track - not yet on his resume since he's still completing it."),

        new Entry("skills_ml",
            List.of("machine learning", "ml skill", "ai skill", "computer vision", "nlp", "xai", "explainable"),
            "In Machine Learning & AI, Darshan works with model training, classification, " +
            "regression, computer vision, NLP, Explainable AI (XAI), and multimodal learning."),

        new Entry("skills_dl",
            List.of("deep learning", "cnn", "ann", "lstm", "vgg", "resnet", "vision transformer", "vit", "yolo", "grad-cam", "gradcam", "neural network"),
            "Darshan has hands-on experience with deep learning architectures including ANN, CNN, " +
            "LSTM, VGG, ResNet, Vision Transformer (ViT), YOLOv8, and Grad-CAM for explainability."),

        new Entry("skills_lib",
            List.of("library", "framework python", "numpy", "pandas", "matplotlib", "seaborn", "opencv", "pytorch", "tools he uses ml"),
            "His go-to Python libraries and frameworks are NumPy, Pandas, Matplotlib, Seaborn, " +
            "OpenCV, and PyTorch."),

        new Entry("skills_db",
            List.of("database", "mongodb", "mysql", "postgresql", "postgres", "sql database"),
            "For databases, Darshan works with MongoDB, MySQL, and PostgreSQL, and is now also " +
            "learning Spring Data JPA and Spring JDBC for the Java side."),

        new Entry("skills_tools",
            List.of("tools", "git", "github", "jupyter", "colab", "os", "operating system", "linux", "windows"),
            "His toolkit includes Git, GitHub, Jupyter Notebook, and Google Colab, and he's " +
            "comfortable on both Windows and Linux."),

        new Entry("skills_all",
            List.of("skills", "what can he do", "tech stack", "technologies"),
            "Darshan's core stack: Java as his main language (Core Java plus the Spring / full-stack " +
            "ecosystem he's currently building on), Python for ML/AI, SQL and JavaScript. On the AI " +
            "side: model training, computer vision, NLP, and Explainable AI, with deep learning " +
            "architectures like CNN, LSTM, ResNet, ViT and YOLOv8. Databases: MongoDB, MySQL, " +
            "PostgreSQL. Tools: Git, GitHub, Docker, and more. Ask me about any one area in more " +
            "depth!"),

        new Entry("project_cheatshield",
            List.of("cheatshield", "exam surveillance", "exam monitoring", "proctoring", "malpractice"),
            "CheatShield AI (Jan-Mar 2026) is a smart exam surveillance system Darshan developed " +
            "using computer vision and deep learning. It uses face recognition, YOLOv8-based phone " +
            "detection, and behavior analysis to catch malpractice, and generates automated alerts, " +
            "attendance records, and monitoring reports."),

        new Entry("project_scholar",
            List.of("scholar", "alzheimer", "mri", "medical ai", "u-net", "unet", "pubmed"),
            "ScholAR (Mar-Apr 2026) is an Explainable AI platform for Alzheimer's detection that " +
            "Darshan built. It's a multimodal system analyzing brain MRI scans and clinical data, " +
            "using 3D U-Net segmentation, ResNet feature extraction, Vision Transformer models, and " +
            "Grad-CAM for explainability. It even includes an autonomous research agent that " +
            "validates predictions against literature from PubMed, Semantic Scholar, and arXiv."),

        new Entry("project_crisis",
            List.of("crisis connector", "emergency", "mern", "geospatial", "hospital app", "police", "fire brigade"),
            "Crisis Connector (Jul-Nov 2025) is a MERN-stack emergency response web app Darshan " +
            "built, with real-time mapping and geospatial services connecting hospitals, police " +
            "stations, fire brigades, and other emergency services, backed by secure authentication."),

        new Entry("projects_all",
            List.of("projects", "project he built", "what has he built", "portfolio projects"),
            "Darshan's three flagship projects are: 1) CheatShield AI - an AI exam surveillance " +
            "system using YOLOv8 and face recognition; 2) ScholAR - an explainable multimodal AI " +
            "platform for Alzheimer's detection from MRI scans; and 3) Crisis Connector - a " +
            "MERN-stack emergency response platform with real-time geospatial mapping. Ask me about " +
            "any one by name for details!"),

        new Entry("achievements",
            List.of("hackathon", "award", "competition", "achievement", "ideathon", "brin-hack", "luminus", "iiit", "0-1"),
            "Darshan has a strong hackathon record: Runner-up and Team Lead at IDEATHON-2K24 " +
            "(Dec 2024), participant at BRIN-HACK 2025, a National Level Hackathon (May 2025), the " +
            "0-1 Hackathon at E-summit'25, IIIT Delhi (Aug 2025), and LUMINUS, another National " +
            "Level Hackathon (Apr 2026)."),

        new Entry("certificates",
            List.of("certificate", "certification", "certifications", "certs"),
            "Darshan's certificates are shown right inside the Competitions & Awards section of " +
            "this site - each award card with a certificate thumbnail can be clicked to view it " +
            "full size."),

        new Entry("contact",
            List.of("contact", "email", "phone", "reach him", "linkedin", "github link", "hire him", "hire"),
            "You can reach Darshan at amdarshan557@gmail.com or 9019837394. Links to his LinkedIn, " +
            "GitHub, LeetCode, and full resume are right on this page - check the top of the site " +
            "or the Contact section!"),

        new Entry("objective",
            List.of("goal", "objective", "career goal", "looking for", "what role", "job role"),
            "Darshan is seeking an entry-level Java Developer role, and is also open to AI/ML " +
            "opportunities, where he can build intelligent, scalable, real-world solutions using " +
            "his background in Java, programming, machine learning, and computer vision."),

        new Entry("strongest",
            List.of("strongest skill", "best at", "expert", "specialty", "specialization", "strength"),
            "Darshan's core strength is Java - Core Java plus the full-stack ecosystem he's " +
            "building on right now. On the AI side, his projects (CheatShield AI and ScholAR) " +
            "center on deep learning models like YOLOv8, ResNet and Vision Transformers, paired " +
            "with explainability techniques like Grad-CAM."),

        new Entry("thanks",
            List.of("thank you", "thanks", "thx", "appreciate it"),
            "You're welcome! Let me know if you want to know anything else about Darshan's skills, " +
            "projects, or experience."),

        new Entry("resume_download",
            List.of("resume", "cv", "download resume"),
            "You can download Darshan's full resume using the 'Download Resume' button at the top " +
            "of the site, or find it linked in the Contact section."),

        new Entry("age_location",
            List.of("age", "how old", "where does he live", "where is he from", "location", "based in"),
            "I can't share personal details like age, but I can tell you Darshan is based in " +
            "Bengaluru, Karnataka, India. Ask me about his skills, projects, education, or " +
            "experience instead!")
    );

    private KnowledgeBase() {}
}
