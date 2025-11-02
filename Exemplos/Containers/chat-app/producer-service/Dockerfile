FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/producer-service-jar-with-dependencies.jar producer-service.jar

#EXPOSE 8080

CMD ["java", "-jar", "producer-service.jar"]
