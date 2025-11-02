FROM openjdk:11-jre-slim

WORKDIR /app

COPY ../target/grpc-client.jar grpc-client.jar

CMD ["java", "-jar", "grpc-client.jar"]
