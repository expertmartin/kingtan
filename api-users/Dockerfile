# Stage 1: Build the application
# FROM maven:latest AS builder
FROM maven:latest AS builder
WORKDIR /build
# Copy only dependency files first to leverage caching
COPY pom.xml .
RUN mvn dependency:go-offline
# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre
#FROM openjdk:21-jdk-slim
WORKDIR /app
# Copy the built JAR from the builder stage
COPY --from=builder /build/target/*.jar product.jar
# Expose the default Spring Boot port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "product.jar"]