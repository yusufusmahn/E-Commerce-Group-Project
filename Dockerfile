# ---------- STAGE 1: Build JAR ----------
FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR /app

# Copy everything (including .mvn, pom.xml, src, etc.)
COPY . .

# Build the project and skip tests to make it faster
RUN ./mvnw clean package -DskipTests

# ---------- STAGE 2: Run app ----------
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the same port your app uses
EXPOSE 1111

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
