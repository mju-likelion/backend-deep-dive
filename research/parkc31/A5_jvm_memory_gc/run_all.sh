#!/usr/bin/env bash
# A5 JVM 메모리와 GC 데모 전체 실행 (JDK 21 필요). 결과는 results/ 에 저장된다.
set -e
cd "$(dirname "$0")"; mkdir -p out results
javac -d out src/*.java

echo "[A] 메모리·CPU 크기별 기본 힙과 GC (MaxRAM으로 메모리 흉내, ActiveProcessorCount로 CPU 흉내)"
for cpu in 1 2; do for m in 512m 1g 2g 4g; do
  echo "CPU $cpu, MEM $m: $(java -XX:ActiveProcessorCount=$cpu -XX:MaxRAM=$m -XX:+PrintFlagsFinal -version 2>/dev/null \
    | awk '/ MaxHeapSize /{printf "heap=%dMB ", $4/1048576} / Use(Serial|G1|Parallel)GC /&&$4=="true"{print $2}')"
done; done | tee results/A_ergonomics.txt

echo "[B] 약한 세대 가설: Survivor 나이 분포 (Serial, Young 고정)"
java -XX:+UseSerialGC -Xmx256m -Xmn96m -XX:SurvivorRatio=2 -Xlog:gc+age=trace,gc:file=results/B_age.log -cp out AgeDemo 8 60000 >/dev/null

echo "[C] G1 사이클 + safepoint (정상: 힙 여유 / 빡빡: Full GC 유발)"
java -XX:+UseG1GC -Xmx512m -Xlog:gc,safepoint:file=results/C_g1_healthy.log:uptime,level,tags -cp out AllocationChurn 16 500 >/dev/null
java -XX:+UseG1GC -Xmx256m -Xlog:gc,safepoint:file=results/C_g1_tight.log:uptime,level,tags -cp out AllocationChurn 20 1100 >/dev/null

echo "[D] 누수 → OOM → 힙덤프 (results/D_leak.hprof 를 MAT로 열어 Dominator Tree → Path to GC Roots 확인)"
java -XX:+UseG1GC -Xmx128m -Xlog:gc:file=results/D_leak_gc.log -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=results/D_leak.hprof -cp out LeakDemo > results/D_leak_stdout.txt 2>&1 || true
python3 src/hprof_chain.py results/D_leak.hprof | tee results/D_hprof_chain.txt

echo "[E] 힙 밖 메모리 (NMT): 힙 64MB, 스레드 200개, 다이렉트 버퍼 100MB"
java -XX:+UseG1GC -Xmx64m -XX:MaxDirectMemorySize=256m -XX:NativeMemoryTracking=summary -cp out NativeMemoryDemo 200 100 > /tmp/e.out 2>&1 &
sleep 6; PID=$(jcmd | awk '/NativeMemoryDemo/{print $1}')
jcmd $PID VM.native_memory summary scale=MB > results/E_nmt_summary.txt
echo "RSS: $(( $(ps -o rss= -p $PID) / 1024 ))MB" | tee results/E_rss.txt; kill $PID   # ps는 macOS/Linux 공통

echo "[F] 다이렉트 버퍼와 DisableExplicitGC"
for opt in "" "-XX:+DisableExplicitGC"; do echo "== ${opt:-기본}"
  java -XX:+UseG1GC -Xmx1g -XX:MaxDirectMemorySize=64m $opt -cp out DirectBufferDemo 2>&1 | grep -E "DONE|OutOfMemoryError"; done | tee results/F_direct.txt
