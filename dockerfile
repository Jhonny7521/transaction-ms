
FROM openjdk:17-alpine
WORKDIR /app
COPY target/transaction-ms-server-0.0.1-SNAPSHOT.jar transaction-ms-0.0.1-SNAPSHOT.jar
EXPOSE 8585
ENTRYPOINT ["java", "-jar", "transaction-ms-0.0.1-SNAPSHOT.jar"]
