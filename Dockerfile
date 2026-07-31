# Multi-stage build: compile with the JDK, run with a slim JRE.
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY backend/src ./src
RUN javac -encoding UTF-8 -d out src/main/java/com/darshan/portfolio/*.java

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/out ./out
COPY frontend ./frontend

ENV FRONTEND_DIR=/app/frontend
# Render/most PaaS providers inject $PORT; PortfolioServer already reads it.
EXPOSE 8080
CMD ["java", "-cp", "out", "com.darshan.portfolio.PortfolioServer"]
