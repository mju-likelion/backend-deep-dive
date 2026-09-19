# 멋사 백엔드 심화 리서치 주제 & 담당자

> 배포/운영 트랙(1~7편)을 마친 뒤 진행하는 **심화 리서치 발표** 주제 목록입니다.
> 원하는 주제의 **담당자 칸에 GitHub ID를 적고** 커밋하면 그 주제는 본인 것이 됩니다.

---

## 0. 운영 규칙

### 담당자 등록 방법

1. GitHub에서 이 파일 우상단 ✏️(Edit) 클릭
2. 원하는 주제 행의 `담당자` 칸에 `@깃허브ID` 입력
3. `Commit changes` → 쓰기 권한이 없으면 GitHub가 자동으로 fork + PR을 만들어 줍니다. PR 올리면 멘토가 머지합니다.
4. 이미 담당자가 있는 주제는 **협의 없이 덮어쓰지 않기.** 같이 하고 싶으면 `@a @b`로 공동 등록.
5. 1차로 **1인 1주제**. 전원이 고른 뒤 남는 주제는 2번째로 추가 선택 가능.
6. 없는 주제를 하고 싶으면 해당 섹션 맨 아래에 행을 추가해서 PR로 제안.

### 깊이 기준 (이 기준을 못 넘으면 "얕은 발표"입니다)

발표는 **"이 주제를 처음 듣는 사람에게 설명"이 아니라 "이미 써본 사람이 잘못 알고 있던 걸 바로잡는"** 수준을 목표로 합니다.

| 필수 요소 | 설명 |
| --- | --- |
| **1차 자료** | 블로그 요약 금지. 공식 문서 / RFC / 논문 / 라이브러리 소스코드 중 최소 1개를 직접 읽고 인용 |
| **재현 가능한 데모 또는 실측** | 실패 케이스를 직접 재현하거나, 수치(레이턴시·처리량·용량·쿼리플랜)를 직접 측정. 코드는 레포에 올림 |
| **반론 / 트레이드오프** | "언제 이걸 쓰면 안 되는가", "업계에서 논쟁 중인 지점"을 최소 1개 |
| **우리 프로젝트 연결** | 멋사 프로젝트 코드나 인프라에 적용하면 무엇이 바뀌는지 한 단락 |
| **예상 질문 5개 + 답** | 발표 전 미리 준비. 청중이 공격할 지점을 스스로 예측 |

### 산출물

- 발표 자료: 이 레포에 `topics/<번호>_<영문slug>.md` 로 커밋 (예: `topics/A1_transactional_pitfalls.md`)
- 데모 코드: 같은 폴더 하위 또는 개인 레포 링크
- 발표 시간: **30분 발표 + 15분 Q&A** 기준

---

## A. Spring / Java 심화

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| A1 | **`@Transactional`이 안 먹는 순간들** | AOP 프록시 구조(JDK 동적 프록시 vs CGLIB), self-invocation, private/final 메서드, `readOnly`의 실제 효과(Hibernate flush 모드·DB 힌트), 전파 속성별 롤백(REQUIRES_NEW·NESTED), 체크 예외 롤백 안 되는 이유, `@Async`와 만나면 트랜잭션이 끊기는 이유<br>**데모**: 실패 케이스 6종 이상 테스트 코드로 재현 | |
| A2 | **영속성 컨텍스트 내부와 N+1** | 1차 캐시, 더티 체킹(스냅샷 비교 비용), flush 타이밍과 `AUTO` 모드, 쓰기 지연 SQL 저장소, `merge` vs `persist`, 준영속 상태 함정<br>**실측**: fetch join vs `@EntityGraph` vs `default_batch_fetch_size` vs DTO 프로젝션을 같은 데이터로 쿼리 수·응답 시간 비교. 페이징 + fetch join 메모리 경고 재현. OSIV on/off 커넥션 점유 시간 비교 | |
| A3 | **가상 스레드(Loom) vs WebFlux** | 플랫폼 스레드 모델의 한계(스레드당 스택, 컨텍스트 스위칭), 가상 스레드가 바꾸는 것과 안 바꾸는 것, pinning(`synchronized`·네이티브 프레임), JDK 24에서 pinning 개선 내용, 스레드 로컬·커넥션 풀 병목<br>**실측**: 같은 I/O 바운드 시나리오를 (MVC 플랫폼 스레드 / MVC 가상 스레드 / WebFlux) 3종 부하 테스트, 처리량·p99·메모리 비교 | |
| A4 | **Spring Security 6 필터체인 해부** | `SecurityFilterChain` 순서와 각 필터 역할, `SecurityContextHolder` 저장 전략, 세션 vs JWT 비교(무효화·탈취·크기), 리프레시 토큰 회전과 재사용 감지, OAuth2 Authorization Code + PKCE 플로우, 흔한 토큰 취약점(alg=none, 키 혼동, 만료 미검증, 로컬스토리지 저장)<br>**데모**: 필터체인 디버그 로그로 요청 하나 추적, 취약한 JWT 검증 코드 공격 재현 | |
| A5 | **JVM 메모리와 GC** | 힙 구조(Young/Old, TLAB), 메타스페이스, 네이티브 메모리(다이렉트 버퍼·스레드 스택), G1 동작 원리와 리전, ZGC/Generational ZGC, STW가 생기는 지점, GC 로그 읽기<br>**데모**: 메모리 릭 코드 → OOM → 힙덤프 → MAT/VisualVM으로 원인 객체 추적. 컨테이너 메모리 제한과 `MaxRAMPercentage` 실측 | |
| A6 | **Spring Boot 자동설정 원리 + 직접 스타터 만들기** | `@Conditional*` 계열 동작, `AutoConfiguration.imports`, 자동설정 순서(`@AutoConfigureAfter`), 사용자 빈이 우선되는 원리(`@ConditionalOnMissingBean`), `spring-boot-configuration-processor`로 IDE 자동완성<br>**데모**: 공통 로깅·예외처리·응답 포맷을 담은 사내 스타터를 만들어 두 프로젝트에 적용. `--debug`로 조건 평가 리포트 읽기 | |
| A7 | **Spring 이벤트와 트랜잭션 경계** | `ApplicationEventPublisher`, `@TransactionalEventListener`의 phase별 동작, AFTER_COMMIT에서 DB 쓰기가 조용히 사라지는 이유, `@Async` 이벤트와 예외 전파, 도메인 이벤트로 결합도 낮추기, D2(아웃박스)로 이어지는 한계<br>**데모**: 결제 완료 → 알림 발송을 이벤트로 분리하고, 커밋 실패 시 알림이 안 나가는지 검증 | |
| A8 | **Java 동시성과 메모리 모델(JMM)** | happens-before, `volatile`이 보장하는 것과 아닌 것, CAS와 `Atomic*`, `synchronized` vs `ReentrantLock` vs `StampedLock`, `ConcurrentHashMap` 내부, 스레드풀 설정(코어·큐·거부정책)이 잘못되면 생기는 장애, `CompletableFuture` 조합과 예외 처리<br>**데모**: 데이터 레이스 재현(JCStress 또는 반복 실행), 스레드풀 큐 무한 증가로 OOM 재현 | |
| A9 | **예외 설계와 에러 응답** | 체크 vs 언체크 논쟁, 예외를 흐름 제어에 쓰면 안 되는 이유(스택트레이스 비용 실측), 계층별 예외 변환, `@ControllerAdvice` 우선순위, RFC 9457 Problem Details 적용, 클라이언트가 분기 가능한 에러 코드 설계<br>**데모**: 예외 발생 비용 벤치마크, Problem Details 표준 응답 구현 | |

---

## B. PostgreSQL 심화

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| B1 | **인덱스 내부와 EXPLAIN ANALYZE 읽는 법** | B-tree 페이지 구조, 복합 인덱스 컬럼 순서(선택도·동등/범위 조건), 커버링 인덱스와 `INCLUDE`, Index Only Scan이 Heap Fetch를 하는 이유(visibility map), 부분 인덱스, 플래너 통계와 `ANALYZE`, `Seq Scan`이 더 빠른 경우<br>**실측**: 100만 건 넣고 인덱스 순서 바꿔가며 플랜·시간 비교. `buffers` 옵션으로 I/O 읽기 | |
| B2 | **MVCC와 VACUUM** | 스냅샷 격리와 튜플 xmin/xmax, dead tuple 발생 원리, HOT 업데이트, autovacuum 트리거 조건과 튜닝 파라미터, 테이블 bloat 측정, XID wraparound와 강제 vacuum, `VACUUM FULL`의 락<br>**데모**: "왜 DELETE 했는데 용량이 안 줄지?" 재현 → `pgstattuple`로 bloat 확인 → 해결. 장기 트랜잭션이 vacuum을 막는 상황 재현 | |
| B3 | **트랜잭션 격리 수준 실측** | READ COMMITTED / REPEATABLE READ / SERIALIZABLE에서 실제로 막히는 이상 현상(dirty·non-repeatable·phantom·write skew·lost update), PostgreSQL이 REPEATABLE READ에서 phantom을 막는 이유, SSI 구현과 직렬화 실패 재시도<br>**데모**: 세션 2개로 각 이상 현상을 격리 수준별로 재현하는 스크립트 | |
| B4 | **락과 동시성 제어** | 락 종류(row/table/advisory), `SELECT FOR UPDATE` vs `FOR NO KEY UPDATE`, 데드락 감지와 `pg_locks` 읽기, FK가 만드는 숨은 락<br>**실전**: 재고 차감 동시성 이슈를 (비관 락 / 낙관 락 `@Version` / 원자적 UPDATE / Redis 분산락) 4가지로 해결하고 처리량·정확성 비교. 데드락 의도적으로 재현 | |
| B5 | **커넥션 풀 튜닝** | 커넥션이 비싼 이유(프로세스 fork), HikariCP 풀 사이즈 공식과 근거, `maxLifetime`·`idleTimeout`, pgbouncer 모드(session/transaction)와 prepared statement 제약, 커넥션 고갈 시 증상<br>**데모**: 느린 쿼리 + 작은 풀로 고갈 장애 재현 → 풀 메트릭으로 진단 → 튜닝 | |
| B6 | **복제와 읽기 분산** | 스트리밍 복제(물리) vs 논리 복제, WAL 구조, replication lag 원인, 읽기 레플리카 라우팅(`AbstractRoutingDataSource`, `readOnly` 기준 분기), 방금 쓴 데이터가 레플리카에서 안 보이는 문제(read-your-writes) 대응, 페일오버<br>**데모**: docker compose로 primary + replica 구성, lag 측정, 라우팅 구현 | |
| B7 | **파티셔닝과 대용량 테이블 운영** | 선언적 파티셔닝(range/list/hash), 파티션 프루닝 조건, 파티션 키 선택 실수, 오래된 파티션 드롭으로 대량 삭제 대체, 샤딩과의 차이와 샤딩을 미루는 기준<br>**실측**: 시간 기반 로그 테이블 1000만 건, 파티션 유무 쿼리·삭제 성능 비교 | |
| B8 | **무중단 스키마 마이그레이션** | Flyway/Liquibase 동작, `ALTER TABLE`이 잡는 락과 테이블 리라이트 발생 조건, `CREATE INDEX CONCURRENTLY`, NOT NULL 컬럼 추가 안전 절차, expand-contract 패턴, 롤백 가능한 마이그레이션, 배포와 마이그레이션 순서<br>**데모**: 트래픽 걸린 상태에서 인덱스 추가 시 락 대기 재현 → 안전한 방법으로 재시도 | |
| B9 | **JSONB · 전문검색 · 확장 기능** | JSONB 저장 구조와 GIN 인덱스, `jsonb_path_ops`, 정규화 vs JSONB 판단 기준, `tsvector`/`tsquery` 기반 전문검색과 한국어 형태소 한계, `pg_trgm`, 유용한 확장(`pg_stat_statements`, `pg_cron`)<br>**실측**: JSONB 필드 검색 인덱스 유무 비교, LIKE vs trgm vs 전문검색 비교 | |

---

## C. AI 백엔드 통합

### C-1. LLM 애플리케이션 백엔드

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| C1 | **임베딩과 벡터 검색을 pgvector로** | 임베딩이 표현하는 것과 못 하는 것, 코사인 vs 내적 vs L2, HNSW vs IVFFlat 구조·빌드 시간·recall 트레이드오프, 필터링과 벡터 검색을 함께 쓸 때의 문제(post-filter recall 저하), 별도 벡터DB로 넘어가야 하는 기준<br>**실측**: 10만~100만 벡터에서 인덱스별 recall@10·p99 비교 | |
| C2 | **RAG 파이프라인 설계** | 주제문: "왜 대부분의 RAG는 성능이 안 나오는가". 청킹 전략(고정/재귀/문서구조/시맨틱), 청크 크기와 오버랩, 하이브리드 검색(BM25 + 벡터, RRF), 리랭커, 쿼리 재작성·HyDE, 컨텍스트 창에 넣는 순서, 평가 없이 튜닝하면 안 되는 이유<br>**실측**: 같은 문서셋에 청킹·검색 전략 바꿔가며 정답률 비교 | |
| C3 | **LLM API 프로덕션 연동** | SSE 스트리밍 구현(Spring `SseEmitter`/WebFlux)과 프록시·타임아웃 함정, 재시도와 지수 백오프, rate limit(429) 처리와 큐잉, 토큰 카운팅·비용 관리, 프롬프트 캐싱 원리와 절감 실측, 모델 폴백, 구조화 출력(JSON 스키마) 검증<br>**데모**: 스트리밍 채팅 API + 강제 429/타임아웃 상황에서의 복구 시연 | |
| C4 | **AI 에이전트 백엔드 아키텍처** | 툴 콜링 루프 구현과 종료 조건, 대화 상태 저장(전체 히스토리 vs 요약 vs 외부 메모리), 긴 작업의 잡 큐 분리와 진행 상태 조회, human-in-the-loop 승인 단계 설계, 멱등한 툴 실행, 비용·스텝 상한, 멀티 에이전트가 필요한 경우와 과한 경우<br>**데모**: DB 조회 + 외부 API 호출 툴을 가진 에이전트를 잡 큐 기반으로 구현 | |
| C5 | **LLM 앱 평가와 관측** | 골든셋 구성과 회귀 테스트를 CI에 넣는 법, LLM-as-judge의 편향과 보정, 프롬프트 버저닝과 배포 전략, 트레이싱(입력·출력·토큰·비용·지연 단위), 사용자 피드백을 평가셋으로 회수하는 루프<br>**데모**: 프롬프트 변경 PR이 골든셋 점수를 떨어뜨리면 CI가 실패하는 파이프라인 | |
| C6 | **프롬프트 인젝션과 AI 보안** | OWASP LLM Top 10, 직접/간접 인젝션(문서·웹·툴 결과 통해 유입), 신뢰 경계 설계(모델 출력은 항상 untrusted), 툴 권한 최소화와 읽기/쓰기 분리, 출력 검증, 데이터 유출 경로, 완전한 방어가 없다는 전제에서의 설계<br>**데모**: RAG 문서에 인젝션 심어 툴 오작동 유도 → 방어 계층 추가 후 재시도 | |
| C7 | **MCP 서버 직접 만들기** | 프로토콜 스펙(JSON-RPC, 3 프리미티브, 트랜스포트), 스테이트리스 HTTP 전환 의미, 툴 스키마 설계가 모델 정확도에 미치는 영향, 인증·권한, 툴 포이즈닝 방어, REST 래핑과의 차이<br>**데모**: 우리 서비스 DB를 읽는 MCP 서버 구현 → Claude Code/Claude Desktop에 연결. (기존 `MCP.md` 참고) | |
| C8 | **Claude Code 스킬/플러그인으로 팀 워크플로 자동화** | `CLAUDE.md` 설계 원칙(무엇을 넣고 무엇을 빼는가), 커스텀 스킬·서브에이전트·훅, 코드리뷰 자동화, 컨텍스트 비용 관리, 팀 컨벤션을 도구로 강제하는 법<br>**실전**: 실제 팀 레포에 적용해서 2주 써본 결과(도움된 것·방해된 것) 가져오기 | |

### C-2. 온톨로지 & 지식그래프

> 온톨로지는 한 회차로 묶으면 "RDF가 뭔지" 소개로 끝나기 쉬워서 **3회차로 나눕니다.**
> 기초(C9) → LLM 결합(C10) → 서비스 적용(C11) 순서로, 각각 독립 발표 가능하지만 앞 회차를 전제로 합니다.
> 인원이 부족하면 C9+C10을 한 명이 묶어서 진행.

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| C9 | **온톨로지 기초: 지식을 스키마로 표현하기** | 온톨로지 vs 택소노미 vs 관계형 스키마 vs 도메인 모델의 차이, RDF 트리플·OWL 클래스/속성/제약, SPARQL 기본, 추론(reasoning)이 실제로 해주는 것(전이 관계·타입 추론)과 비용, 표준 온톨로지 사례(schema.org, FIBO, SNOMED), 왜 시맨틱 웹은 실패했다고 하는가<br>**데모**: 우리 프로젝트 도메인(유저·게시글·모임 등)을 OWL로 모델링하고 SPARQL로 추론 결과 확인 | |
| C10 | **지식그래프 + LLM: GraphRAG** | 벡터 RAG가 못 하는 질문 유형(다중 홉·집계·관계 질문), 텍스트에서 엔티티·관계 추출(LLM 기반 트리플 추출의 품질 문제), 그래프 DB 선택(Neo4j vs Postgres 위 Apache AGE vs 그냥 관계형 테이블), Microsoft GraphRAG의 커뮤니티 요약 방식, 그래프 질의를 LLM이 생성할 때의 위험<br>**실측**: 같은 문서셋으로 벡터 RAG(C2)와 GraphRAG를 질문 유형별 정답률·비용 비교 | |
| C11 | **서비스 온톨로지: 에이전트를 위한 시맨틱 레이어** | Palantir Foundry식 온톨로지(객체·링크·액션)가 왜 다시 주목받는가, DDD 애그리거트·바운디드 컨텍스트와의 관계, 에이전트가 툴을 온톨로지 통해 발견·호출하는 구조(C4·C7 연결), 사내 데이터 용어 통일(시맨틱 레이어)이 LLM 정확도에 미치는 영향, 과설계 경계<br>**데모**: 우리 서비스의 도메인 객체·관계·허용 액션을 온톨로지로 정의하고, 에이전트가 그것만 보고 질의·조작하게 만들기 | |

---

## D. 인프라 / 아키텍처

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| D1 | **Redis 캐시 전략** | 캐시 아사이드 / 라이트 스루 / 라이트 비하인드, 캐시 스탬피드와 해결(락·확률적 조기 만료·요청 병합), TTL 설계와 지터, 캐시 일관성(갱신 순서 문제), 분산락과 Redlock 논쟁(Martin Kleppmann vs antirez), Redis 자료구조별 메모리·시간 복잡도, 키 설계와 빅 키 문제<br>**데모**: 스탬피드 재현 → 해결 전후 DB 부하 비교 | |
| D2 | **메시지 큐와 비동기** | Kafka vs RabbitMQ vs SQS 모델 차이(로그 vs 브로커), 파티션과 순서 보장, 컨슈머 그룹·리밸런싱, 아웃박스 패턴(A7과 연결), 멱등 컨슈머 설계, at-least-once 위에서 exactly-once의 진실, DLQ와 재처리, 백프레셔<br>**데모**: 아웃박스 + 멱등 컨슈머로 중복 메시지 상황에서도 정확히 1번 처리 검증 | |
| D3 | **관측성 3요소와 OpenTelemetry** | 로그/메트릭/트레이스의 역할과 상호 연결(trace id 전파), OTel SDK·Collector 구조, 자동 계측 vs 수동 계측, 샘플링 전략, 카디널리티 폭발, RED/USE 메서드, SLO와 에러 버짓<br>**실습**: Spring 앱 2개 + DB 호출을 분산 추적으로 연결(Tempo/Jaeger + Grafana), 느린 요청 원인 추적 | |
| D4 | **컨테이너 내부** | namespace·cgroup이 실제로 격리하는 것, 컨테이너 안에서 JVM이 CPU/메모리를 잘못 인식하는 문제, 이미지 레이어와 캐시 무효화, 멀티스테이지 빌드, distroless/JRE 최소화, 레이어드 JAR, 이미지 취약점 스캔<br>**실측**: Spring Boot 이미지 300MB → 80MB 과정을 단계별 크기·빌드 시간으로 기록 | |
| D5 | **무중단 배포 전략** | 롤링 / 블루그린 / 카나리 비교, 헬스체크(liveness vs readiness)와 graceful shutdown(진행 중 요청 처리), 커넥션 드레이닝, DB 마이그레이션과의 순서(B8 연결), 롤백 조건 자동화, 피처 플래그<br>**구현**: GitHub Actions로 카나리 배포 + 에러율 기준 자동 롤백 | |
| D6 | **Testcontainers와 테스트 전략** | 테스트 피라미드 vs 트로피 논쟁, 단위 테스트에서 mock 과다 사용 문제, Testcontainers로 실제 DB·Redis·Kafka 통합 테스트, 컨테이너 재사용으로 속도 개선, 플레이키 테스트 원인 분류(시간·순서·공유 상태·비동기)와 격리, 테스트 데이터 전략<br>**실측**: H2 vs Testcontainers PostgreSQL에서 통과/실패가 달라지는 쿼리 사례 | |
| D7 | **회복탄력성 패턴** | 타임아웃 계층 설계(연결·읽기·전체), 재시도가 장애를 증폭시키는 경우(retry storm)와 백오프·지터, 서킷 브레이커 상태 전이, 벌크헤드, 폴백 설계, Resilience4j 적용 순서(데코레이터 순서가 결과를 바꾼다)<br>**데모**: 느린 외부 API 하나가 전체 스레드풀을 고갈시키는 장애 재현 → 패턴 적용 후 격리 확인 | |
| D8 | **성능 테스트와 병목 찾기** | 부하 테스트 설계(목표 지표·시나리오·워밍업), k6/Gatling, 처리량-지연 곡선과 knee point, JVM 프로파일링(async-profiler, JFR), 플레임그래프 읽기, DB·네트워크·GC·락 병목 구분법, 벤치마크에서 흔한 실수(JIT 워밍업 무시, 동일 머신 측정)<br>**실습**: 의도적으로 병목 3종을 심은 앱을 프로파일링으로 찾아내기 | |
| D9 | **쿠버네티스 핵심과 "언제 과한가"** | Pod/Deployment/Service/Ingress 모델, 선언적 상태와 컨트롤러 루프, 리소스 요청/제한과 QoS, 롤링 업데이트·프로브, ConfigMap/Secret, 한 대 서버·docker compose·ECS·k8s 선택 기준, 운영 비용의 실체<br>**실습**: kind 또는 k3s에 Spring 앱 배포, 노드 죽였을 때 자가 복구 시연 | |
| D10 | **IaC와 환경 재현성** | Terraform 상태 파일·plan/apply·drift, 모듈 설계, 시크릿을 IaC에 넣지 않는 법, 환경별(dev/prod) 분리, 클릭옵스에서 IaC로 넘어가는 마이그레이션 전략, GitOps 개념<br>**실습**: 현재 Lightsail/EC2 + DNS 구성을 Terraform으로 코드화하고 처음부터 재생성 | |
| D11 | **모듈러 모놀리스 vs MSA** | MSA가 해결하는 조직 문제 vs 만드는 기술 문제(분산 트랜잭션·네트워크·관측), 모듈러 모놀리스 설계(패키지 경계·ArchUnit·Spring Modulith), 분해 기준(바운디드 컨텍스트·변경 빈도·팀), Saga와 보상 트랜잭션, 서비스 간 데이터 공유 안티패턴<br>**데모**: 멋사 프로젝트를 모듈 경계로 나누고 ArchUnit으로 의존 규칙 강제 | |

---

## E. CS / 개발자 소양

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| E1 | **HTTP/1.1 → 2 → 3와 TLS 핸드셰이크** | HOL 블로킹의 종류(HTTP/1.1 파이프라이닝 vs HTTP/2 TCP 레벨), 스트림·멀티플렉싱, HPACK, QUIC이 UDP 위에 올린 것, TLS 1.3 핸드셰이크 1-RTT·0-RTT, ALPN, 인증서 체인 검증<br>**실습**: Wireshark/tcpdump로 HTTPS 요청 캡처해서 핸드셰이크 단계 보기, HTTP/1.1 vs 2 자원 다수 로딩 시간 비교 | |
| E2 | **TCP 심화와 서버 튜닝** | 3-way handshake·4-way close, TIME_WAIT가 쌓이는 이유와 서버 측 close 문제, keep-alive와 커넥션 재사용, Nagle·delayed ACK, 혼잡 제어(CUBIC vs BBR), 소켓 백로그와 `SYN` 큐, `ulimit`·파일 디스크립터 한계<br>**데모**: 짧은 연결을 대량 생성해 TIME_WAIT 폭증 및 포트 고갈 재현 → keep-alive로 해결 | |
| E3 | **I/O 모델과 이벤트 루프** | 블로킹/논블로킹/비동기의 정확한 구분, `select`·`poll`·`epoll`·`io_uring`, C10K 문제, 톰캣 NIO 커넥터 구조(acceptor·poller·worker), Netty 이벤트 루프, Node.js와 비교, 이벤트 루프를 블로킹하면 생기는 일<br>**데모**: 톰캣 스레드 덤프로 요청 처리 구조 확인, 이벤트 루프 블로킹 코드 재현 | |
| E4 | **OS: 프로세스·스레드·메모리** | 프로세스 vs 스레드 실제 차이(리눅스에선 둘 다 task), 컨텍스트 스위칭 비용 실측, 가상 메모리·페이지 테이블·페이지 폴트, 스왑이 JVM에 치명적인 이유, mmap과 페이지 캐시(DB·Kafka가 빠른 이유), OOM Killer 동작<br>**실측**: 스레드 수 늘려가며 컨텍스트 스위칭 측정(`perf`/`vmstat`), 컨테이너 메모리 제한 초과 시 OOM Killer 로그 | |
| E5 | **분산 시스템 기초** | CAP와 PACELC의 실제 의미(CAP 오해 바로잡기), 선형성·순차 일관성·최종 일관성, 논리 시계와 순서, 리더 선출과 합의(Raft 동작 원리), 분산 락의 근본적 한계(D1 연결), 멱등성과 정확히 한 번의 의미(D2 연결), 장애 감지와 타임아웃<br>**데모**: Raft 시각화 도구 또는 미니 구현으로 네트워크 분단 시 동작 확인 | |
| E6 | **실무 자료구조와 저장 엔진** | 해시테이블 충돌·리사이징과 Java `HashMap` 트리화, 블룸 필터, 스킵리스트(Redis sorted set), B-tree vs LSM 트리(PostgreSQL vs RocksDB/Cassandra) 읽기·쓰기 증폭, WAL, 시간 복잡도만 보면 놓치는 캐시 지역성<br>**실측**: `ArrayList` vs `LinkedList` 중간 삽입 실측(교과서와 다른 결과), 블룸 필터로 캐시 미스 방어 | |
| E7 | **암호학 기초와 백엔드 적용** | 해시 vs 암호화 vs 서명 vs MAC, 비밀번호 저장(bcrypt/scrypt/argon2, 왜 SHA-256 단독은 안 되는가), 대칭(AES-GCM)·비대칭(RSA/ECDSA) 사용처, JWT 서명 알고리즘 선택(HS256 vs RS256 vs EdDSA), TLS 인증서, 난수 생성(`SecureRandom`), 흔한 구현 실수(IV 재사용, 타이밍 공격)<br>**데모**: 잘못된 비교로 생기는 타이밍 공격 재현, 비밀번호 해시 알고리즘별 비용 비교 | |
| E8 | **문자열·시간·숫자: 실무 버그의 근원** | 유니코드·UTF-8·정규화(NFC/NFD, 한글 자소 분리 버그), 이모지와 `length()`의 불일치, DB collation, 타임존·DST·`Instant` vs `LocalDateTime` vs `ZonedDateTime` 선택, DB `timestamp` vs `timestamptz`, 부동소수점 오차와 돈 계산(`BigDecimal`), 정수 오버플로<br>**데모**: 실제 장애 사례 5개를 재현하는 테스트 코드 | |
| E9 | **API 설계론** | REST의 한계(리소스 모델링이 안 되는 행위, 과다/과소 페칭), gRPC(스트리밍·스키마 진화) / GraphQL(N+1·복잡도 제어) 선택 기준, 버저닝 전략과 하위 호환 규칙, 페이지네이션(offset vs cursor), 멱등키, 에러 응답 표준 RFC 9457, OpenAPI 스펙 우선 개발<br>**데모**: 같은 도메인을 REST/gRPC/GraphQL로 구현해 트레이드오프 비교 | |
| E10 | **시스템 디자인 종합** | "우리 프로젝트에 트래픽 100배가 오면": 병목 예측(DB 쓰기·읽기·세션·파일), 읽기 분산·캐시·큐·CDN 도입 순서, 데이터 규모별 선택 변화, 비용 추정, 단일 장애점 제거<br>**실습**: 설계 문서 작성 → 다른 참가자가 공격(리뷰) → 수정. 발표는 공격받은 지점과 수정 근거 중심 | |
| E11 | **Git 내부 구조** | 객체 모델(blob/tree/commit/tag)과 콘텐츠 주소화, ref와 HEAD, rebase가 실제로 하는 일(커밋 재생성), merge 전략(ort, squash, fast-forward)의 히스토리 차이, reflog로 복구, `bisect`로 회귀 찾기, 대용량 레포와 부분 클론<br>**데모**: `.git` 디렉터리 직접 뜯어보기, 잘못된 `reset --hard` 후 reflog로 살려내기 | |
| E12 | **JVM 실행 원리: 바이트코드·JIT·클래스 로딩** | 바이트코드와 `javap`, 클래스 로더 계층과 `ClassNotFoundException` vs `NoClassDefFoundError`, 인터프리터 → C1 → C2 티어드 컴파일, 인라이닝·탈출 분석, 워밍업이 벤치마크를 망치는 이유(JMH), AOT/GraalVM 네이티브 이미지의 트레이드오프<br>**실측**: JMH로 워밍업 전후 성능 차이, 네이티브 이미지 vs JIT 기동 시간·처리량 비교 | |
| E13 | **소프트웨어 설계: 클린/헥사고날 아키텍처 실전** | 레이어드 vs 헥사고날 vs 클린의 실제 차이(의존 방향), 포트·어댑터가 테스트에 주는 이점, 과설계 비판(간단한 CRUD에 5계층), 도메인 모델 vs 트랜잭션 스크립트, 패키지 구조 논쟁(계층별 vs 기능별)<br>**실전**: 멋사 프로젝트 한 도메인을 헥사고날로 리팩터링해 전후 테스트 용이성·코드량 비교 | |

---

## 주제 간 연결 맵

먼저 들으면 좋은 순서 (강제는 아님):

```
A1 @Transactional ─→ A7 이벤트/트랜잭션 경계 ─→ D2 아웃박스/큐 ─→ E5 분산 시스템
A2 영속성 컨텍스트 ─→ B1 인덱스/EXPLAIN ─→ B4 락 ─→ B3 격리 수준
A3 가상 스레드 ─→ E3 I/O 모델 ─→ E4 OS
B8 마이그레이션 ─→ D5 무중단 배포
C1 pgvector ─→ C2 RAG ─→ C10 GraphRAG
C9 온톨로지 기초 ─→ C10 GraphRAG ─→ C11 서비스 온톨로지 (C4 에이전트, C7 MCP와 합류)
D4 컨테이너 ─→ D9 쿠버네티스 ─→ D10 IaC
```

---

## 제안된 추가 주제 (PR로 추가해 주세요)

| # | 주제 | 반드시 다룰 것 · 데모 | 제안자 | 담당자 |
| --- | --- | --- | --- | --- |
| | | | | |
