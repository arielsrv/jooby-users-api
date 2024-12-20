# syntax=docker/dockerfile:1
FROM gradle:8.11.1 AS build
ADD . /app
WORKDIR /app
RUN gradle shadowJar

FROM gcr.io/distroless/java17-debian12:nonroot AS release
COPY --from=build /app /app
WORKDIR /app
CMD ["build/libs/app.jar"]
