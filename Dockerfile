FROM gradle:8.5-jdk21 AS builder
WORKDIR /build
COPY . .
RUN gradle bootJar

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]