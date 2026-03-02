FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the jar file to the container
COPY target/dubbo-triple-nacos-service-1.0.0.jar app.jar

# Expose ports
EXPOSE 9090 50051

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
