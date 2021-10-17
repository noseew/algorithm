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

    @Test
    public void test05() throws InterruptedException {
        /*
         */
        RateLimitLeakyBucket limit = new RateLimitLeakyBucket(10, 2);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (limit.get()) {
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

    /**
     * 漏桶算法
     */
    public static class RateLimitLeakyBucket {

        /**
         * 桶的容量
         */
        private int capacity;
        /**
         * 漏出速率
         */
        private int permitsPerSecond;
        /**
         * 剩余水量
         */
        private long leftWater;
        /**
         * 上次注入时间
         */
        private long timeStamp = System.currentTimeMillis();

        public RateLimitLeakyBucket(int permitsPerSecond, int capacity) {
            this.capacity = capacity;
            this.permitsPerSecond = permitsPerSecond;
        }

        public synchronized boolean get() {
            // 1. 计算剩余水量
            long now = System.currentTimeMillis();
            // 上次请求截止到目前的秒数
            long timeGap = (now - timeStamp) / 1000;
            leftWater = Math.max(0, leftWater - timeGap * permitsPerSecond);
            timeStamp = now;

            // 如果未满, 则放行, 否则限流
            if (leftWater < capacity) {
                leftWater += 1;
                return true;
            }
            return false;
        }
    }

    /**
     * 令牌桶
     */
    public static class RateLimitTokenBucket {
        /**
         * 令牌桶的容量「限流器允许的最大突发流量」
         */
        private final long capacity;
        /**
         * 令牌发放速率
         */
        private final long generatedPerSeconds;
        /**
         * 最后一个令牌发放的时间
         */
        long lastTokenTime = System.currentTimeMillis();
        /**
         * 当前令牌数量
         */
        private long currentTokens;

        public RateLimitTokenBucket(long generatedPerSeconds, int capacity) {
            this.generatedPerSeconds = generatedPerSeconds;
            this.capacity = capacity;
        }

        /**
         * 尝试获取令牌
         *
         * @return true表示获取到令牌，放行；否则为限流
         */
        public synchronized boolean get() {
            /**
             * 计算令牌当前数量
             * 请求时间在最后令牌是产生时间相差大于等于额1s（为啥时1s？因为生成令牌的最小时间单位时s），则
             * 1. 重新计算令牌桶中的令牌数
             * 2. 将最后一个令牌发放时间重置为当前时间
             */
            long now = System.currentTimeMillis();
            if (now - lastTokenTime >= 1000) {
                long newPermits = (now - lastTokenTime) / 1000 * generatedPerSeconds;
                currentTokens = Math.min(currentTokens + newPermits, capacity);
                lastTokenTime = now;
            }
            if (currentTokens > 0) {
                currentTokens--;
                return true;
            }
            return false;
        }
    }
}
