package org.song.algorithm.algorithmbase.datatype;

import org.junit.Test;
import org.song.algorithm.algorithmbase.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class HashMap_test {

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
}

