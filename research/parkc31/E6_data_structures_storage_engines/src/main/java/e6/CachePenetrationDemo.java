package e6;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.Arrays;
import java.util.Random;

/**
 * 캐시 관통 재현. Redis 캐시 + PostgreSQL 구성에 존재하지 않는 키 요청을 대량으로 보낸다.
 *
 * 모드
 *  - none      : 캐시 어사이드만. 없는 키는 매번 DB까지 간다.
 *  - nullCache : 없는 키를 짧은 TTL로 캐싱한다.
 *  - bloom     : DB의 모든 id를 블룸 필터에 넣고, "확실히 없음"이면 DB에 가지 않는다.
 *
 * 워크로드: 요청의 90%는 무작위 없는 키, 10%는 무작위 있는 키.
 */
public class CachePenetrationDemo {

    static final int ROWS = 100_000;
    static final int REQUESTS = 20_000;
    static final double MISSING_RATIO = 0.9;

    static final String PG_URL = env("PG_URL", "jdbc:postgresql://localhost:55432/e6");
    static final String REDIS_HOST = env("REDIS_HOST", "localhost");
    static final int REDIS_PORT = Integer.parseInt(env("REDIS_PORT", "56379"));

    public static void main(String[] args) throws Exception {
        try (Connection db = DriverManager.getConnection(PG_URL, "e6", "e6");
             Jedis redis = new Jedis(REDIS_HOST, REDIS_PORT)) {
            seed(db);
            long[] keys = workload();

            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-10s %-10s%n",
                    "모드", "요청", "DB 조회", "찾은 행", "p50(µs)", "p99(µs)", "총(ms)");
            // JIT·커넥션·PG 버퍼 캐시 워밍업. 첫 모드만 불리해지지 않게 한 번 버린다.
            redis.flushAll();
            run("warmup", db, redis, keys);
            for (String mode : new String[]{"none", "nullCache", "bloom"}) {
                redis.flushAll();
                run(mode, db, redis, keys);
            }
        }
    }

    static void seed(Connection db) throws SQLException {
        try (Statement st = db.createStatement()) {
            st.execute("DROP TABLE IF EXISTS item");
            st.execute("CREATE TABLE item (id BIGINT PRIMARY KEY, payload TEXT)");
            st.execute("INSERT INTO item SELECT g, md5(g::text) FROM generate_series(1, " + ROWS + ") g");
            st.execute("ANALYZE item");
        }
    }

    static long[] workload() {
        Random r = new Random(7);
        long[] keys = new long[REQUESTS];
        for (int i = 0; i < REQUESTS; i++) {
            keys[i] = r.nextDouble() < MISSING_RATIO
                    ? 1_000_000L + r.nextInt(100_000_000)   // 없는 키, 거의 반복되지 않음
                    : 1 + r.nextInt(ROWS);                   // 있는 키
        }
        return keys;
    }

    static void run(String mode, Connection db, Jedis redis, long[] keys) throws SQLException {
        BloomFilter<Long> bloom = null;
        if (mode.equals("bloom")) {
            bloom = BloomFilter.create(Funnels.longFunnel(), ROWS, 0.01);
            try (Statement st = db.createStatement(); ResultSet rs = st.executeQuery("SELECT id FROM item")) {
                while (rs.next()) bloom.put(rs.getLong(1));
            }
        }

        long dbQueries = 0, found = 0;
        long[] lat = new long[keys.length];
        long start = System.nanoTime();
        try (PreparedStatement ps = db.prepareStatement("SELECT payload FROM item WHERE id = ?")) {
            for (int i = 0; i < keys.length; i++) {
                long t0 = System.nanoTime();
                long id = keys[i];
                String value;

                if (bloom != null && !bloom.mightContain(id)) {
                    value = null;
                } else {
                    String cached = redis.get("item:" + id);
                    if (cached != null) {
                        value = cached.isEmpty() ? null : cached;
                    } else {
                        dbQueries++;
                        ps.setLong(1, id);
                        try (ResultSet rs = ps.executeQuery()) {
                            value = rs.next() ? rs.getString(1) : null;
                        }
                        if (value != null) redis.setex("item:" + id, 300, value);
                        else if (mode.equals("nullCache")) redis.setex("item:" + id, 30, "");
                    }
                }

                if (value != null) found++;
                lat[i] = System.nanoTime() - t0;
            }
        }
        long totalMs = (System.nanoTime() - start) / 1_000_000;

        Arrays.sort(lat);
        System.out.printf("%-10s %-10d %-10d %-10d %-10d %-10d %-10d%n",
                mode, keys.length, dbQueries, found,
                lat[lat.length / 2] / 1000, lat[(int) (lat.length * 0.99)] / 1000, totalMs);
    }

    static String env(String k, String d) {
        String v = System.getenv(k);
        return v == null ? d : v;
    }
}
