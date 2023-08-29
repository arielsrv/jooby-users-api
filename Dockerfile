FROM eclipse-temurin:17-jdk as build

ADD . /app
WORKDIR /app

RUN ./gradlew shadowJar

ENTRYPOINT ["java", "-jar", "build/libs/app.jar"]
