# Use a base image with OpenJDK
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/Streaming-1.0.jar /app/Streaming.jar

# Install FFmpeg
RUN apt-get update && \
    apt-get install -y ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# Set the environment variable for file upload directory
ENV FILE_UPLOAD_DIR=/app/videoStorage

# Make sure the directory exists and has the correct permissions
RUN mkdir -p $FILE_UPLOAD_DIR && \
    chmod -R 777 $FILE_UPLOAD_DIR

# Run the application
CMD ["java", "-jar", "/app/Streaming.jar"]
