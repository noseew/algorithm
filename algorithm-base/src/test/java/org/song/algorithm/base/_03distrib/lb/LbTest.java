package org.song.algorithm.base._03distrib.lb;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.song.algorithm.base.utils.DispatchUtils;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.IntStream;

/**
 * 负载均衡算法
 */
public class LbTest {

    protected static List<AbstractLB.Task> tasks = Lists.newArrayList(new AbstractLB.Task("task1", 40), new AbstractLB.Task("task2", 20));

    @Test
    public void test_RR1() {
        Lb01RR1 rr1 = new Lb01RR1();
        DispatchUtils instance = DispatchUtils.getInstance();
        IntStream.range(1, 100).forEach(e -> {
            AbstractLB.Task task = rr1.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        });
        System.out.println(instance.toPrettyString());
    }

    @Test
    public void test_RR2() {
        Lb01RR1 rr1 = new Lb01RR1();
        Lb01RR2 rr2 = new Lb01RR2();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("rr1");
        IntStream.range(1, 100).forEach(e -> {
            rr1.select(tasks).invoke(e);
        });
        stopWatch.stop();
        stopWatch.start("rr2");
        IntStream.range(1, 100).forEach(e -> {
            rr2.select(tasks).invoke(e);
        });
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    public void test_Random() {
        Lb02Random1 random1 = new Lb02Random1();
        IntStream.range(1, 50).forEach(e -> {
            random1.select(tasks).invoke(e);
        });
    }

    @Test
    public void test_WR() {
        Lb03WR1 lbWr_1 = new Lb03WR1();
        IntStream.range(1, 50).forEach(e -> {
            lbWr_1.select(tasks).invoke(e);
        });
    }

    @Test
    public void test_WRR1() {
        Lb03WRR1 lbWrr_1 = new Lb03WRR1();
        DispatchUtils instance = DispatchUtils.getInstance();
        tasks.add(new AbstractLB.Task("task3", 30));
        IntStream.range(1, 500).forEach(e -> {
            AbstractLB.Task task = lbWrr_1.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        });
        System.out.println(instance.toPrettyString());
    }

    @Test
    public void test_WRR2() {
        Lb03WRR2 lb = new Lb03WRR2();
        DispatchUtils instance = DispatchUtils.getInstance();
        tasks.add(new AbstractLB.Task("task3", 30));
        for (int e : IntStream.range(1, 500).toArray()) {
            AbstractLB.Task task = lb.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        }
        System.out.println(instance.toPrettyString());
    }

    @Test
    public void test_SWRR() {
        Lb04SmoothWRR1 lb = new Lb04SmoothWRR1();
        DispatchUtils instance = DispatchUtils.getInstance();
        tasks.add(new AbstractLB.Task("task3", 30));
        for (int e : IntStream.range(1, 50).toArray()) {
            AbstractLB.Task task = lb.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        }
        System.out.println(instance.toPrettyString());
    }

    @Test
    public void test_SWRR2() {
        Lb04SmoothWRR2 lb = new Lb04SmoothWRR2();
        DispatchUtils instance = DispatchUtils.getInstance();
        tasks.add(new AbstractLB.Task("task3", 30));
        for (int e : IntStream.range(1, 50).toArray()) {
            AbstractLB.Task task = lb.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        }
        System.out.println(instance.toPrettyString());
    }

    @Test
    public void test_hash() {
        Lb05Hash1 lbHash_1 = new Lb05Hash1();
        for (int e : IntStream.range(1, 5).toArray()) {
            lbHash_1.select(tasks, e).invoke(e);
        }
        System.out.println("********************************");
        for (int e : IntStream.range(1, 5).toArray()) {
            lbHash_1.select(tasks, e).invoke(e);
        }
        System.out.println("********************************");
        for (int e : IntStream.range(1, 5).toArray()) {
            lbHash_1.select(tasks, e).invoke(e);
        }
    }

    @Test
    public void test_consistencyHash() {
        Lb05ConsistencyHash1 loadBalance = new Lb05ConsistencyHash1();
        System.out.println("*********************1**********************");
        for (int e : IntStream.range(1, 10).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
//        tasks.add(new Task("task3", 0));
        tasks.add(new AbstractLB.Task("task4", 0));
        System.out.println("*********************2**********************");
        for (int e : IntStream.range(1, 10).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
//        tasks.removeIf(t -> t.getName().equals("task3"));
        tasks.removeIf(t -> t.getName().equals("task4"));
        System.out.println("*********************3**********************");
        for (int e : IntStream.range(1, 10).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
    }

}
