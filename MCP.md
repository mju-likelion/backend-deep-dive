# MCP (Model Context Protocol) 기술 발표 자료

> **대상**: 백엔드 / 플랫폼 엔지니어
> **분량**: 발표 25분 + Q&A 5분
> **기준 스펙**: `2026-07-28` (현행)
> **작성일**: 2026-09-17

---

## 0. 발표 구성 및 시간 배분

| # | 섹션 | 시간 | 핵심 메시지 |
|---|------|------|-------------|
| 1 | 문제 정의: M×N | 3분 | 통합 비용은 곱셈으로 증가한다 |
| 2 | MCP 아키텍처 1장 요약 | 3분 | Host/Client/Server, 3 프리미티브, 2 트랜스포트 |
| 3 | **왜 REST가 아니라 MCP인가 (심화)** | **8분** | 계약의 소비자가 사람에서 모델로 바뀌었다 |
| 4 | 반론과 한계 | 4분 | 컨텍스트 세금, Perplexity 이탈, 대응책 |
| 5 | 2026-07-28: 스테이트리스 전환 | 5분 | 이제야 평범한 HTTP 서비스처럼 운영 가능 |
| 6 | 생태계·거버넌스·로드맵 | 3분 | Linux Foundation 산하, 확장 모델로 진화 |
| 7 | 보안 & 도입 체크리스트 | 2분 | 툴 포이즈닝은 실존 위협 |
| — | Q&A | 5분 | 예상 질문 10선 준비 |

---

## 1. 문제 정의 — M×N 통합 지옥

LLM 에이전트에 사내 시스템을 붙이려면, 모델/호스트(M개)마다 시스템(N개)별 어댑터를 따로 짜야 합니다.

```
Claude Desktop  ─┐          ┌─ Jira
Cursor          ─┤          ├─ Confluence
VS Code Copilot ─┼─  ???  ─ ┼─ 사내 DB
자체 에이전트    ─┘          └─ 사내 배포 API
```

- 어댑터 개수 = **M × N**
- 각 어댑터마다 인증, 에러 처리, 스키마 변환, 재시도 로직이 중복
- 호스트가 하나 추가되면 N개를 다시 짬

MCP의 주장: 이걸 **M + N**으로 바꾼다. 서버는 한 번 만들고, 모든 호스트가 같은 프로토콜로 붙는다.

> 비유: "AI 애플리케이션의 USB-C". 다만 발표에서 한 번만 쓰고 넘어가길 권합니다. 개발자 청중은 비유보다 프로토콜 레벨의 근거를 원합니다.

---

## 2. MCP 아키텍처 — 1장 요약

### 2.1 구성 요소

| 역할 | 설명 | 예시 |
|------|------|------|
| **Host** | LLM을 품고 있는 애플리케이션. 사용자 승인·정책의 주체 | Claude Desktop, Cursor, VS Code |
| **Client** | Host 내부에서 서버 1개와 1:1로 대응하는 커넥터 | 호스트가 서버당 하나씩 생성 |
| **Server** | 실제 기능을 노출하는 프로세스/서비스 | GitHub MCP, 사내 배포 MCP |

### 2.2 서버가 노출하는 3가지 프리미티브

| 프리미티브 | 제어 주체 | 성격 | REST 대응 개념 |
|-----------|----------|------|---------------|
| **Tools** | 모델 (model-controlled) | 부수효과 있는 행동 | `POST /orders` |
| **Resources** | 애플리케이션 (app-controlled) | 읽기 전용 컨텍스트, URI로 식별 | `GET /docs/{id}` |
| **Prompts** | 사용자 (user-controlled) | 서버가 배포하는 재사용 워크플로 템플릿 | (대응 개념 없음) |

이 3분할이 핵심입니다. REST에는 "누가 이 호출을 결정하는가"라는 축이 아예 없습니다. MCP는 그걸 프로토콜에 박아 넣었습니다.

### 2.3 메시지 & 트랜스포트

- **메시지**: JSON-RPC 2.0
- **트랜스포트**: `stdio`(로컬 프로세스) / `Streamable HTTP`(원격)
  - 구 `HTTP+SSE`는 `2025-03-26`부터 deprecated, `2026-07-28`에서 정식 Deprecated 등급으로 재분류

---

## 3. 핵심 — 왜 REST API 대신 MCP인가

> 발표에서 가장 많은 시간을 써야 하는 부분. 개발자는 "REST로 다 되는데?"부터 묻습니다.

### 3.1 한 문장 답

**REST API의 계약(OpenAPI)은 사람과 컴파일 타임 코드가 읽도록 설계됐고, MCP의 계약은 런타임에 모델이 읽도록 설계됐습니다.**

OpenAPI 문서는 개발자가 읽고 → 클라이언트 코드를 짜고 → 배포합니다. 계약 해석이 **빌드 타임**에 끝납니다.
MCP는 에이전트가 실행 중에 `tools/list`를 호출해 → 그 자리에서 무엇을 할 수 있는지 알아내고 → 호출합니다. 계약 해석이 **런타임**에 일어납니다.

이 차이에서 나머지 모든 차이가 파생됩니다.

### 3.2 5개 축 비교

#### 축 1 — 디스커버리

| | REST | MCP |
|---|------|-----|
| 능력 발견 | 사람이 문서 읽음 / 코드젠 | `tools/list` 런타임 조회 |
| 변경 반영 | 재빌드·재배포 필요 | `listChanged` 알림 또는 TTL 만료 후 재조회 |
| 설명 형식 | 사람용 산문 | 자연어 description + JSON Schema (모델이 소비하도록 튜닝) |

핵심: 사내 배포 툴에 `rollback` 기능이 추가됐을 때, REST 기반 에이전트는 코드를 고쳐 배포해야 합니다. MCP 기반은 서버만 올리면 그날부터 에이전트가 롤백을 씁니다.

> **주의**: OpenAPI → MCP 자동 변환은 안티패턴입니다. 200개 엔드포인트를 200개 툴로 기계 변환하면 모델이 선택을 못 합니다. 좋은 MCP 서버는 "엔드포인트 미러링"이 아니라 **"사용자가 달성하려는 결과(outcome) 단위"**로 툴을 설계합니다.
> 예: `GET /users` + `GET /users/{id}/orders` + `POST /refunds` 3개 → `refund_last_order_for_customer` 1개.

#### 축 2 — 통합 비용 구조

REST는 통합 지점마다 인증 방식, 페이지네이션 규약, 에러 포맷, 재시도 정책이 전부 다릅니다. 에이전트 개발자는 그 차이를 전부 흡수하는 어댑터를 씁니다.
MCP는 그 규약을 프로토콜이 고정합니다. → 어댑터 코드가 사라지고, 서버 벤더가 한 번 구현합니다.

실제로 벤더들이 자사 REST API를 유지하면서 **그 위에 MCP 서버를 얹는** 형태가 표준이 됐습니다. MCP는 REST의 대체재가 아니라 **에이전트 전용 프레젠테이션 레이어(BFF의 AI판)**입니다. 이 프레이밍이 개발자 청중에게 가장 잘 먹힙니다.

```
        [ Agent ]
            │  MCP (JSON-RPC: tools/list, tools/call)
            ▼
   [ MCP Server = Agent BFF ]
            │  기존 REST / gRPC / DB
            ▼
   [ 기존 마이크로서비스 ]
```

#### 축 3 — 상호작용 패턴

REST는 요청/응답 1왕복이 전부입니다. "이 작업 진행할까요?"를 중간에 물을 방법이 없습니다.
MCP `2026-07-28`은 **MRTR(Multi Round-Trip Requests)**로 이걸 프로토콜에 넣었습니다.

```json
// 서버 응답: 완료가 아니라 "입력 필요"
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "resultType": "input_required",
    "inputRequests": [ { "...": "사용자 확인 요청" } ],
    "requestState": "opaque-server-token"
  }
}
```

클라이언트는 같은 요청을 `inputResponses`를 붙여 재시도합니다. 프로덕션 배포 승인, 누락 파라미터 수집, 결제 확인 같은 시나리오가 표준 방식으로 해결됩니다.

#### 축 4 — 인증/인가

MCP는 인증을 "알아서 하세요"로 남기지 않고 **의견을 가집니다**.

- 원격 서버는 **OAuth 2.1 Resource Server**로 규정 (`2025-06-18` 이후)
- `2026-07-28`에서 강화된 항목:
  - RFC 9207 `iss` 파라미터 검증 필수 → **authorization server mix-up 공격** 차단
  - 클라이언트 자격증명은 발급한 AS에 바인딩. 다른 AS에 재사용 금지, AS 변경 시 재등록 필수
  - DCR(RFC 7591)은 **deprecated** → **Client ID Metadata Documents** 권장
  - DCR 시 `application_type` 명시 요구 (데스크톱/CLI의 localhost 리다이렉트 충돌 해소)

REST에서 이건 팀마다 제각각입니다. MCP는 최소한 "표준 스택이 뭔지"에 답을 줍니다.

#### 축 5 — 왜 JSON-RPC인가 (REST도 gRPC도 아니고)

| 기준 | REST | gRPC | JSON-RPC (MCP 선택) |
|------|------|------|---------------------|
| 모델 | 명사(리소스) 중심 | 메서드 | **동사(메서드) 중심** |
| 트랜스포트 | HTTP 전용 | HTTP/2 전용 | **stdio·HTTP·커스텀 모두** |
| 툴링 | 낮음 | protoc 코드젠 필수 | 낮음, 가독성 높음 |
| 디버깅 | 쉬움 | 바이너리라 어려움 | 쉬움 |

결정타는 **트랜스포트 독립성**입니다. 로컬 `stdio`로 도는 서버와 원격 HTTP 서버가 **완전히 같은 메시지 포맷**을 씁니다. REST를 골랐으면 로컬 프로세스 실행은 별도 규약이 필요했습니다.

또 툴 호출은 본질적으로 `run_analysis()`라는 **동사**이지 리소스 CRUD가 아닙니다. REST에 억지로 매핑하면 `POST /tools/run_analysis/invoke` 같은 RPC-over-REST가 되고, 그럴 바엔 JSON-RPC가 정직합니다.

### 3.3 그래서 — 언제 MCP를 쓰고, 언제 REST로 충분한가

**MCP가 값을 하는 경우**

- 에이전트가 **런타임에** 능력을 발견해야 할 때
- 여러 호스트(Claude Code / Cursor / 사내 에이전트)에서 **같은 통합**을 재사용해야 할 때
- 작업 중간에 **사람 확인**이 필요할 때
- 자격증명을 개발자 로컬에 뿌리지 않고 **중앙 서버에서 OAuth로 통제**하고 싶을 때 ← 사내 도입에서 실제로 가장 큰 명분
- 서버가 배포하는 문서/워크플로(Prompts, Resources)를 조직 전체에 자동 동기화하고 싶을 때

**REST로 충분한 경우**

- 워크플로가 고정되어 있고 개발자가 코드를 작성하는 경우
- 고빈도·저지연이 지배적이고 HTTP 캐싱/CDN이 중요한 경우
- 외부 개발자용 공개 API (SDK가 답)
- 툴이 1~2개뿐인 단일 목적 에이전트 → 그냥 함수 호출이 낫습니다

---

## 4. 반론과 한계 — 정직하게 짚고 가기

> 이 섹션을 넣으면 발표 신뢰도가 크게 올라갑니다. 청중은 이미 "MCP 죽었다"는 글을 봤을 가능성이 높습니다.

### 4.1 컨텍스트 세금 (Context Tax)

가장 실질적인 비판입니다. 서버를 연결하면 **모든 툴 스키마가 시스템 프롬프트에 매 턴 주입**됩니다.

- 서버 7개 연결 시 약 **67,300 토큰** 소모 — 200K 컨텍스트의 33.7%
- GitHub MCP 하나가 27개 툴로 약 **18,000 토큰**
- 툴이 많아지면 **툴 선택 정확도 자체가 붕괴**한다는 보고 (43% → 14% 수준)

**Perplexity 사례 (2026년 3월)**: CTO Denis Yarats가 MCP를 버리고 REST + CLI로 회귀, 자체 Agent API 발표. 근거는 이 "컨텍스트 세금"이었습니다.

### 4.2 업계의 대응 — Progressive Disclosure

세 가지 해법이 나와 있고, 모두 원리는 같습니다: **필요해질 때까지 툴 정의를 로드하지 않는다.**

| 해법 | 주체 / 시점 | 절감 | 방식 |
|------|-----------|------|------|
| **Tool Search** | Anthropic, 2025-11 | ~85% | 툴을 `defer_loading`으로 두고 시맨틱 검색으로 필요한 것만 로드 |
| **Code Execution with MCP** | Anthropic, 2025-11 | ~98.7% | 서버를 import 가능한 TS 모듈로 제시, 샌드박스에서 코드 실행 (150K → 2K 토큰) |
| **Code Mode** | Cloudflare, 2026-02 | ~99.9% | API를 타입드 SDK로 노출, `search()`/`execute()` 2개 툴로 축약 |

실무 가이드라인:

- 툴 30개 미만 → 최적화 불필요
- 30~200개 → Tool Search
- 100개 이상 → Code Mode / Code Execution 패턴 (샌드박스 필요)

### 4.3 남은 진짜 한계

- **로컬 stdio MCP는 CLI보다 나을 게 없는 경우가 많습니다.** 단순 API 래퍼 MCP 서버는 실제로 가치가 없습니다. MCP의 가치는 **원격·중앙화·거버넌스**에서 나옵니다.
- REST 생태계의 수십 년 축적(캐싱, CDN, 로드밸런싱, 관측)을 MCP는 아직 못 따라갑니다. → 다만 `2026-07-28`이 정확히 이 지점을 공략합니다 (다음 섹션).
- 스펙 변화 속도가 빠릅니다. 1년에 4~5회 개정, 그중 하나는 브레이킹 체인지였습니다.

**결론 프레이밍**: 승패가 아니라 **전문화**입니다. IDE·데스크톱 통합은 MCP, 프로덕션 에이전트 파이프라인은 REST, 특수 워크플로는 CLI — GraphQL과 gRPC가 HTTP 옆에서 자리를 찾은 것과 같은 경로입니다.

---

## 5. 2026-07-28 스펙 — 스테이트리스 전환

> 백엔드/플랫폼 엔지니어에게 **가장 중요한 섹션**. "MCP 서버를 어떻게 운영하나"에 대한 답이 여기서 바뀌었습니다.

### 5.1 무엇이 바뀌었나

| 항목 | 이전 (~2025-11-25) | 현행 (2026-07-28) |
|------|-------------------|------------------|
| 세션 | `Mcp-Session-Id` 헤더, 프로토콜 레벨 세션 | **제거**. 서버가 필요하면 툴 인자로 핸들을 명시적으로 전달 |
| 핸드셰이크 | `initialize` / `notifications/initialized` | **제거**. 매 요청의 `_meta`가 버전·능력을 운반 |
| 능력 광고 | `initialize` 응답 | **`server/discover`** RPC (서버 구현 필수) |
| 서버→클라이언트 | HTTP GET + `resources/subscribe` | **`subscriptions/listen`** 단일 롱리브드 POST 스트림, 타입별 옵트인 |
| 서버 주도 요청 | `sampling/createMessage`, `elicitation/create`, `roots/list` | **MRTR** (`resultType: "input_required"`)로 대체 |
| 결과 포맷 | 자유 | 모든 결과에 **`resultType`** 필수 (`"complete"` / `"input_required"`) |
| 스트림 복구 | `Last-Event-ID` 재전송 | **제거**. 끊기면 새 request ID로 재발행 |
| 제거된 메서드 | — | `ping`, `logging/setLevel`, `notifications/roots/list_changed` |

### 5.2 왜 중요한가 — 운영 관점

```
[변경 전] 클라이언트 ── sticky session ──> 특정 인스턴스
          세션 상태 공유 스토리지 필요, 오토스케일 시 세션 유실

[변경 후] 클라이언트 ── 평범한 round-robin LB ──> 아무 인스턴스
          공유 스토리지 불필요, 무상태 컨테이너처럼 배포
```

추가로 백엔드가 좋아할 항목들:

1. **헤더 기반 라우팅** — `Mcp-Method`, `Mcp-Name` 헤더가 필수화. 게이트웨이/레이트리미터가 **JSON 바디를 파싱하지 않고** 라우팅·인가·쿼터 적용 가능.
2. **캐시 힌트 표준화** — `tools/list`, `prompts/list`, `resources/read` 등의 결과에 `ttlMs`와 `cacheScope`(`public`/`private`) 필수. 중간 캐시 계층을 표준적으로 끼울 수 있습니다.
3. **결정적 정렬** — `tools/list`는 결정적 순서로 반환 권장. 클라이언트 캐싱 + **LLM 프롬프트 캐시 히트율** 개선 목적.
4. **OpenTelemetry** — `_meta`의 `traceparent` / `tracestate` / `baggage` 키로 트레이스 컨텍스트 전파 규약 문서화. 에이전트 → MCP 서버 → 내부 서비스 분산 추적이 표준 경로를 얻었습니다.
5. **에러 코드 정책** — `-32000~-32019` 구현 정의, `-32020~-32099` 스펙 예약. resource not found는 `-32002` → `-32602`(Invalid Params)로 정정.

#### 참고: `server/discover` 응답 예시

```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "resultType": "complete",
    "supportedVersions": ["2026-07-28"],
    "capabilities": {
      "tools": {},
      "extensions": { "io.modelcontextprotocol/ui": {} }
    },
    "_meta": {
      "io.modelcontextprotocol/serverInfo": { "name": "ExampleServer", "version": "1.0.0" }
    },
    "ttlMs": 3600000,
    "cacheScope": "public"
  }
}
```

### 5.3 Deprecated — 신규 구현은 쓰지 말 것

12개월 최소 유예를 보장하는 **feature lifecycle 정책**이 새로 도입됐습니다 (Active → Deprecated → Removed).

| 대상 | 권장 대체 |
|------|----------|
| **Roots** | 디렉터리/파일을 툴 파라미터, 리소스 URI, 서버 설정으로 전달 |
| **Sampling** | LLM 프로바이더 API에 직접 통합 |
| **Logging** | stdio는 `stderr`로, 원격은 OpenTelemetry |
| HTTP+SSE 트랜스포트 | Streamable HTTP |
| OAuth DCR (RFC 7591) | Client ID Metadata Documents |

Sampling 폐기는 논쟁적입니다. "서버가 클라이언트의 LLM을 빌려 쓴다"는 아이디어는 우아했지만, 스테이트리스 전환과 양립하지 않았습니다.

### 5.4 마이그레이션 상태

Tier 1 SDK 4종(TypeScript, Python, Go, C#) + 베타 Rust SDK가 모두 신규 스펙을 지원합니다. 세션 의존 구현체는 브레이킹 체인지를 맞습니다.

---

## 6. 생태계 · 거버넌스 · 로드맵

### 6.1 스펙 버전 타임라인

| 버전 | 주요 내용 |
|------|----------|
| **2024-11-05** | 최초 스펙. JSON-RPC 2.0, Tools/Resources/Prompts, stdio + HTTP+SSE |
| **2025-03-26** | OAuth 2.1 인가 프레임워크, Streamable HTTP 도입, 툴 어노테이션 |
| **2025-06-18** | 구조화된 툴 출력, Elicitation, Resource Links, 서버를 OAuth Resource Server로 규정 |
| **2025-11-25** | OIDC 기반 인가 디스커버리, 아이콘 메타데이터, 실험적 Tasks, JSON Schema 2020-12 |
| **2026-07-28** | **스테이트리스 전환**, MRTR, MCP Apps 확장, 공식 폐기 정책 |

### 6.2 채택 마일스톤

| 시점 | 사건 |
|------|------|
| 2025-03 | Microsoft Copilot Studio, OpenAI Agents SDK 채택 |
| 2025-04 | Google, Gemini 모델 MCP 지원 발표 |
| 2025-05 | Microsoft Build 2025 — 플랫폼 전반 1급 지원 |
| 2025-10 | Amazon Bedrock AgentCore GA |
| **2025-12-09** | **Anthropic이 MCP를 Linux Foundation 산하 Agentic AI Foundation(AAIF)에 기증** |
| 2026-08-13 | AAIF 신규 회원 57곳 합류, **총 247개 조직** (Gold: Alibaba, Visa, Wells Fargo) |

거버넌스 포인트: MCP는 더 이상 Anthropic 소유가 아닙니다. AAIF는 MCP, goose, AGENTS.md, agentgateway를 호스팅하며 Executive Director는 Mazin Gilbert입니다. **벤더 락인 우려에 대한 답변**으로 이 사실을 꼭 넣으세요.

### 6.3 생태계 규모 (참고 수치 — 비공식 집계)

- 2026년 4월 기준 PulseMCP·공식 레지스트리·Smithery·mcp.so 합산 **약 9,400개 서버** (2025년 말 ~6,800개 대비 +38%)
- 카테고리: 커넥터/SaaS 38%, 개발 도구 27%, 데이터/검색 18%, 시스템/브라우저 11%, 크리에이티브 6%
- 프로덕션급 호스트 6종: Claude Desktop, Claude Code, Cursor, Codex CLI, Windsurf, VS Code + Copilot

> 발표 시 "비공식 집계, 방향성 참고용"이라고 명시하세요. 공식 레지스트리 카운트가 아닙니다.

### 6.4 확장(Extensions) 모델 — 구조적으로 중요한 변화

코어 스펙은 작게 유지하고, 나머지는 **확장**으로 뺍니다.

- 식별자 형식: `{vendor-prefix}/{extension-name}` (예: `io.modelcontextprotocol/tasks`)
- 서드파티는 역도메인 사용 (`com.example/my-extension`)
- **항상 기본 비활성**, 명시적 옵트인
- 협상: 클라이언트는 `_meta`의 `clientCapabilities.extensions`, 서버는 `server/discover` 응답의 `capabilities.extensions`
- 코어와 **독립적으로 버전 진화**

공식 확장 목록:

| 확장 | 내용 |
|------|------|
| **MCP Apps** (`ext-apps`, 2026-01-26) | 대화 안에 인터랙티브 UI(차트, 폼, 플레이어) 인라인 렌더 |
| **MCP Tasks** | 장기 실행 작업. 폴링(`tasks/get`), 중간 입력(`tasks/update`), 지속 핸들 |
| **Skills over MCP** (`ext-skills`) | 서버가 Agent Skills(워크플로 지침 + 지원 파일)를 배포 |
| **Auth 확장** (`ext-auth`) | OAuth Client Credentials(M2M), Enterprise-Managed Authorization |

### 6.5 2026 로드맵 — 4대 축

릴리스 기반 계획에서 **Working Group 기반**으로 전환했습니다.

1. **트랜스포트 진화·확장성** — 스테이트리스 세션 처리, 수평 확장, **`.well-known` 메타데이터 포맷**(라이브 커넥션 없이 서버 디스커버리). 트랜스포트 종류는 의도적으로 적게 유지.
2. **에이전트 커뮤니케이션** — Tasks 프리미티브 성숙, 일시적 실패 재시도 시맨틱, 결과 만료 정책.
3. **거버넌스 성숙** — contributor ladder, Working Group에 SEP 리뷰 권한 위임(병목 제거), Core Maintainer는 전략 감독.
4. **엔터프라이즈 준비도** — 감사 추적, SSO, 게이트웨이 동작, 설정 이식성. **의도적으로 가장 덜 정의된 영역**이며 대부분 코어가 아닌 확장으로 처리 예정.

고정 릴리스 날짜는 없습니다. Working Group이 자체 일정을 정합니다.

---

## 7. 보안 — 반드시 짚어야 할 실존 위협

> 사내 도입을 논의하는 자리라면 이 섹션이 의사결정을 좌우합니다.

### 7.1 주요 위협

| 위협 | 메커니즘 |
|------|---------|
| **Tool Poisoning** | 툴 description에 숨은 지시문 삽입. 프로토콜이 description을 권위 있는 지침으로 취급 |
| **Auto-Execution** | IDE가 workspace trust 수락만으로 개발자 권한으로 MCP 서버 기동. 별도 코드 실행 경고 없음 |
| **Rug Pull** | 승인 후 서버가 툴 정의를 조용히 변경 |
| **Confused Deputy** | MCP 서버가 보유한 상위 권한을 공격자가 대리 행사 |
| **공급망 증폭** | 오염된 리포 설정 파일 하나가 팀 전체로 전파 |

### 7.2 실측 데이터 및 사고 사례

- **MCPTox 벤치마크**: 20개 모델 평균 툴 포이즈닝 성공률 **36.5%**, 최고 **72.8%**
- **CurXecute** (CVE-2025-54135, CVSS 8.6) — Slack 메시지 인젝션으로 SSH 키 유출
- **MCPoison** (CVE-2025-54136) — 커밋된 설정 변경으로 팀 전체 침해
- **Miasma 웜** (2026-06) — 73개 GitHub 리포(Microsoft `azure/durabletask` 포함)에 적대적 MCP 설정 심어 자격증명 수집

### 7.3 완화책 (사내 도입 시 최소 요건)

- MCP 서버 인벤토리 관리 + **명시적 allowlist**
- **MCP 설정 변경을 코드 리뷰 대상**으로 취급 (`.mcp.json`은 실행 가능한 코드입니다)
- 툴 description 스캐닝 (숨은 지시문 탐지)
- MCP 프로세스를 프로덕션 자격증명에서 격리 (컨테이너, 임시 토큰)
- **단계적 신뢰 모델** — 신규 서버는 read-only로 시작, 행동 검증 후 승격
- IDE 패치 버전 유지 (Cursor 1.3.9+ 등)

---

## 8. 도입 체크리스트 & 결론

### 8.1 사내 도입 판단 기준

```
Q1. 에이전트가 런타임에 능력을 발견해야 하는가?
    NO  → 그냥 REST 호출하는 코드를 쓰세요.
    YES ↓
Q2. 통합을 2개 이상의 호스트에서 재사용하는가?
    NO  → 단일 호스트 전용 함수 툴로 충분할 수 있음
    YES ↓
Q3. 자격증명/감사를 중앙에서 통제해야 하는가?
    YES → 원격 MCP 서버 (OAuth 2.1, Streamable HTTP)  ← 사내 도입의 표준 답
    NO  → 로컬 stdio MCP 또는 CLI 검토
Q4. 툴 개수가 30개를 넘는가?
    YES → Tool Search / Code Execution 패턴 함께 설계
```

### 8.2 서버 설계 원칙 3가지

1. **엔드포인트가 아니라 결과(outcome) 단위로 툴을 설계하라.** OpenAPI 자동 변환 금지.
2. **툴 개수를 예산으로 관리하라.** 토큰은 유한 자원입니다.
3. **description은 모델을 위한 프롬프트다.** 사람용 문서를 복붙하지 마세요. 동시에 — 그래서 description은 신뢰 경계이기도 합니다.

### 8.3 결론 (발표 마무리 문구)

> MCP는 REST를 대체하지 않습니다. MCP는 **에이전트를 위한 새로운 계약 계층**이고, 그 아래에는 여전히 여러분의 REST API가 있습니다.
>
> 2026-07-28 스테이트리스 전환으로 MCP 서버는 드디어 "평범한 무상태 HTTP 서비스"가 됐습니다. 즉, 여러분이 이미 아는 방식으로 배포·확장·관측할 수 있습니다.
>
> 남은 진짜 과제는 프로토콜이 아니라 **컨텍스트 예산 관리**와 **신뢰 경계 설계**입니다.

---

## 9. 예상 Q&A 10선

**Q1. 그냥 REST API에 OpenAPI 스펙 주면 되지 않나요?**
됩니다. 단, 계약 해석이 빌드 타임에 고정됩니다. OpenAPI는 사람용 산문이라 모델 소비에 최적화돼 있지 않고, 200개 엔드포인트를 그대로 주면 모델이 선택에 실패합니다. MCP의 차이는 (1) 런타임 디스커버리 (2) 변경 알림(`listChanged`) (3) 중간 사용자 확인(MRTR) (4) 트랜스포트 독립성입니다. 정적 워크플로면 OpenAPI + 코드젠이 더 낫습니다.

**Q2. 벤더 락인 아닌가요? Anthropic 것 아닌가요?**
2025년 12월 Linux Foundation 산하 Agentic AI Foundation에 기증됐습니다. 2026년 8월 기준 247개 조직이 참여하고 Gold 멤버에 Alibaba, Visa, Wells Fargo가 있습니다. Microsoft, Google, OpenAI, AWS 모두 채택했습니다.

**Q3. 성능은요? JSON-RPC 오버헤드가 있지 않나요?**
있습니다. 하지만 병목은 직렬화가 아니라 **LLM 추론 시간과 토큰**입니다. 툴 호출 1회의 네트워크 왕복은 모델 생성 시간 대비 무시할 수준입니다. 고빈도 저지연 서비스 간 통신에 MCP를 쓰지 마세요 — 그건 gRPC의 영역입니다.

**Q4. 토큰 낭비 문제는 해결됐나요?**
프로토콜 차원에서 완전히는 아닙니다. 현재는 클라이언트 측 패턴으로 해결합니다 — Tool Search(~85% 절감), Code Execution(~98.7%), Cloudflare Code Mode(~99.9%). 스펙 쪽에서는 `ttlMs`/`cacheScope` 캐시 힌트와 결정적 툴 정렬로 프롬프트 캐시 히트율을 올리는 방향으로 대응 중입니다.

**Q5. Perplexity가 MCP 버렸다던데요?**
2026년 3월 맞습니다. 컨텍스트 세금이 이유였고, 자체 Agent API로 갔습니다. 다만 Perplexity는 **자사 제품 내부 파이프라인**을 최적화한 케이스입니다. 호스트를 자기가 통제하면 MCP의 상호운용성 가치가 없습니다. 반대로 여러 IDE·에이전트에서 쓰이는 사내 시스템을 노출하는 상황이면 판단이 달라집니다.

**Q6. 기존 REST 서버를 그대로 MCP로 감싸면 되나요?**
기술적으로는 가능하지만 권장하지 않습니다. 엔드포인트 미러링은 툴 폭발 → 선택 정확도 하락으로 이어집니다. "사용자가 달성하려는 결과" 단위로 재설계해야 하고, 그래서 개발 공수가 단순 래핑보다 큽니다.

**Q7. 2026-07-28 브레이킹 체인지 때문에 지금 도입하기 이른 것 아닌가요?**
오히려 반대 타이밍입니다. 스테이트리스 전환이 기존 인프라(LB, 오토스케일, 게이트웨이)와의 마찰을 제거했고, 12개월 최소 유예를 보장하는 공식 폐기 정책이 이번에 도입됐습니다. 예측 가능성이 이전보다 좋아졌습니다. 다만 신규 구현은 Roots/Sampling/Logging을 쓰지 마세요.

**Q8. Sampling이 폐기되면 서버가 LLM을 어떻게 쓰나요?**
LLM 프로바이더 API에 직접 통합하는 것이 권장 경로입니다. Sampling은 "서버가 클라이언트의 모델을 빌려 쓴다"는 개념이었는데, 스테이트리스 코어와 양립하지 않았습니다. 대신 MRTR로 "사용자 입력이 필요한 경우"는 여전히 커버됩니다.

**Q9. 보안 리스크가 큰데 사내 도입해도 되나요?**
allowlist, 설정 파일 코드 리뷰, 단계적 신뢰(신규 서버 read-only 시작), 프로덕션 자격증명 격리 — 이 4가지 없이 도입하면 안 됩니다. 툴 포이즈닝 성공률이 모델 평균 36.5%로 측정된 실존 위협입니다. 반대로 말하면, **중앙 MCP 서버 + OAuth**는 개발자 로컬에 API 키를 뿌리는 현재 상태보다 오히려 안전합니다.

**Q10. A2A(Agent2Agent)와는 어떤 관계인가요?**
MCP는 **에이전트 ↔ 도구/데이터** 수직 통합, A2A는 **에이전트 ↔ 에이전트** 수평 통신입니다. 경쟁이 아니라 보완이고, 현재 둘 다 Linux Foundation 산하에서 거버넌스됩니다.

---

## 10. 참고 자료

### 공식 출처 (스펙·로드맵·거버넌스)

- [Key Changes — MCP Specification 2026-07-28](https://modelcontextprotocol.io/specification/2026-07-28/changelog)
- [The 2026-07-28 Specification | MCP Blog](https://blog.modelcontextprotocol.io/posts/2026-07-28/)
- [The 2026 MCP Roadmap | MCP Blog](https://blog.modelcontextprotocol.io/posts/2026-mcp-roadmap/)
- [Extensions Overview — Model Context Protocol](https://modelcontextprotocol.io/extensions/overview)
- [MCP joins the Agentic AI Foundation | MCP Blog](https://blog.modelcontextprotocol.io/posts/2025-12-09-mcp-joins-agentic-ai-foundation/)
- [AAIF Welcomes 57 New Members — Linux Foundation](https://www.linuxfoundation.org/press/agentic-ai-foundation-welcomes-57-new-members-gaining-major-financial-services-players-and-apac-leaders)

### 분석·비교 (2차 출처)

- [MCP Specification Version Timeline — hidekazu-konishi.com](https://hidekazu-konishi.com/entry/mcp_specification_version_timeline.html)
- [MCP vs. REST: What's the right way to connect AI agents to your API? — WorkOS](https://workos.com/blog/mcp-vs-rest)
- [Why MCP Uses JSON-RPC Instead of REST or gRPC — DEV Community](https://dev.to/om_shree_0709/why-mcp-uses-json-rpc-instead-of-rest-or-grpc-1gpo)
- [MCP Ecosystem H1 2026 Retrospective — Digital Applied](https://www.digitalapplied.com/blog/mcp-ecosystem-h1-2026-retrospective-adoption-data-points)
- [MCP Context Bloat Fix 2026 — MCP.Directory](https://mcp.directory/blog/mcp-context-bloat-fix-2026-tool-search-code-mode-progressive-disclosure)
- [Perplexity Ditches MCP: The 72% Context Tax — AgentMarketCap](https://agentmarketcap.ai/blog/2026/04/13/perplexity-mcp-exit-rest-api-vs-mcp-agent-tool-integration)
- [MCP is Dead; Long Live MCP! — Charles Chen](https://chrlschn.dev/blog/2026/03/mcp-is-dead-long-live-mcp/)

### 보안

- [CSA Research Note: MCP Tool Poisoning and Auto-Execution](https://labs.cloudsecurityalliance.org/research/csa-research-note-mcp-tool-poisoning-auto-execution-20260701/)
- [MCP Tool Poisoning — OWASP Foundation](https://owasp.org/www-community/attacks/MCP_Tool_Poisoning)

---

### 사용 시 참고사항

- **수치 신뢰도**: 스펙 변경 내역·로드맵·거버넌스는 공식 출처 기반입니다. 서버 개수(9,400개), 채택률, 토큰 절감률은 2차 출처 추정치이므로 발표에서 "업계 추정"이라고 표시하는 게 안전합니다.
- **분량 조절**: 전체를 다 읽으면 30분을 넘깁니다. 섹션 3(REST 비교)과 5(스테이트리스)를 주축으로 하고, 6·7은 슬라이드를 넘기며 요약하는 방식을 권장합니다.
