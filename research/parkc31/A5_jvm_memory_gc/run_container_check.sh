#!/usr/bin/env bash
# [A-실제] Docker 컨테이너 메모리/CPU 제한별 JVM 기본값 (Docker 필요). MaxRAM 흉내와 달리 GC 선택의 메모리 조건까지 실제로 반영된다.
for cpus in 1 2; do for m in 512m 1g 2g 4g; do
  echo "cpus=$cpus mem=$m: $(docker run --rm --cpus=$cpus --memory=$m eclipse-temurin:21 java -XX:+PrintFlagsFinal -version 2>/dev/null \
    | awk '/ MaxHeapSize /{printf "heap=%dMB ", $4/1048576} / Use(Serial|G1|Parallel)GC /&&$4=="true"{print $2}')"
done; done
# [C-실제] OOMKilled 재현: 힙을 90%로 잡고 힙 밖 메모리(스레드+다이렉트 버퍼)를 늘리면 예외 없이 137로 종료
# docker run --rm --memory=512m -v "$PWD/out:/app" eclipse-temurin:21 \
#   java -XX:MaxRAMPercentage=90 -XX:MaxDirectMemorySize=1g -cp /app NativeMemoryDemo 300 300; echo "exit=$?"
