package org.song.algorithm.algorithmbase._02case.ratelimit;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.ThreadUtils;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 限流算法
 * 窗口限流
 * - 固定窗口
 * - 滑动窗口
 */
public class RateLimit_01 {

    @Test
    public void test01() throws InterruptedException {
        /*
        总数:1000, 成功:11, 失败:989, 通过率:0.0110
         */
        RateLimitFixedWindow rateLimitFixedWindow = new RateLimitFixedWindow(10, 1);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (rateLimitFixedWindow.get()) {
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
        RateLimitFixedWindow02 rateLimitFixedWindow = new RateLimitFixedWindow02(10, 1);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (rateLimitFixedWindow.get()) {
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
    public void test03() throws InterruptedException {
        /*
        总数:1000, 成功:10, 失败:990, 通过率:0.0100
         */
        RateLimitFixedWindow03 rateLimitFixedWindow = new RateLimitFixedWindow03(10, 1);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (rateLimitFixedWindow.get()) {
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
        RateLimitSlidingWindow rateLimitSlidingWindow = new RateLimitSlidingWindow(10, 2, 5);

        TestReporter reporter = new TestReporter();
        
        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (rateLimitSlidingWindow.get()) {
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

    public static class TestReporter {
        AtomicInteger success = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();

        @Override
        public String toString() {
            return String.format("总数:%s, 成功:%s, 失败:%s, 通过率:%s", success.get() + fail.get(), success.get(), fail.get(),
                    BigDecimal.valueOf(success.get())
                            .divide(BigDecimal.valueOf(success.get() + fail.get()), 4, BigDecimal.ROUND_HALF_UP));
        }
    }

    /**
     * 固定窗口,
     * 采用 计数器 + 定时器
     */
    public static class RateLimitFixedWindow {

        private final AtomicInteger count = new AtomicInteger();
        private final int maxLimit;

        private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        public RateLimitFixedWindow(int maxLimit, int seconds) {
            this.maxLimit = maxLimit;
            service.scheduleAtFixedRate(() -> {
                count.set(0);
            }, seconds, seconds, TimeUnit.SECONDS);
        }

        /**
         * 是否通过
         *
         * @return
         */
        public boolean get() {
            return count.getAndIncrement() <= maxLimit;
        }
    }

    /**
     * 固定窗口,
     * 采用 计数器 + 时间戳分组的数组
     */
    public static class RateLimitFixedWindow02 {

        private AtomicInteger counter = new AtomicInteger();
        private int maxLimit;
        /**
         * 窗口大小, 单位秒
         */
        private int winSecond;
        /**
         * 上次窗口时间
         */
        private long lastTime = System.currentTimeMillis();

        RateLimitFixedWindow02(int maxLimit, int winSecond) {
            this.maxLimit = maxLimit;
            this.winSecond = winSecond;
        }

        /**
         * 是否通过
         *
         * @return
         */
        public boolean get() {
            long now = System.currentTimeMillis();
            /*
            如果在窗口之外, 则重置
             */
            if (((now - lastTime) / 1000) > winSecond) {
                lastTime = now;
                counter.set(1);
                return true;
            }
            // 如果在窗口之内, 则判断次数
            return counter.incrementAndGet() <= maxLimit;
        }
    }

    /**
     * 固定窗口, 采用滑动窗口模拟
     * 采用 计数器 + 时间戳分组的数组
     * 向滑动窗口迈进,
     */
    public static class RateLimitFixedWindow03 {

        private final AtomicInteger[] countWin;
        private final int maxLimit;

        /**
         * @param maxLimit 最大限流数量
         * @param seconds  单位秒, 必须 > 0
         */
        public RateLimitFixedWindow03(int maxLimit, int seconds) {
            this.maxLimit = maxLimit;
            seconds = seconds <= 0 ? 1 : seconds;
            /*
            限流最小窗口固定1s, 数组长度为限流单位秒+1, 多出的1是为了腾出空间为下一秒做准备, 同时只要到下一秒, 则最早的那一秒就失效了
            示例:
                假设限流 2s 10次
                数组为: |_0_|_1_|_2_|
                计算2s内的访问次数只需要连续的两个数组item, 比如 0和1, 1和2, 2和0, 那么剩下的那个需要清空, 并为下一秒做准备
             */
            countWin = new AtomicInteger[seconds + 1];
            for (int i = 0; i < countWin.length; i++) {
                countWin[i] = new AtomicInteger();
            }
        }

        /**
         * 是否通过
         *
         * @return
         */
        public boolean get() {
            // 当前秒, 当前秒的访问会落到指定位置的数组中
            int s = (int) (System.currentTimeMillis() / 1000);
            // 清空下一个窗口计数,
            countWin[(s + 1) % countWin.length].set(0);

            // 统计 seconds 范围内的所有计数,
            int last = s;
            long count = 0;
            for (int i = 0; i < countWin.length - 1; i++) {
                count += countWin[(last -= i) % countWin.length].get();
            }
            if (count >= maxLimit) {
                // 如果计数超了, 则返回限流
                return false;
            }
            // 当前窗口计数+1
            countWin[s % countWin.length].getAndIncrement();

            return true;
        }
    }

    /**
     * 滑动窗口, 
     * RateLimitFixedWindow03 的优化版本, 可以自定义窗口大小, 和窗口精度
     */
    public static class RateLimitSlidingWindow {
        /**
         * 窗口数量
         */
        private AtomicInteger[] countWin;
        /**
         * 限流窗口次数
         */
        private int maxLimit;

        /**
         * @param maxLimit 最大限流数量
         * @param seconds  单位限流窗口, 单位秒, 必须 > 0
         * @param winCount 单位窗口数量
         */
        public RateLimitSlidingWindow(int maxLimit, int seconds, int winCount) {
            // 转换成每秒限流大小
            this.maxLimit = maxLimit / seconds;
            // 转换成每秒窗口大小
            int secondsCount = winCount / seconds;
            /*
            将限流范围 
             */
            countWin = new AtomicInteger[secondsCount + 1];
            for (int i = 0; i < countWin.length; i++) {
                countWin[i] = new AtomicInteger();
            }
        }

        /**
         * 是否通过
         *
         * @return
         */
        public boolean get() {
            // 当前秒, 当前秒的访问会落到指定位置的数组中
            int s = (int) (System.currentTimeMillis() / 1000);
            // 清空下一个窗口计数,
            countWin[(s + 1) % countWin.length].set(0);

            // 统计 seconds 范围内的所有计数,
            int last = s;
            long count = 0;
            for (int i = 0; i < countWin.length - 1; i++) {
                count += countWin[(last -= i) % countWin.length].get();
            }
            if (count >= maxLimit) {
                // 如果计数超了, 则返回限流
                return false;
            }
            // 当前窗口计数+1
            countWin[s % countWin.length].getAndIncrement();

            return true;
        }
    }
}
