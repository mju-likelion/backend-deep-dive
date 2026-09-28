import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 데모 4: 힙 밖 메모리. 힙은 작게 두고 스레드 스택과 다이렉트 버퍼로 네이티브 메모리를 쓴다.
 */
public class NativeMemoryDemo {
    static final List<ByteBuffer> DIRECT = new ArrayList<>();

    static int depth(int n) { return n == 0 ? 0 : 1 + depth(n - 1); }   // 스택을 조금 실제로 사용

    public static void main(String[] args) throws Exception {
        int threads = Integer.parseInt(args[0]);
        int directMb = Integer.parseInt(args[1]);
        CountDownLatch hold = new CountDownLatch(1);
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(() -> { depth(2000); try { hold.await(); } catch (InterruptedException ignored) {} });
            t.setDaemon(true);
            t.start();
        }
        for (int i = 0; i < directMb; i++) {
            ByteBuffer b = ByteBuffer.allocateDirect(1024 * 1024);
            for (int p = 0; p < b.capacity(); p += 4096) b.put(p, (byte) 1);   // 실제로 메모리를 건드려 RSS에 반영
            DIRECT.add(b);
        }
        System.out.println("READY threads=" + threads + " directMB=" + directMb);
        Thread.sleep(Long.MAX_VALUE);
    }
}
