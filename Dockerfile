# Use official OpenJDK 21 lightweight image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/E-COMMERCE-JUMIA-1.0-SNAPSHOT.jar app.jar

# Expose port 1111
EXPOSE 1111

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
