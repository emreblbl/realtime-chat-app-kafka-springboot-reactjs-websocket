# Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

# The application's .jar file
ARG JAR_FILE=target/kafka-chat-server-0.0.1-SNAPSHOT.jar

# Add the application's .jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-jar","/app.jar"]