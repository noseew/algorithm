package org.song.algorithm.base._01datatype._02high.hashmap._01base.test;

import org.junit.Test;
import org.song.algorithm.base._01datatype._02high.hashmap._01base.*;
import org.song.algorithm.base._01datatype._02high.hashmap._02redis.Dict_base_01;
import org.song.algorithm.base.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;

public class HashMap_perf_test {

    private Comparator<String> comparator = Comparator.comparing(e -> e);


    /**
     * 一次性hash VS 渐进式hash
     * 渐进式rehash效率高约 4%
     */
    @Test
    public void test_perf_put_01() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            Dict_base_01<String, String> map2 = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "Dict_base_01", () -> {
            Dict_base_01<String, String> map2 = new Dict_base_01<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * 链地址法 VS 二次探测法
     * 二次探测法效率高约 4%
     */
    @Test
    public void test_perf_link_vs_open() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            HashMap_openAddressing_02<String, String> map2 = new HashMap_openAddressing_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });


        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_03", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "HashMap_openAddressing_02", () -> {
            HashMap_openAddressing_02<String, String> map2 = new HashMap_openAddressing_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * % VS 位运算
     * 不严谨测试: 位运算效率高约4%
     */
    @Test
    public void test_perf_mod_vs_bit() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap_base_01<String, String> map = new HashMap_base_01<>();
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_01 %", () -> {
            HashMap_base_01<String, String> map2 = new HashMap_base_01<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_02 bit", () -> {
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * 普通尾插法 VS 优化后的尾插法
     * 优化后的尾插法 效率比 头插法 高约 8%
     * 其中 不用重新计算 索引 约 5%
     */
    @Test
    public void test_perf_tail_vs_proTail() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_02 tail", () -> {
            HashMap_base_02<String, String> map2 = new HashMap_base_02<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_03 优化tail", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * 红黑树 VS 链表
     * 不严谨测试: 红黑树 效率高约 4%
     */
    @Test
    public void test_perf_link_vs_RBRee() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap_base_03<String, String> map = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            HashMap_base_05<String, String> map2 = new HashMap_base_05<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_03 链表", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_05 红黑树", () -> {
            HashMap_base_05<String, String> map2 = new HashMap_base_05<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * 树化 AVL 对比 红黑树
     * 不严谨测试: 红黑树 效率高约 4%
     */
    @Test
    public void test_perf_AVL_vs_RBRee() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap_base_04<String, String> map = new HashMap_base_04<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            HashMap_base_05<String, String> map2 = new HashMap_base_05<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_04 AVL", () -> {
            HashMap_base_04<String, String> map2 = new HashMap_base_04<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_05 红黑树", () -> {
            HashMap_base_05<String, String> map2 = new HashMap_base_05<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * AVL 对比 链表
     * 不严谨测试: AVL 似乎更慢 慢约4%
     */
    @Test
    public void test_perf_AVL_vs_link() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap_base_04<String, String> map = new HashMap_base_04<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_04 AVL", () -> {
            HashMap_base_04<String, String> map2 = new HashMap_base_04<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_03 链表", () -> {
            HashMap_base_03<String, String> map2 = new HashMap_base_03<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

    /**
     * 自己实现的红黑树HashMap VS JDK HashMap
     * 不严谨测试: 自己实现的似乎更快, 差距 < 1%
     */
    @Test
    public void test_perf_HashMap_vs_05() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();
        Random r = new Random();

        StopWatchUtils.warnup(() -> {
            HashMap<String, String> map = new HashMap<>();
            for (int i = 0; i < num; i++) {
                map.put(String.valueOf(r.nextInt()), "");
            }
            HashMap_base_05<String, String> map2 = new HashMap_base_05<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "HashMap", () -> {
            HashMap<String, String> map2 = new HashMap<>();
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "HashMap_base_05", () -> {
            HashMap_base_05<String, String> map2 = new HashMap_base_05<>(comparator, 8);
            for (int i = 0; i < num; i++) {
                map2.put(String.valueOf(r.nextInt()), "");
            }
        });
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                r2.run();
                r1.run();
            } else {
                r1.run();
                r2.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }
    
    
}
