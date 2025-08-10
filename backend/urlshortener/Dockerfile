# multi-stage build for smaller final image
FROM maven:3.9.4-eclipse-temurin-21 AS build

# copy source code and build
WORKDIR /app
COPY pom.xml .
COPY src ./src

# build jar file
RUN mvn clean package -DskipTests

# Runtime stage - smaller base image
FROM eclipse-temurin:21-jre-alpine

# Create non-root user for security
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# copy the built jar from build stage
COPY --from=build /app/target/urlshortener-0.0.1-SNAPSHOT.jar app.jar

# change ownership to non-root user
RUN chown -R appuser:appgroup /app
USER appuser

EXPOSE 8080

# Use exec form for better signal handling
ENTRYPOINT ["java", "-jar", "app.jar"]