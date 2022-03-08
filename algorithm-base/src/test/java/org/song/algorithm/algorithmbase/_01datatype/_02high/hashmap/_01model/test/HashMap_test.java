package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.*;
import org.song.algorithm.algorithmbase.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class HashMap_test {

    private int maxVal = 50_0000;
    private int maxSize = 5000;

    private Random r = new Random();

    @Test
    public void testHash() {
        // 相等
        System.out.println(System.identityHashCode(1));
        System.out.println(System.identityHashCode(1));
        // 不相等
        System.out.println(System.identityHashCode(129));
        System.out.println(System.identityHashCode(129));
    }

    @Test
    public void test_01_start() {
        HashMap_base_01<String, String> map = new HashMap_base_01<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        map.put("5", "5");
        map.put("6", "6");
        map.put("7", "7");
        System.out.println(map.get("1"));
        System.out.println(map.remove("2"));
        System.out.println();
    }

    @Test
    public void test_01_start2() {
        
        /*
        System.identityHashCode() 函数有坑
        
         */
        for (int j = 0; j < 10000; j++) {
//            HashMap_base_01<Integer, Integer> map1 = new HashMap_base_01<>();
//            HashMap_base_02<Integer, Integer> map1 = new HashMap_base_02<>();
//            HashMap_base_03<Integer, Integer> map1 = new HashMap_base_03<>();
            HashMap_base_04<Integer, Integer> map1 = new HashMap_base_04<>();
//            HashMap_clash_01<Integer, Integer> map1 = new HashMap_clash_01<>();
            for (int i = 0; i < maxSize; i++) {
                int k = r.nextInt(maxVal);
                int v = r.nextInt(maxVal);
                map1.put(k, v);
                Integer v1 = map1.get(k);
                boolean eq = eq(v, v1);
                if (!eq) {
                    map1.get(k);
                    map1.put(k, v);
                    assert eq;
                }
            }

            System.out.println();

            for (int i = 0; i < maxSize; i++) {
                int k = r.nextInt(maxVal / 2);
                map1.remove(k);
            }
            
        }
    }

    @Test
    public void test_01_start_03() {
        HashMap_base_03<String, String> map = new HashMap_base_03<>();
        map.put("1abc", "1");
        map.put("2bcd", "2");
        map.put("3cde", "3");
        map.put("4def", "4");
        map.put("5efg", "5");
        map.put("6fgh", "6");
        map.put("7ghi", "7");
        map.put("8hij", "8");
        System.out.println(map.get("5efg"));
    }

    @Test
    public void hash_test() {
        System.out.println(System.identityHashCode("1"));
        System.out.println("1".hashCode());
    }

    @Test
    public void test_auto01() {
        HashMap<Integer, Integer> map0 = new HashMap<>();
        HashMap_base_01<Integer, Integer> map1 = new HashMap_base_01<>();
        HashMap_base_02<Integer, Integer> map2 = new HashMap_base_02<>();
        HashMap_base_03<Integer, Integer> map3 = new HashMap_base_03<>();
        HashMap_base_04<Integer, Integer> map4 = new HashMap_base_04<>();
//        HashMap_clash_01<Integer, Integer> map5 = new HashMap_clash_01<>();

        for (int i = 0; i < maxSize; i++) {
            int k = r.nextInt(maxVal);
            int v = r.nextInt(maxVal);

            map0.put(k, v);
            map1.put(k, v);
            map2.put(k, v);
            map3.put(k, v);
            map4.put(k, v);
//            map5.put(k, v);
        }

        for (int i = 0; i < maxSize; i++) {
            Integer v0 = map0.get(i);
            Integer v1 = map1.get(i);
            Integer v2 = map2.get(i);
            Integer v3 = map3.get(i);
            Integer v4 = map4.get(i);
//            Integer v5 = map5.get(i);
            assert eq(v0, v1);
            assert eq(v0, v2);
            assert eq(v0, v3);
            assert eq(v0, v4);
//            assert eq(v0, v5);
        }

        for (int i = 0; i < maxSize; i++) {
            int v = r.nextInt(maxVal / 2);
            map0.remove(v);
            map1.remove(v);
            map2.remove(v);
            map3.remove(v);
            map4.remove(v);
//            map5.remove(v);
        }

        for (int i = 0; i < maxSize; i++) {
            Integer v0 = map0.get(i);
            Integer v1 = map1.get(i);
            Integer v2 = map2.get(i);
            Integer v3 = map3.get(i);
            Integer v4 = map4.get(i);
//            Integer v5 = map5.get(i);
            assert eq(v0, v1);
            assert eq(v0, v2);
            assert eq(v0, v3);
            assert eq(v0, v4);
//            assert eq(v0, v5);
        }
    }

    /**
     * -Xint
     * 效率接近
     */
    @Test
    public void test_02_perf_put_01() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            HashMap_base_01<String, String> map = new HashMap_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_01", () -> {
            HashMap_base_01<String, String> map = new HashMap_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_02", () -> {
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_02", () -> {
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_01", () -> {
            HashMap_base_01<String, String> map = new HashMap_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * -Xint
     * 优化后的尾插法 效率比 头插法 高约 10%
     * 其中 不用重新计算 索引 约 5%
     */
    @Test
    public void test_02_perf_put_02() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_02", () -> {
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_02", () -> {
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * -Xint
     * 效率接近, 排除扩容
     */
    @Test
    public void test_02_perf_proto_put_01() {
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("HashMap_base_02");
        HashMap_base_02<String, String> map2 = new HashMap_base_02<>(num);
        for (int i = 0; i < num; i++) {
            map2.put(UUID.randomUUID().toString(), "");
        }
        stopWatch.stop();

        stopWatch.start("HashMap");
        HashMap<String, String> map3 = new HashMap<>(num);
        for (int i = 0; i < num; i++) {
            map3.put(UUID.randomUUID().toString(), "");
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    /**
     * -Xint
     * HashMap 快约 15%, 主要差扩容上
     * put + 扩容
     */
    @Test
    public void test_02_perf_proto_put_02() {
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("HashMap");
        HashMap<String, String> map3 = new HashMap<>();
        for (int i = 0; i < num; i++) {
            map3.put(UUID.randomUUID().toString(), "");
        }
        stopWatch.stop();

        stopWatch.start("HashMap_base_02");
        HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
        for (int i = 0; i < num; i++) {
            map2.put(UUID.randomUUID().toString(), "");
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

    private static boolean eq(Object o1, Object o2) {
        if (o1 == null  && o2 == null) {
            return true;
        }
        return Objects.equals(o1, o2);
    }
}

