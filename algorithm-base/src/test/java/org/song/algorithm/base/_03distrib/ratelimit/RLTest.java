package org.song.algorithm.base._03distrib.ratelimit;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base.utils.ThreadUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流算法
 * 窗口限流
 * - 固定窗口
 * - 滑动窗口
 */
public class RLTest {

    @Test
    public void test01() throws InterruptedException {
        /*
        总数:1000, 成功:11, 失败:989, 通过率:0.0110
         */
        RL01FixedWindow fixedWindow = new RL01FixedWindow(10, 1);

        TestReporter reporter = new TestReporter();

        int tryTimes = 100;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 2000);
                if (fixedWindow.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                } else {
                    reporter.fail.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(reporter);
    }

    @Test
    public void test02() throws InterruptedException {
        /*
        总数:1000, 成功:10, 失败:990, 通过率:0.0100
         */
        RL01FixedWindow02 window02 = new RL01FixedWindow02(10, 1);

        TestReporter reporter = new TestReporter();

        int tryTimes = 100;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 2000);
                if (window02.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                } else {
                    reporter.fail.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(reporter);
    }

    @Test
    public void test04() throws InterruptedException {
        /*
        总数:1000, 成功:10, 失败:990, 通过率:0.0100
         */
        RL02SlidingWindow RL02SlidingWindow = new RL02SlidingWindow(10, 1, 5);

        TestReporter reporter = new TestReporter();

        int tryTimes = 100;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 2000);
                if (RL02SlidingWindow.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                } else {
                    reporter.fail.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(reporter);
    }

    @Test
    public void test04_2() throws InterruptedException {
        /*
        总数:1000, 成功:10, 失败:990, 通过率:0.0100
         */
        RL02SlidingWindow02 RL02SlidingWindow = new RL02SlidingWindow02(10, 1, 5);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 5000);
                if (RL02SlidingWindow.get()) {
                    reporter.success.getAndIncrement();
//                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                } else {
                    reporter.fail.getAndIncrement();
//                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(reporter);
    }

    @Test
    public void test05() throws InterruptedException {
        /*
         */
        RL03LeakyBucket limit = new RL03LeakyBucket(2, 10);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (limit.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁 " + LocalDateTime.now());
                } else {
                    reporter.fail.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(reporter);
    }

    @Test
    public void test06() throws InterruptedException {
        /*
         */
        RL04TokenBucket limit = new RL04TokenBucket(2, 10);

        // 等待攒够一定量的令牌
        ThreadUtils.sleepRandom(TimeUnit.SECONDS, 5);
        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (limit.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁 " + LocalDateTime.now());
                } else {
                    reporter.fail.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(reporter);
    }

    @Test
    public void test07() throws InterruptedException {
        /*
         */
        RL04TokenBucket02 limit = new RL04TokenBucket02(2, 10);

        // 等待攒够一定量的令牌
        ThreadUtils.sleepRandom(TimeUnit.SECONDS, 5);
        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (limit.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁 " + LocalDateTime.now());
                } else {
                    reporter.fail.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(reporter);
    }

    static class TestReporter {
        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        @Override
        public String toString() {
            return String.format("总数:%s, 成功:%s, 失败:%s, 通过率:%s", success.get() + fail.get(), success.get(), fail.get(),
                    BigDecimal.valueOf(success.get())
                            .divide(BigDecimal.valueOf(success.get() + fail.get()), 4, BigDecimal.ROUND_HALF_UP));
        }
    }
}
