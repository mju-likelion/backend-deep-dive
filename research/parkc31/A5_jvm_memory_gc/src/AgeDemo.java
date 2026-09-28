import java.util.Random;

/**
 * 데모 1 보조: 수명이 제각각인 객체를 만들어 Survivor 나이 분포를 관찰한다.
 * - temp: 만들자마자 버려지는 객체 (대부분)
 * - ring: 무작위 슬롯을 덮어쓰므로 수명이 기하분포를 따름 → 오래 살수록 수가 줄어든다
 */
public class AgeDemo {
    static volatile Object SINK;   // temp가 밖으로 나가게 해서 JIT 탈출 분석이 할당을 없애지 못하게 함
    public static void main(String[] args) {
        int seconds = Integer.parseInt(args[0]);
        int slots = Integer.parseInt(args[1]);
        byte[][] ring = new byte[slots][];
        Random rnd = new Random(7);
        long sink = 0, end = System.currentTimeMillis() + seconds * 1000L;
        while (System.currentTimeMillis() < end) {
            for (int i = 0; i < 20; i++) { byte[] temp = new byte[256]; SINK = temp; sink += temp.length; }
            ring[rnd.nextInt(slots)] = new byte[64];
        }
        System.out.println(sink);
    }
}
