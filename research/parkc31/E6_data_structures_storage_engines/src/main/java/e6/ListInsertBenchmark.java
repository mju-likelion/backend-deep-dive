package e6;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 리스트 한가운데에 원소 하나를 넣고 다시 빼는 한 쌍의 비용.
 * 크기를 일정하게 유지하려고 삽입과 삭제를 묶어서 잰다.
 *
 * linkedScattered: 무작위 위치에 끼워 넣으며 만든 LinkedList.
 * 논리 순서와 메모리(할당) 순서가 어긋나 있어 실제 서비스에서 오래 산 리스트에 가깝다.
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(value = 1, jvmArgs = {"-Xms1g", "-Xmx1g"})
public class ListInsertBenchmark {

    @Param({"1000", "10000", "100000"})
    int size;

    ArrayList<Integer> array;
    LinkedList<Integer> linked;
    LinkedList<Integer> linkedScattered;
    ListIterator<Integer> cursor;
    final Integer x = 42;

    @Setup(Level.Trial)
    public void setup() {
        array = new ArrayList<>(size + 1);
        linked = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            array.add(i);
            linked.add(i);
        }

        Random r = new Random(1);
        linkedScattered = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            linkedScattered.add(r.nextInt(linkedScattered.size() + 1), i);
        }

        cursor = new LinkedList<>(linked).listIterator(size / 2);
    }

    @Benchmark
    public Integer arrayList_addRemoveMiddle() {
        array.add(size / 2, x);
        return array.remove(size / 2);
    }

    @Benchmark
    public Integer linkedList_addRemoveMiddle() {
        linked.add(size / 2, x);
        return linked.remove(size / 2);
    }

    @Benchmark
    public Integer linkedListScattered_addRemoveMiddle() {
        linkedScattered.add(size / 2, x);
        return linkedScattered.remove(size / 2);
    }

    /** 교과서의 O(1)이 성립하는 경우: 위치를 이미 쥐고 있다. */
    @Benchmark
    public Integer linkedList_iteratorAddRemove() {
        cursor.add(x);
        Integer v = cursor.previous();
        cursor.remove();
        return v;
    }
}
