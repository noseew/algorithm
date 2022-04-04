package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_redis.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._01model.Tree05RB01;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_redis.*;
import org.song.algorithm.algorithmbase.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.*;

public class SkipList_perf_Test {

    private Random r = new Random();

    /**
     * 链表 数组 相差太大, 代码有问题
     * 数组有问题
     */
    @Test
    public void test_perf_skip3_vs_skip1() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListLinked02<Integer, Integer> skip1 = new SkipListLinked02<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
            SkipListArray01<Integer, Integer> skip2 = new SkipListArray01<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0, key);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListLinked02", () -> {
            SkipListLinked02<Integer, Integer> skip1 = new SkipListLinked02<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "SkipListArray01", () -> {
            SkipListArray01<Integer, Integer> skip2 = new SkipListArray01<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0, key);
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
     * 优化更新
     * 效率提高约 < 1%
     */
    @Test
    public void test_perf_skip2_vs_skip1() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListLinked01<Integer, Integer> skip1 = new SkipListLinked01<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
            SkipListLinked02<Integer, Integer> skip2 = new SkipListLinked02<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0, key);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListBase01Linked", () -> {
            SkipListLinked01<Integer, Integer> skip1 = new SkipListLinked01<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "SkipListBase01Linked02", () -> {
            SkipListLinked02<Integer, Integer> skip2 = new SkipListLinked02<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0, key);
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

    /**
     * 链表 红黑树 相差太大, 红黑树高9倍 代码有问题?
     */
    @Test
    public void test_perf_rbtree_vs_skip1() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListLinked02<Integer, Integer> skip1 = new SkipListLinked02<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
            Tree05RB01<Integer> rbTree1 = new Tree05RB01<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                rbTree1.add(key);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListBase01Linked02", () -> {
            SkipListLinked02<Integer, Integer> skip1 = new SkipListLinked02<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
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
