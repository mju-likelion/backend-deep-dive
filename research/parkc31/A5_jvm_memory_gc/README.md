# A5 JVM 메모리와 GC — 데모 코드

JDK 21 기준. `./run_all.sh` 한 번이면 A~F 실험이 모두 돌고 결과가 `results/`에 저장된다.
Docker가 있으면 `./run_container_check.sh`로 실제 컨테이너 제한 조건을 확인한다.

| 실험 | 파일 | 보여주는 것 | 본문 장 |
|---|---|---|---|
| A | (옵션만) | 메모리·CPU에 따른 기본 힙(25%/50%)과 기본 GC(Serial/G1) | 9장 |
| B | `AgeDemo.java` | Survivor 나이 분포 → 오래 살수록 적어짐 (약한 세대 가설) | 3장 |
| C | `AllocationChurn.java` | G1 한 사이클이 로그에 찍히는 순서, 빡빡한 힙에서 Evacuation Failure → Full GC, 힙덤프의 STW | 4~6장 |
| D | `LeakDemo.java`, `hprof_chain.py` | static 리스트 누수 → GC 후 사용량 계단식 증가 → OOM → 힙덤프 → 누수 경로 | 2, 10장 |
| E | `NativeMemoryDemo.java` | 힙 64MB인데 JVM 전체는 그보다 훨씬 큼 (NMT), 다이렉트 메모리 기본 한도 = 최대 힙 | 1, 8장 |
| F | `DirectBufferDemo.java` | `-XX:+DisableExplicitGC`가 다이렉트 버퍼 회수 경로를 막아 OOM | 8장 |

참고: 이 환경은 CPU가 1개라 옵션 없이 실행하면 Serial GC가 선택된다. G1 실험은 `-XX:+UseG1GC`를 명시했다.
`hprof_chain.py`는 MAT 없이 누수 경로를 확인하는 간이 분석기다. 발표에서는 `results/D_leak.hprof`를 MAT로 열어 Dominator Tree → Path to GC Roots 화면을 보여준다.
