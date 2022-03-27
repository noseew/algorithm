package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05RB01;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base.SkipListMapArray;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_redis.SkipListLinked01;
import org.song.algorithm.algorithmbase.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.Comparator;
import java.util.Random;

public class SkipListMap_Array_perf_Test {

    private Random r = new Random();

    /**
     * Zset版本链表 VS 简单版本跳表map
     * 简单版本跳表map 效率高2倍
     */
    @Test
    public void test_perf_skip3_vs_skip1() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListLinked01<Integer, Integer> skip1 = new SkipListLinked01<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
            SkipListMapArray<Integer, Integer> skip2 = new SkipListMapArray<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListLinked01", () -> {
            SkipListLinked01<Integer, Integer> skip1 = new SkipListLinked01<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "SkipListMap", () -> {
            SkipListMapArray<Integer, Integer> skip2 = new SkipListMapArray<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0);
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
     * 跳表map VS 红黑树 相差太大, 红黑树高3倍 代码有问题?
     */
    @Test
    public void test_perf_rbtree_vs_skip1() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListMapArray<Integer, Integer> skip1 = new SkipListMapArray<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0);
            }
            Tree05RB01<Integer> rbTree1 = new Tree05RB01<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                rbTree1.add(key);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListMap", () -> {
            SkipListMapArray<Integer, Integer> skip1 = new SkipListMapArray<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "Tree05RB01", () -> {
            Tree05RB01<Integer> rbTree1 = new Tree05RB01<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                rbTree1.add(key);
            }
        });
        for (int i = 0; i < 50; i++) {
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
