# parkc31 리서치

| 주제 | 문서 | 한 줄 요약 | 발표일 |
|---|---|---|---|
| A5 | [JVM 메모리와 GC](./A5_jvm_memory_gc.md) | `-Xmx`는 힙만 제한한다. 힙 밖 네이티브 메모리까지 합친 전체가 컨테이너 한도를 넘으면 예외 없이 137로 죽는 이유를 G1·ZGC 동작과 실측으로 설명한다 | (미정) |
| E6 | [실무 자료구조와 저장 엔진](./E6_data_structures_storage_engines.md) | 빅오는 연산 횟수만 센다. ArrayList가 LinkedList를 18배 이기는 이유부터 HashMap 트리화, 블룸 필터, 스킵리스트, B-tree vs LSM, WAL까지 "느린 곳에 몇 번, 어떤 순서로 가느냐"로 설명하고 실측한다 | (미정) |

데모 코드와 측정 결과: [`A5_jvm_memory_gc/`](./A5_jvm_memory_gc), [`E6_data_structures_storage_engines/`](./E6_data_structures_storage_engines) (발표자료는 `slides/`)
