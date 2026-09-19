# 멋사 백엔드 심화 리서치 주제 & 담당자

> 배포/운영 트랙(`devops/` 1~7편)을 마친 뒤 진행하는 **심화 리서치 발표** 주제 목록입니다.
> 원하는 주제의 **담당자 칸에 GitHub ID를 적고** 커밋하면 그 주제는 본인 것이 됩니다.

---

## 0. 운영 규칙

### 담당자 등록 방법

1. GitHub에서 이 파일 우상단 ✏️(Edit) 클릭
2. 원하는 주제 행의 `담당자` 칸에 `@깃허브ID` 입력
3. `Commit changes`
   - 이 레포에 write 권한이 있는 참가자는 `main`에 바로 커밋됩니다.
   - 권한이 없으면 GitHub가 자동으로 fork + PR을 만들어 줍니다. PR 올리면 멘토가 머지합니다.
4. 이미 담당자가 있는 주제는 **협의 없이 덮어쓰지 않기.** 같이 하고 싶으면 `@a @b`로 공동 등록.
5. 1차로 **1인 1주제**. 전원이 고른 뒤 남는 주제는 2번째로 추가 선택 가능.
6. 없는 주제를 하고 싶으면 맨 아래 "제안된 추가 주제" 표에 행을 추가해서 제안.

### 깊이 기준 (이 기준을 못 넘으면 "얕은 발표"입니다)

발표는 **"이 주제를 처음 듣는 사람에게 설명"이 아니라 "이미 써본 사람이 잘못 알고 있던 걸 바로잡는"** 수준을 목표로 합니다.

| 필수 요소 | 설명 |
| --- | --- |
| **1차 자료** | 블로그 요약 금지. 공식 문서 / RFC / 논문 / 라이브러리 소스코드 중 최소 1개를 직접 읽고 인용 |
| **재현 가능한 데모 또는 실측** | 실패 케이스를 직접 재현하거나, 수치(레이턴시·처리량·용량·쿼리플랜)를 직접 측정. 코드는 레포에 올림 |
| **반론 / 트레이드오프** | "언제 이걸 쓰면 안 되는가", "업계에서 논쟁 중인 지점"을 최소 1개 |
| **우리 프로젝트 연결** | 멋사 프로젝트 코드나 인프라에 적용하면 무엇이 바뀌는지 한 단락 |
| **예상 질문 5개 + 답** | 발표 전 미리 준비. 청중이 공격할 지점을 스스로 예측 |

### 산출물: 리서치 본문은 `research/<깃허브ID>/` 아래에

리서치 본문과 데모 코드는 **사람별 디렉토리**에 넣습니다. 다른 사람 디렉토리는 건드리지 않습니다.

```
research/
├── RESEARCH_TOPICS.md            # 이 문서 (주제 목록 + 담당자)
├── README.md                     # 참가자별 디렉토리 링크 목록 (멘토가 관리)
├── jjangjjangsunho/
│   ├── README.md                 # 내 리서치 목록 + 한 줄 요약 + 발표일
│   ├── A1_transactional_pitfalls.md
│   └── A1_transactional_pitfalls/   # 데모 코드 (선택, 개인 레포 링크로 대체 가능)
│       └── ...
└── parkc31/
    ├── README.md
    └── C10_kinetic_layer_actions.md
```

규칙:

- 디렉토리 이름은 **GitHub ID 그대로** (대소문자 포함). 예: `research/SeokH-dev/`
- 리서치 파일 이름은 `<주제번호>_<영문slug>.md`. 주제번호는 이 문서의 `#` 칸(A1, B3, C10 …)과 일치시킵니다.
- 자기 디렉토리의 `README.md`에 리서치 목록·한 줄 요약·발표일을 유지합니다.
- 데모 코드가 크면 개인 레포에 두고 본문에서 링크. 작으면 같은 이름의 하위 폴더에 넣습니다.
- 본문 구조 권장: **주제문(한 문장) → 흔한 오해 → 내부 동작 → 재현/실측 → 트레이드오프와 반론 → 우리 프로젝트 적용 → 예상 질문 5개 → 참고한 1차 자료**
- 발표 시간: **30분 발표 + 15분 Q&A** 기준
- 발표 전날까지 `main`에 커밋 (write 권한 있는 참가자) 또는 PR (그 외)

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

## C. AI

> 백엔드 연동에 한정하지 않습니다. **모델 내부·학습·추론·평가처럼 순수 AI 주제도 환영**합니다.
> 단, 깊이 기준은 동일합니다. 논문/공식 문서를 직접 읽고, 직접 돌려본 실측이 있어야 합니다.

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

### C-2. 온톨로지: Palantir Foundry 문서 구조를 따라 4회차

> Palantir는 온톨로지를 **시맨틱 레이어**(Object · Property · Link · Interface)와 **키네틱 레이어**(Action · Function)로 나누고,
> 그 아래에 **아키텍처**(저장·인덱싱·권한·SDK), 그 위에 **AIP**(LLM 에이전트)를 얹습니다.
> 공식 문서(https://www.palantir.com/docs/foundry/ontology/overview)의 이 구분을 그대로 4회차로 나눕니다.
> 발표는 "Palantir가 이렇게 한다" 소개가 아니라, **같은 개념을 우리 Spring + PostgreSQL 스택으로 구현하면 어떻게 되는가**가 핵심입니다.
> 인원이 부족하면 C9+C10, C11+C12로 묶어서 2회차로 진행.

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| C9 | **시맨틱 레이어: Object · Property · Link · Interface** | Palantir 정의 그대로 읽기: Object Type(실세계 엔티티/이벤트의 스키마) vs Object(인스턴스) vs Object Set, Property와 Shared Property, Primary Key와 Title Property, Link Type의 백킹 방식(외래키 vs 다대다 조인 테이블), Interface(객체 타입 다형성), Value Type/Struct. 그리고 이것이 **관계형 스키마·JPA 엔티티·DDD 도메인 모델·RDF/OWL 온톨로지와 무엇이 다른가**. "데이터 모델이 아니라 실제 데이터에 매핑된다"(backing datasource)는 주장의 의미. 시맨틱 웹(RDF/OWL/SPARQL)이 기업에서 실패한 이유와 Palantir가 다르게 한 지점<br>**데모**: 멋사 프로젝트 도메인(유저·모임·게시글 등)을 Object/Link/Interface로 모델링하고, 기존 JPA 엔티티 모델과 나란히 비교. 하나의 Object Type이 여러 테이블(datasource)을 합쳐 백킹되는 경우 구현 | |
| C10 | **키네틱 레이어: Action · Function · Edits** | Action Type의 구성(Parameters, Submission Criteria, Rules, Side Effects/Webhook)과 "사용자가 한 번에 취하는 변경 집합"이라는 정의, Function(TypeScript/Python)과 Function-backed Action, 파이프라인 데이터와 사용자 편집(Edits)의 분리 저장, Edits-only Property, Writeback Dataset, 편집 충돌 처리. 이것이 **CQRS·커맨드 패턴·도메인 서비스·이벤트 소싱과 어디가 같고 다른가**. 왜 "임의 UPDATE"를 막고 Action만 허용하는가(감사·권한·LLM 안전과의 연결)<br>**데모**: C9 모델 위에 Action 계층 구현. 파라미터 검증 → 제출 조건 → 규칙 실행 → 사이드 이펙트(웹훅) 순서로 동작하고, 편집 이력이 원본 데이터와 분리 저장되는 것 시연 | |
| C11 | **온톨로지 아키텍처: 저장 · 인덱싱 · 권한 · SDK** | Object Storage V2(Phonograph 대체)가 왜 필요했나: 여러 datasource를 하나의 Object Type으로 인덱싱하고 검색·집계·링크 탐색을 빠르게 하는 구조, 파이프라인 데이터와 Edits를 합쳐 읽는 방식. 권한 모델: Roles, Restricted View(행 단위), Markings(분류 기반), 읽기 시점 강제와 다운스트림 보호의 한계. Ontology SDK(OSDK): 온톨로지에서 타입 있는 SDK를 생성(TS/Python/Java), Object Set 쿼리·집계·링크 로드·Action 적용·Function 호출, 스코프 토큰 + 사용자 권한 이중 적용<br>**데모**: PostgreSQL 위에 "읽기 모델 인덱스"(materialized view 또는 Elasticsearch)를 두고 Object Set 검색·집계 API 구현, 행 단위 권한 필터 적용. OpenAPI 스펙에서 클라이언트 SDK 자동 생성으로 OSDK 흉내 내기. 별도 인덱스 계층이 과한 데이터 규모는 어디까지인가 | |
| C12 | **AIP: LLM 에이전트가 온톨로지를 쓰는 법** | Palantir AIP가 LLM에 주는 것: 온톨로지가 **툴 목록**(Object 조회·Action 적용·Function 호출)이자 **그라운딩 컨텍스트**(Object Set 기반 retrieval)이자 **권한 경계**가 된다는 구조. 자유 SQL/코드 생성 대신 Action만 호출하게 하는 것의 안전성과 한계. AIP Logic(LLM을 Function처럼 파이프라인에 넣기)과 AIP Evals. 벡터 RAG(C2)·GraphRAG·MCP(C7)·자체 에이전트 루프(C4)와 비교: 온톨로지가 있으면 무엇이 쉬워지고 무엇은 여전히 어려운가<br>**실측**: C9~C11로 만든 온톨로지 위에 에이전트를 올려, (자유 SQL 생성 / 온톨로지 Action만 허용) 두 방식으로 같은 질의·조작 태스크를 수행. 정확도·위험한 동작 발생 횟수·토큰 비용 비교. C6 인젝션 시나리오로 공격도 해보기 | |

### C-3. 모델 내부 · 학습 · 추론 (순수 AI)

| # | 주제 | 반드시 다룰 것 · 데모 | 담당자 |
| --- | --- | --- | --- |
| C13 | **트랜스포머 내부: 어텐션에서 KV 캐시까지** | 토크나이저(BPE)와 한국어가 토큰을 많이 먹는 이유, 임베딩·위치 인코딩(RoPE), 셀프 어텐션의 계산 복잡도, 디코더 전용 구조와 자기회귀 생성, KV 캐시가 왜 필요하고 메모리를 얼마나 먹는지, 컨텍스트 길이가 늘면 무엇이 비싸지는가<br>**데모**: 수백 줄짜리 미니 GPT를 직접 구현·학습(nanoGPT 수준)하고 어텐션 맵 시각화. 토크나이저별 한국어 토큰 수 비교 | |
| C14 | **추론 최적화와 서빙** | 양자화(INT8/INT4, GPTQ/AWQ)가 품질에 미치는 영향 실측, PagedAttention과 연속 배칭(vLLM), speculative decoding, 처리량 vs 지연 트레이드오프, GPU 메모리 계산법(파라미터·KV 캐시·활성값), 로컬 서빙(Ollama/llama.cpp) vs API 비용 손익분기<br>**실측**: 같은 7B 모델을 FP16/INT8/INT4로 서빙해 처리량·지연·벤치마크 점수 비교 | |
| C15 | **파인튜닝 vs RAG vs 프롬프팅: 언제 무엇을** | 사전학습·SFT·RLHF/DPO 단계별 역할, LoRA/QLoRA 원리와 학습 비용, 파인튜닝이 지식 주입에 약한 이유, 판단 기준(지식 최신성·형식 학습·도메인 어휘·비용), 파인튜닝 데이터 품질 문제<br>**실측**: 소형 모델에 같은 태스크를 (프롬프팅 / RAG / LoRA 파인튜닝) 3방식으로 적용해 정확도·비용·지연 비교 | |
| C16 | **LLM 평가와 벤치마크의 함정** | 주요 벤치마크(MMLU, HumanEval, 한국어 벤치마크)가 측정하는 것과 못 하는 것, 데이터 오염, 리더보드 점수와 실사용 체감의 괴리, LLM-as-judge 편향(위치·길이·자기 선호), 통계적으로 의미 있는 비교(표본 수·신뢰 구간), 우리 태스크용 평가셋 만드는 법<br>**실측**: 모델 2~3개를 자체 평가셋으로 비교하고, 판정 모델을 바꿨을 때 순위가 뒤집히는지 확인 | |
| C17 | **임베딩 모델 내부와 선택** | 문장 임베딩 학습 방식(대조 학습, 하드 네거티브), bi-encoder vs cross-encoder, 차원 축소(Matryoshka)와 저장 비용, 한국어·다국어 임베딩 모델 비교, 도메인 특화 임베딩 파인튜닝, 임베딩이 못 잡는 것(부정·숫자·고유명사)<br>**실측**: 임베딩 모델 3종을 같은 한국어 검색 태스크로 recall 비교, 차원 줄였을 때 성능 변화 (C1과 연결) | |
| C18 | **AI 코딩 도구는 실제로 무엇을 바꾸는가** | 코드 생성 모델의 한계(맥락 부족·환각 API·보안 취약 코드 비율), 에이전틱 코딩(Claude Code·Cursor) 동작 구조와 컨텍스트 관리, 실증 연구 결과(생산성·품질 논쟁), 코드리뷰·테스트 생성 적용 시 검증 전략, 팀에 도입할 때의 규칙 설계<br>**실전**: 같은 기능을 (직접 / AI 보조 / 에이전트 위임) 3방식으로 구현해 시간·버그·리뷰 코멘트 수 비교 (C8과 연결) | |

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
C13 트랜스포머 내부 ─→ C14 추론 최적화 / C15 파인튜닝 vs RAG
C17 임베딩 모델 ─→ C1 pgvector ─→ C2 RAG ─→ C12 AIP
C16 LLM 평가 ─→ C5 LLM 앱 평가와 관측
C9 시맨틱 레이어 ─→ C10 키네틱 레이어 ─→ C11 아키텍처/SDK ─→ C12 AIP (C4 에이전트, C6 보안, C7 MCP와 합류)
D4 컨테이너 ─→ D9 쿠버네티스 ─→ D10 IaC
```

---

## 제안된 추가 주제 (PR로 추가해 주세요)

| # | 주제 | 반드시 다룰 것 · 데모 | 제안자 | 담당자 |
| --- | --- | --- | --- | --- |
| | | | | |
