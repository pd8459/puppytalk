# 1단계: 프로젝트 빌드 시 Java 21 사용
FROM gradle:8.5-jdk21 AS builder
WORKDIR /build
COPY . .
RUN gradle bootJar

# 2단계: 최종 이미지 생성 시 Java 21 사용
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]