FROM openjdk:11-jre-slim

WORKDIR /app

COPY ../target/grpc-server.jar grpc-server.jar

CMD ["java", "-jar", "grpc-server.jar"]
