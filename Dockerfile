# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies for offline behavior
RUN mvn dependency:go-offline -B
COPY src ./src
# Build the application skipping tests for speed
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copy the built jar file from the build stage
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Environment variables that should be overridden in deployment
ENV SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/bikeshare
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=root
ENV JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# Ensure wait-for-it or similar script is used in real cloud environments
# For MVP, just run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
