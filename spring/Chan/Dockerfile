# ============================================
#  [1단계] 빌드 스테이지
# ============================================
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# 의존성 먼저 복사 (캐시 활용)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# 소스코드 복사
COPY src src

# jar 빌드
RUN ./gradlew clean bootJar --no-daemon

# ============================================
#  [2단계] 실행 스테이지
# ============================================
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]