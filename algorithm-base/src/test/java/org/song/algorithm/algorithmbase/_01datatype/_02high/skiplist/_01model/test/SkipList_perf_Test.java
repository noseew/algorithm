package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05RB01;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_05;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.AbstractSkipList;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.SkipListBase01Linked;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.SkipListBase02LinkedRank;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model.SkipListBase03Array;
import org.song.algorithm.algorithmbase.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.*;

public class SkipList_perf_Test {

    private Random r = new Random();

    /**
     * 链表 红黑树 相差太大, 代码有问题
     */
    @Test
    public void test_perf_rbtree_vs_skip1() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListBase01Linked<Integer, Integer> skip1 = new SkipListBase01Linked<>();
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

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListBase01Linked", () -> {
            SkipListBase01Linked<Integer, Integer> skip1 = new SkipListBase01Linked<>();
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
     * 链表 数组 相差太大, 代码有问题
     */
    @Test
    public void test_perf_skip3_vs_skip1() {
        int maxVal = 100_0000;
        int num = 1_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            SkipListBase01Linked<Integer, Integer> skip1 = new SkipListBase01Linked<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
            SkipListBase03Array<Integer, Integer> skip2 = new SkipListBase03Array<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip2.put(key, 0, key);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "SkipListBase01Linked", () -> {
            SkipListBase01Linked<Integer, Integer> skip1 = new SkipListBase01Linked<>();
            for (int i = 0; i < num; i++) {
                int key = r.nextInt(maxVal);
                skip1.put(key, 0, key);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "SkipListBase03Array", () -> {
            SkipListBase03Array<Integer, Integer> skip2 = new SkipListBase03Array<>();
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
}
