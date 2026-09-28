import java.util.*;

/**
 * 데모 1·2: 짧게 사는 객체(요청 처리 중 생성)와 오래 사는 객체(캐시)를 섞어 할당한다.
 * - 짧게 사는 객체: 대부분 Young GC에서 바로 사라진다 (약한 세대 가설)
 * - 캐시: Survivor를 거쳐 Old로 승격되고, 교체되면서 Old에 쓰레기가 쌓인다 → G1 동시 마킹, Mixed GC 유발
 */
public class AllocationChurn {
    static final Map<Integer, byte[]> CACHE = new HashMap<>();   // 오래 사는 객체 (GC Root: static)
    static long sink;

    public static void main(String[] args) throws Exception {
        int seconds = args.length > 0 ? Integer.parseInt(args[0]) : 20;
        int cacheEntries = args.length > 1 ? Integer.parseInt(args[1]) : 1100; // 약 110MB 유지
        Random rnd = new Random(42);
        long end = System.currentTimeMillis() + seconds * 1000L;
        long loops = 0;
        while (System.currentTimeMillis() < end) {
            // 1) 요청 하나 처리하는 흉내: 짧게 사는 객체 다수
            for (int i = 0; i < 200; i++) {
                byte[] temp = new byte[512 + rnd.nextInt(1024)];
                sink += temp.length;
            }
            // 2) 캐시에 100KB 항목을 넣거나 교체 → 교체된 옛 값은 Old의 쓰레기가 됨
            CACHE.put(rnd.nextInt(cacheEntries), new byte[100 * 1024]);
            loops++;
            if (loops % 2000 == 0) Thread.sleep(1);
        }
        System.out.println("loops=" + loops + ", cacheSize=" + CACHE.size() + ", sink=" + sink);
    }
}
