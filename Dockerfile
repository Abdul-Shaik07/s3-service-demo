FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/s3-service-demo-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 5555

ENTRYPOINT ["java", "-jar", "app.jar"]