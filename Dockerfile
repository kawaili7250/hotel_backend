# Use Java 17 (Render supports it and Aiven requires SSL-enabled MySQL)
FROM eclipse-temurin:17-jdk AS build

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw pom.xml ./

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (this caches Maven deps)
RUN ./mvnw dependency:go-offline

# Copy project source
COPY src src

# Build application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Run Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
