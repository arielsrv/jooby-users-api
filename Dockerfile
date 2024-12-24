# syntax=docker/dockerfile:1
ARG GRADLE_VERSION=8.12
ARG JAVA_VERSION=17

FROM gradle:${GRADLE_VERSION}-jdk17 AS build
WORKDIR /users-api
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src
RUN gradle shadowJar

FROM eclipse-temurin:${JAVA_VERSION}-jdk
WORKDIR /users-api
COPY --from=build /users-api/build/libs/users-api-1.0.0-all.jar app.jar
CMD ["java", "-jar", "app.jar"]
