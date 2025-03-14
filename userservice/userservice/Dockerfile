FROM --platform=linux/amd64 openjdk:23

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host to the container
COPY target/userservice-0.0.1-SNAPSHOT.jar /app/userservice-0.0.1-SNAPSHOT.jar

# Expose the port that your Java service listens on
EXPOSE 9810

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "/app/userservice-0.0.1-SNAPSHOT.jar"]