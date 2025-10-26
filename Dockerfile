# Use official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Create app directory inside container
WORKDIR /app
#RUN mvn clean install
# Copy files from your local system to container
COPY ./target/User-Service-0.0.1-SNAPSHOT.jar /app

# Compile and run the app
#RUN mvn clean install

# Command to run your app
CMD ["java", "-jar", "User-Service-0.0.1-SNAPSHOT.jar"]
