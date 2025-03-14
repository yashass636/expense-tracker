# Use Amazon Corretto 21 with platform specification
FROM --platform=linux/amd64 amazoncorretto:23

WORKDIR /app

# Copy the JAR file from the host to the container
COPY target/SpringSec-0.0.1-SNAPSHOT.jar /app/SpringSec-0.0.1-SNAPSHOT.jar

# Expose the port that your Java service listens on
EXPOSE 8089

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/SpringSec-0.0.1-SNAPSHOT.jar"]