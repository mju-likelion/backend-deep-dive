#!/usr/bin/env bash
# Docker만 있으면 된다. 빌드와 실행 모두 JDK 21 컨테이너 안에서 한다.
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p results

docker compose up -d
NET="$(basename "$PWD" | tr '[:upper:]' '[:lower:]')_default"
IMG=maven:3.9-eclipse-temurin-21
RUN=(docker run --rm --network "$NET" -v "$PWD":/w -v "$HOME/.m2":/root/.m2 -w /w "$IMG")

"${RUN[@]}" mvn -q -DskipTests package

# A. ArrayList vs LinkedList 중간 삽입
"${RUN[@]}" java -jar target/bench.jar ListInsertBenchmark -f 3 -wi 5 -i 10 -rf text -rff results/A_list_insert.txt | tee results/A_list_insert.log

# B-1. 블룸 필터 거짓 양성률
"${RUN[@]}" java -cp target/bench.jar e6.BloomFalsePositive | tee results/B1_bloom_fpp.txt

# B-2. 캐시 관통 방어
until docker compose exec -T postgres pg_isready -U e6 >/dev/null 2>&1; do sleep 1; done
docker run --rm --network "$NET" -v "$PWD":/w -w /w \
  -e PG_URL=jdbc:postgresql://postgres:5432/e6 -e REDIS_HOST=redis -e REDIS_PORT=6379 \
  "$IMG" java -cp target/bench.jar e6.CachePenetrationDemo | tee results/B2_cache_penetration.txt

{ echo "측정 시각: $(date)"; "${RUN[@]}" java -version 2>&1; docker info --format 'Docker VM: CPU {{.NCPU}}, 메모리 {{.MemTotal}} bytes'; sysctl -n machdep.cpu.brand_string 2>/dev/null || true; } > results/environment.txt

docker compose down
