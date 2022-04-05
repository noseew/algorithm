package org.song.algorithm.base._03distrib.lb;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._02high.skiplist._01base.SkipListMapArray02OptLevel;
import org.song.algorithm.base._01datatype._02high.skiplist._01base.SkipListMapLinked03OptLevel;
import org.song.algorithm.base.utils.DispatchUtils;
import org.song.algorithm.base.utils.StopWatchUtils;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 负载均衡算法
 */
public class LbPerfTest {

    protected static List<AbstractLB.Task> tasks = Lists.newArrayList();

    static {
        int[] rate = {10, 20, 40, 60, 80};

        for (int i = 0; i < 100; i++) {
            AbstractLB.Task task = new AbstractLB.Task("task" + (i % 5), rate[i % 5]);
            tasks.add(task);
        }
    }
    
    /*
    不考虑并发
    Lb01RR1	                    5	0.01%
    Lb01RR2	                    3	0.01%
    Lb05Hash1	                5	0.01%
    Lb02Random1	                102	0.22%
    Lb03WRR2	                105	0.23%
    Lb05ConsistencyHash1	1065	2.28%
    Lb03WR1	                1864	4.00%
    Lb03WRR1	            3341	7.16%
    Lb04SmoothWRR1	        19783	42.41%
    Lb04SmoothWRR2	        19841	42.54%
     */
    @Test
    public void test01() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();

        StopWatchUtils.warnup(() -> {
            Lb01RR1 rr1 = new Lb01RR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = rr1.select(tasks);
            }
            Lb01RR2 rr2 = new Lb01RR2();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = rr2.select(tasks);
            }
            Lb02Random1 random1 = new Lb02Random1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = random1.select(tasks);
            }
            Lb03WR1 lbWr_1 = new Lb03WR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = lbWr_1.select(tasks);
            }
            Lb03WRR1 lbWrr_1 = new Lb03WRR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = lbWrr_1.select(tasks);
            }
            Lb03WRR2 wrr2 = new Lb03WRR2();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = wrr2.select(tasks);
            }
            Lb04SmoothWRR1 swrr1 = new Lb04SmoothWRR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = swrr1.select(tasks);
            }
            Lb04SmoothWRR2 swrr2 = new Lb04SmoothWRR2();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = swrr2.select(tasks);
            }
            Lb05Hash1 hash1 = new Lb05Hash1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = hash1.select(tasks, i);
            }
            Lb05ConsistencyHash1 chash1 = new Lb05ConsistencyHash1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = chash1.select(tasks, i);
            }
        });

        Runnable r1 = () -> StopWatchUtils.run(stopWatch, "Lb01RR1", () -> {
            Lb01RR1 rr1 = new Lb01RR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = rr1.select(tasks);
            }
        });
        Runnable r2 = () -> StopWatchUtils.run(stopWatch, "Lb01RR2", () -> {
            Lb01RR2 rr2 = new Lb01RR2();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = rr2.select(tasks);
            }
        });
        Runnable r3 = () -> StopWatchUtils.run(stopWatch, "Lb02Random1", () -> {
            Lb02Random1 random1 = new Lb02Random1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = random1.select(tasks);
            }
        });
        Runnable r4 = () -> StopWatchUtils.run(stopWatch, "Lb03WR1", () -> {
            Lb03WR1 lbWr_1 = new Lb03WR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = lbWr_1.select(tasks);
            }
        });
        Runnable r5 = () -> StopWatchUtils.run(stopWatch, "Lb03WRR1", () -> {
            Lb03WRR1 lbWrr_1 = new Lb03WRR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = lbWrr_1.select(tasks);
            }
        });
        Runnable r6 = () -> StopWatchUtils.run(stopWatch, "Lb03WRR2", () -> {
            Lb03WRR2 wrr2 = new Lb03WRR2();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = wrr2.select(tasks);
            }
        });
        Runnable r7 = () -> StopWatchUtils.run(stopWatch, "Lb04SmoothWRR1", () -> {
            Lb04SmoothWRR1 swrr1 = new Lb04SmoothWRR1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = swrr1.select(tasks);
            }
        });
        Runnable r8 = () -> StopWatchUtils.run(stopWatch, "Lb04SmoothWRR2", () -> {
            Lb04SmoothWRR2 swrr2 = new Lb04SmoothWRR2();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = swrr2.select(tasks);
            }
        });
        Runnable r9 = () -> StopWatchUtils.run(stopWatch, "Lb05Hash1", () -> {
            Lb05Hash1 hash1 = new Lb05Hash1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = hash1.select(tasks, i);
            }
        });
        Runnable r10 = () -> StopWatchUtils.run(stopWatch, "Lb05ConsistencyHash1", () -> {
            Lb05ConsistencyHash1 chash1 = new Lb05ConsistencyHash1();
            for (int i = 0; i < num; i++) {
                AbstractLB.Task task = chash1.select(tasks, i);
            }
        });
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                r1.run();
                r2.run();
                r3.run();
                r4.run();
                r5.run();
                r6.run();
                r7.run();
                r8.run();
                r9.run();
                r10.run();
            } else {
                r10.run();
                r9.run();
                r8.run();
                r7.run();
                r6.run();
                r5.run();
                r4.run();
                r3.run();
                r2.run();
                r1.run();
            }
        }

//        System.out.println(stopWatch.prettyPrint());
        System.out.println(StopWatchUtils.calculate(stopWatch));
    }

}
