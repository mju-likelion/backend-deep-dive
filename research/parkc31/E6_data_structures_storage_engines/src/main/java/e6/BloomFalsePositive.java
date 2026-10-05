package e6;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 설정한 거짓 양성률과 실측 거짓 양성률 비교, 그리고 설계 용량을 넘겨 넣었을 때의 변화.
 */
public class BloomFalsePositive {

    static final int N = 1_000_000;
    static final int PROBES = 1_000_000;

    public static void main(String[] args) throws IOException {
        System.out.printf("%-10s %-8s %-12s %-14s %-12s %-12s%n",
                "설정 fpp", "적재 배수", "넣은 원소", "실측 FP율", "필터 크기", "원소당 비트");
        for (double fpp : new double[]{0.01, 0.001}) {
            for (int load : new int[]{1, 2, 5}) {
                run(fpp, load);
            }
        }
    }

    static void run(double fpp, int load) throws IOException {
        BloomFilter<Long> f = BloomFilter.create(Funnels.longFunnel(), N, fpp);
        long inserted = (long) N * load;
        for (long i = 0; i < inserted; i++) f.put(i);

        // 거짓 음성이 없는지 확인
        for (long i = 0; i < inserted; i += 997) {
            if (!f.mightContain(i)) throw new AssertionError("false negative: " + i);
        }

        long fp = 0;
        for (long i = 0; i < PROBES; i++) {
            if (f.mightContain(10_000_000_000L + i)) fp++;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        f.writeTo(out);
        double bitsPerElement = out.size() * 8.0 / N;

        System.out.printf("%-10s %-8s %-12d %-14s %-12s %-12.2f%n",
                fpp, load + "x", inserted,
                String.format("%.4f%%", fp * 100.0 / PROBES),
                String.format("%.2fMB", out.size() / 1024.0 / 1024.0),
                bitsPerElement);
    }
}
