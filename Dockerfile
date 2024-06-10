FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/transaction-sender-service-0.0.1-SNAPSHOT.jar transaction-sender-service-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "transaction-sender-service-0.0.1-SNAPSHOT.jar"]
