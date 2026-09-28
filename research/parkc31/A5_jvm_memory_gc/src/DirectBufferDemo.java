import java.nio.ByteBuffer;

/**
 * 데모 5: 다이렉트 버퍼는 힙의 ByteBuffer 객체가 GC될 때 해제된다.
 * 힙이 넉넉해 GC가 안 돌면 네이티브 메모리가 쌓이고, 한도에 닿으면 JDK가 System.gc()로 회수를 시도한다.
 * -XX:+DisableExplicitGC 를 켜면 그 경로가 막힌다.
 */
public class DirectBufferDemo {
    public static void main(String[] args) {
        for (int i = 1; i <= 2000; i++) {
            ByteBuffer b = ByteBuffer.allocateDirect(1024 * 1024);   // 1MB, 참조는 바로 버림
            b.put(0, (byte) i);
            if (i % 500 == 0) System.out.println("allocated " + i + "MB (cumulative)");
        }
        System.out.println("DONE: allocated 2000MB within a 64MB direct memory limit");
    }
}
