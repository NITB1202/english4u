FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9097
ENTRYPOINT ["java", "-jar", "app.jar"]