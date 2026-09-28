import java.util.*;

/**
 * 데모 3: 메모리 누수 → OOM → 힙덤프.
 * 요청마다 static 리스트에 데이터를 넣고 절대 지우지 않는다.
 * GC Root(static 필드) → ArrayList → Object[] → byte[] 로 계속 도달 가능하므로 회수되지 않는다.
 */
public class LeakDemo {
    static final List<byte[]> LEAK = new ArrayList<>();   // 누수의 원인

    static void handleRequest(int n) {
        byte[] temp = new byte[20 * 1024];      // 정상: 요청이 끝나면 회수됨
        LEAK.add(new byte[50 * 1024]);          // 누수: 요청마다 50KB씩 영원히 쌓임
    }

    public static void main(String[] args) throws Exception {
        int n = 0;
        while (true) {
            handleRequest(n++);
            if (n % 200 == 0) {
                System.out.printf("requests=%d, leaked=%dMB%n", n, LEAK.size() * 50 / 1024);
                Thread.sleep(20);
            }
        }
    }
}
