# Multi-stage Docker build for Spring Boot application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

COPY pom.xml .
COPY common/pom.xml ./common/pom.xml
COPY api/pom.xml ./api/pom.xml
COPY hello-api-1/pom.xml ./hello-api-1/pom.xml
COPY hello-api-2/pom.xml ./hello-api-2/pom.xml

RUN mvn dependency:go-offline -B || true

COPY common ./common
COPY api ./api
COPY hello-api-1 ./hello-api-1
COPY hello-api-2 ./hello-api-2

RUN mvn clean package -DskipTests -pl api -am

# Stage 2: Create runtime image
FROM eclipse-temurin:21-jre

# Add metadata
LABEL maintainer="your-email@example.com"
LABEL description="My First Spring Boot Application"

# Create non-root user for security
RUN addgroup --system spring && adduser --system spring --ingroup spring

# Set working directory
WORKDIR /app

# Copy the built JAR from build stage
COPY --from=build /app/api/target/api-1.0-SNAPSHOT.jar app.jar

# Change ownership to spring user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-Xmx512m -Xms256m -Djava.security.egd=file:/dev/./urandom"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
