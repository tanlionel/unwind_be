FROM openjdk:17-jdk-slim
RUN mkdir /app
COPY target/unwind-0.0.1-SNAPSHOT.jar /app/app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]