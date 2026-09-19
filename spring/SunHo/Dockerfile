# ============================================
#  [1단계] 빌드 스테이지 — 여기서 jar 파일을 만든다
# ============================================

# eclipse-temurin:21-jdk 이미지를 기반으로 시작.
#   - temurin: 무료 오픈소스 Java 배포판
#   - 17: Java 버전 (우리 프로젝트가 Java 17이니까 맞춰줌)
#   - jdk: 빌드하려면 컴파일러가 포함된 JDK가 필요
#   - AS builder: 이 스테이지에 'builder'라는 별명을 붙임 (2단계에서 씀)
FROM eclipse-temurin:17-jdk AS builder

# 컨테이너 안에서 작업할 폴더를 /app 으로 지정.
# 이후 명령어들은 전부 이 폴더 기준으로 실행됨.
WORKDIR /app

# 내 프로젝트 파일들을 컨테이너 안으로 복사한다.
# (아래 순서대로 복사하는 이유: Docker는 '변하지 않는 것 먼저' 복사하면
#  캐시를 재활용해서 빌드가 빨라짐. 소스코드는 자주 바뀌니까 맨 나중에.)
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# gradlew(그래들 실행 파일)에 실행 권한을 줌.
# 이거 안 하면 'Permission denied' 에러가 남.
RUN chmod +x gradlew

# 이제 소스코드를 복사 (제일 자주 바뀌는 부분이라 마지막에)
COPY src src

# 그래들로 실제 빌드! bootJar = 실행 가능한 jar 파일을 만든다.
#   - clean: 이전 빌드 결과물 삭제하고 새로
#   - --no-daemon: 컨테이너 빌드에선 데몬(백그라운드 프로세스) 안 씀
# 결과물: /app/build/libs/ 안에 xxx.jar 생성됨
RUN ./gradlew clean bootJar --no-daemon


# ============================================
#  [2단계] 실행 스테이지 — 만든 jar만 가져와서 실행
# ============================================

# 이번엔 jre(실행 전용, JDK보다 훨씬 가벼움) 이미지로 새로 시작.
# 빌드 도구들은 다 버리고 실행에 필요한 것만 남겨서 이미지를 슬림하게!
FROM eclipse-temurin:17-jre
WORKDIR /app

# 1단계(builder)에서 만든 jar 파일만 쏙 빼서 이 스테이지로 복사.
# --from=builder : "builder라는 별명의 스테이지에서 가져와라"
# *.jar : 이름이 뭐든 jar 파일을 app.jar 라는 이름으로 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 이 컨테이너가 8080 포트를 쓴다고 '문서상' 표시.
# (실제 외부 연결은 compose의 ports에서 함. 여긴 일종의 안내판)
EXPOSE 8080

# 컨테이너가 시작될 때 실행할 명령어.
# = 터미널에서 java -jar app.jar 를 친 것과 같음
ENTRYPOINT ["java", "-jar", "app.jar"]