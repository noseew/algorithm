package org.song.algorithm.base._01datatype._02high.skiplist._01base.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model.Tree05RB01;
import org.song.algorithm.base._01datatype._02high.skiplist._01base.SkipListMapArray02OptLevel;
import org.song.algorithm.base._01datatype._02high.skiplist._01base.SkipListMapLinked03OptLevel;
import org.song.algorithm.base._01datatype._02high.skiplist._01base.SkipListMapLinkedFromJDK;
import org.song.algorithm.base.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.Comparator;
import java.util.Random;

public class SkipListMap_perf_Test {

    private Random r = new Random();

    /**
     * 简单版跳表 索引实现不同的对比, 数组 VS 链表
     * <p>
     * 链表速度更快
     */
    @Test
    public void test_perf_skipLink_vs_skipArray() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListMapLinked03OptLevel<Integer, Integer> skip1 = new SkipListMapLinked03OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0);
            }
            SkipListMapArray02OptLevel<Integer, Integer> skip3 = new SkipListMapArray02OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip3.put(key, 0);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListMapLinked02OptLevel", () -> {
            SkipListMapLinked03OptLevel<Integer, Integer> skip1 = new SkipListMapLinked03OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "SkipListMapArray02OptLevel", () -> {
            SkipListMapArray02OptLevel<Integer, Integer> skip3 = new SkipListMapArray02OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip3.put(key, 0);
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
     * 各种数据结构实现的 全对比
     *
     * SkipListMapLinked02OptLevel	1686	16.64%
     * SkipListMapArray02OptLevel	2023	19.96%
     * Tree05RB01	                2330	22.99%
     * SkipListMapLinkedFromJDK	    3991	39.38%
     */
    @Test
    public void test_perf_vs_all() {
        int maxVal = 1000_0000;
        int num = 5_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListMapArray02OptLevel<Integer, Integer> skip1 = new SkipListMapArray02OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0);
            }
            SkipListMapLinked03OptLevel<Integer, Integer> skip2 = new SkipListMapLinked03OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0);
            }
            SkipListMapLinkedFromJDK<Integer, Integer> jdk2 = new SkipListMapLinkedFromJDK<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                jdk2.put(key, 0);
            }
            Tree05RB01<Integer> rbTree1 = new Tree05RB01<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                rbTree1.add(key);
            }
        });

        Runnable r0 = () -> StopWatchUtils.run(stopWatch, "Tree05RB01", () -> {
            Tree05RB01<Integer> rbTree1 = new Tree05RB01<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                rbTree1.add(key);
            }
        });
        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListMapArray02OptLevel", () -> {
            SkipListMapArray02OptLevel<Integer, Integer> skip1 = new SkipListMapArray02OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "SkipListMapLinked02OptLevel", () -> {
            SkipListMapLinked03OptLevel<Integer, Integer> skip2 = new SkipListMapLinked03OptLevel<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0);
            }
        });
        Runnable r4 = () -> StopWatchUtils.run(stopWatch, "SkipListMapLinkedFromJDK", () -> {
            SkipListMapLinkedFromJDK<Integer, Integer> jdk2 = new SkipListMapLinkedFromJDK<>(Comparator.comparing(Integer::doubleValue));
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                jdk2.put(key, 0);
            }
        });
        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                r0.run();
                r1.run();
                r2.run();
                r4.run();
            } else {
                r4.run();
                r2.run();
                r1.run();
                r0.run();
            }
        }

        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));

    }
}
