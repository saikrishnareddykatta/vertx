FROM eclipse-temurin:17-jre

WORKDIR /app
COPY build/libs/*-fat.jar app.jar
EXPOSE 8888
CMD ["java", "-jar", "app.jar"]
