# 1단계: Spring Boot 애플리케이션 빌드
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Gradle 관련 파일을 먼저 복사해서 Docker 캐시 활용
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Linux 컨테이너에서 gradlew 실행 권한 부여
RUN chmod +x gradlew

# 소스코드 복사
COPY src src

# 실행 가능한 Spring Boot jar 생성
RUN ./gradlew clean bootJar --no-daemon

# 2단계: 만들어진 jar만 실행
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]