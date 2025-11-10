FROM eclipse-temurin:21-jdk-jammy

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]