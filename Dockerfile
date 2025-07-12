# ---------- STAGE 1: Build JAR ----------
FROM eclipse-temurin:21-jdk-alpine as builder

# Install Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copy everything
COPY . .

# Build the project
RUN mvn clean package -DskipTests

# ---------- STAGE 2: Run app ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the app port
EXPOSE 1111

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
