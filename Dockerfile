FROM gradle:8-jdk17 as build
WORKDIR /items-api
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle
COPY src src
COPY conf conf
RUN gradle shadowJar

FROM eclipse-temurin:17-jdk
WORKDIR /items-api
COPY --from=build /items-api/build/libs/users-api-1.0.0-all.jar app.jar
COPY conf conf
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
