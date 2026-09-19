# 🦁 멋사대학 과제 (1~10주차)
#### 명지대학교 자연캠퍼스 정보통신공학과 박찬 

### 💻 백엔드 (Spring Boot) 트랙
Java와 Spring Boot를 활용한 서버 개발을 학습하고 10주차에 걸쳐 과제를 수행했습니다. 
API 설계, 데이터베이스, JPA까지 백엔드 전반을 다뤘습니다.

### 주요 기능
- **멤버 CRUD**
    - 아기사자(LION) / 운영진(STAFF)을 구분하여 등록
    - 이름 중복 등록 시 (409 Conflict) 에러 응답
    - 학번이 숫자가 아니라면 (400 Bad Request 에러 응답)
    - 파트(part)별 필터링 조회 지원

- **과제 CRUD**
    - 멤버가 존재해야만 과제 등록 가능 (존재하지 않는 멤버 ID로 등록 시 404 에러)
    - 멤버별 / 제목 키워드 포함(Containing) 검색 / 전체 조회 지원

- **공통 예외 처리**
    - `@RestControllerAdvice` 기반 전역 예외 처리로 일관된 에러 응답 형식 제공
    - 404 (Not Found), 409 (Conflict) 등 상황별 적절한 HTTP 상태 코드 반환

- **프론트엔드 연동**
    - Spring Boot의 정적 리소스 서빙 기능을 활용해 `static/` 폴더에 프론트엔드 배치
    - HTTP 통신 로그 패널을 통해 요청/응답 흐름을 실시간으로 확인 가능

## 📅 커리큘럼 개요
| 주차 | 미션 | 핵심 키워드 | 단계 |                  블로그                  |
| :---: | :--- | :--- | :---: |:-------------------------------------:|
| **1주** | Java 핵심 문법 & 흐름 | `변수`, `조건문`, `반복문` | Java |                  미작성                   |
| **2주** | 객체지향 I - 클래스와 캡슐화 | `Class`, `Field`, `Method` | Java | [링크](https://talk40399.tistory.com/1) |
| **3주** | 객체지향 II - 상속/다형성/추상화 | `Inheritance`, `Polymorphism`, `Interface` | Java | [링크](https://talk40399.tistory.com/2) |
| **4주** | Java Collections & 설계 확장 | `List`, `Map`, `Generics` | Java | [링크](https://talk40399.tistory.com/3) |
| **5주** | 자바로 배우는 IoC/DI | `IoC`, `DI`, `Constructor Injection` | Java | [링크](https://talk40399.tistory.com/4) |
| **6주** | Spring Boot 전환 | `Spring Boot`, `Bean`, `Annotation` | Spring Core | [링크](https://talk40399.tistory.com/5) |
| **7주** | REST API 설계(CRUD) | `REST`, `HTTP`, `DTO` | Spring Core | [링크](https://talk40399.tistory.com/6) |
| **8주** | JPA 기초 & 영속성 컨텍스트 | `JPA`, `Entity`, `Repository` | JPA | [링크](https://talk40399.tistory.com/7) |
| **9주** | 연관관계 & 트랜잭션 | `@OneToMany`, `@ManyToOne`, `Transactional` | JPA | [링크](https://talk40399.tistory.com/8) |
| **10주** | 개인 미니 프로젝트: 예외 처리 통합 & 프론트엔드 연동 | `Architecture`, `Refactoring`, `Project` | Project | [링크](https://talk40399.tistory.com/9) |
---

## 📚 기술 스택

| 구분 | 기술 |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA |
| DB | MySQL |
|API|Springdoc OpenAPI (Swagger) 2.8.8|

---

## 📝 실행 방법

1. 저장소 클론
```bash
   git clone https://github.com/본인계정/저장소명.git
```

2. MySQL에 데이터베이스 생성
```sql
   CREATE DATABASE likelion_pbl;
```

3. `application.yml`에 DB 연결 정보 입력 (username, password)

4. 애플리케이션 실행
```bash
   ./gradlew bootRun
```

5. 브라우저에서 `http://localhost:8080` 접속
---

## ✅ API 목록

### Member API

| Method | URI | 설명 |
|---|---|---|
| POST | `/members/lions` | LION 등록 |
| POST | `/members/staffs` | STAFF 등록 |
| GET | `/members` | 전체 조회 (part 파라미터로 필터링 가능) |
| GET | `/members/{id}` | 단건 조회 |
| PUT | `/members/lions/{id}` | LION 수정 |
| PUT | `/members/staffs/{id}` | STAFF 수정 |
| DELETE | `/members/{id}` | 삭제 |

### Assignment API

| Method | URI | 설명 |
|---|---|---|
| POST | `/members/{memberId}/assignments` | 과제 등록 |
| GET | `/assignments` | 전체 조회 |
| GET | `/members/{memberId}/assignments` | 멤버별 과제 조회 |
| GET | `/assignments/{id}` | 단건 조회 |
| GET | `/assignments/search?keyword=` | 제목 검색 |
| PUT | `/assignments/{id}` | 수정 |
| DELETE | `/assignments/{id}` | 삭제 |

---
## 🌳 프로젝트 구조

```text
src/main/java
└── com
    └── BabyLion
        └── Spring
            ├── Application.java
            ├── PblApplication.java
            ├── assignment
            │   ├── controller
            │   │   └── AssignmentController.java
            │   ├── domain
            │   │   └── Assignment.java
            │   ├── dto
            │   │   ├── AssignmentCreateRequest.java
            │   │   ├── AssignmentResponse.java
            │   │   └── AssignmentUpdateRequest.java
            │   ├── repository
            │   │   └── AssignmentRepository.java
            │   └── service
            │       └── AssignmentService.java
            ├── global
            │   ├── dto
            │   │   └── ErrorResponse.java
            │   └── exeption
            │       ├── AssignmentNotFoundException.java
            │       ├── DuplicateMemberException.java
            │       ├── ErrorCodeEnum.java
            │       ├── GlobalExceptionHandler.java
            │       ├── InvalidStudentIdException.java
            │       └── MemberNotFoundException.java
            └── member
                ├── controller
                │   ├── HelloController.java
                │   └── MemberController.java
                ├── domain
                │   ├── Member.java
                │   └── RoleType.java
                ├── dto
                │   ├── LionCreateRequest.java
                │   ├── LionUpdateRequest.java
                │   ├── MemberResponse.java
                │   ├── StaffCreateRequest.java
                │   └── StaffUpdateRequest.java
                ├── repository
                │   └── MemberRepository.java
                └── service
                    └── MemberService.java
```