# Stage 1: Build the jar using Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the jar on a lightweight JRE
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Use prod settings (database details come from environment variables)
ENV SPRING_PROFILES_ACTIVE=prod

# Don't run as root
RUN adduser -D kickabout
USER kickabout

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]