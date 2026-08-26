#FROM openjdk:11-jre-slim
FROM eclipse-temurin:11-jre

WORKDIR /app

COPY target/consumer-service-jar-with-dependencies.jar consumer-service.jar

#EXPOSE 8081

CMD ["java", "-jar", "consumer-service.jar"]
