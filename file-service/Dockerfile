FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/file-service-0.0.1-SNAPSHOT.jar app.jar
COPY .env .env

EXPOSE 9096
ENTRYPOINT ["java", "-jar", "app.jar"]