FROM gradle:8.3.0-jdk17 as build
WORKDIR /users-api
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src
COPY conf conf
RUN gradle shadowJar

FROM eclipse-temurin:17-jdk
WORKDIR /users-api
COPY --from=build /users-api/build/libs/users-api-1.0.0-all.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
