# syntax=docker/dockerfile:1
FROM gradle:8.11.1 AS build
ADD . /app
WORKDIR /app
RUN gradle shadowJar

FROM gcr.io/distroless/java17-debian12:latest AS release
COPY --from=build /app/build /app/build
WORKDIR /app/build/libs
CMD ["app.jar"]
