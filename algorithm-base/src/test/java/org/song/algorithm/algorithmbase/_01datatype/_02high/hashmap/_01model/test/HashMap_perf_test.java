package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.*;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap.redis.Dict_base_01;
import org.song.algorithm.algorithmbase.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.UUID;

public class HashMap_perf_test {


    /**
     * 一次性hash VS 渐进式hash 不严谨测试  put
     * 渐进式rehash效率高约 5%
     */
    @Test
    public void test_perf_put_01() {
        int num = 50_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
            Dict_base_01<String, String> map2 = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });


        StopWatchUtils.run(stopWatch, "Dict_base_01", () -> {
            Dict_base_01<String, String> map = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "Dict_base_01", () -> {
            Dict_base_01<String, String> map = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }
    @Test
    public void test_perf_putGet_01() {
        int num = 50_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
            Dict_base_01<String, String> map2 = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });


        StopWatchUtils.run(stopWatch, "Dict_base_01", () -> {
            Dict_base_01<String, String> map = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "Dict_base_01", () -> {
            Dict_base_01<String, String> map = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * 链地址法 VS 二次探测法 不严谨测试  put
     * 二次探测法效率高约 2%
     */
    @Test
    public void test_perf_put_02() {
        int num = 5_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
            HashMap_openAddressing_02<String, String> map2 = new HashMap_openAddressing_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });



        StopWatchUtils.run(stopWatch, "HashMap_openAddressing_02", () -> {
            HashMap_openAddressing_02<String, String> map = new HashMap_openAddressing_02<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_openAddressing_02", () -> {
            HashMap_openAddressing_02<String, String> map = new HashMap_openAddressing_02<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
        });

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * % VS 位运算,   put
     */
    @Test
    public void test_perf_put_03() {
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
     *  put
     * 优化后的尾插法 效率比 头插法 高约 10%
     * 其中 不用重新计算 索引 约 5%
     */
    @Test
    public void test_perf_put_04() {
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
     * 树化 VS 非树化 put
     * 这里似乎树化效率并没有提高
     */
    @Test
    public void test_perf_put_05() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(UUID.randomUUID().toString(), "");
            }
            HashMap_base_04<String, String> map2 = new HashMap_base_04<>();
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

        StopWatchUtils.run(stopWatch, "HashMap_base_04", () -> {
            HashMap_base_04<String, String> map2 = new HashMap_base_04<>();
            for (int i = 0; i < num; i++) {
                map2.put(UUID.randomUUID().toString(), "");
            }
        });

        StopWatchUtils.run(stopWatch, "HashMap_base_04", () -> {
            HashMap_base_04<String, String> map2 = new HashMap_base_04<>();
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
     */
    @Test
    public void test_02_perf() {
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
