# users-api

[![Java CI with Gradle](https://github.com/arielsrv/jooby-users-api/actions/workflows/gradle.yml/badge.svg)](https://github.com/arielsrv/jooby-users-api/actions/workflows/gradle.yml)
[![Coverage](.github/badges/jacoco.svg)](https://github.com/arielsrv/jooby-users-api/actions/workflows/gradle.yml)


Welcome to users-api!!

## running

    ./gradlew joobyRun

## building

    ./gradlew build

## docker

     docker build . -t users-api
     docker run -p 8080:8080 -it users-api
