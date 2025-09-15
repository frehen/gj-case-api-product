FROM eclipse-temurin:21-jre-alpine

# Set working directory
WORKDIR /app

# Copy the jar file (replace with your actual jar name)
COPY target/product-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
