package org.song.algorithm.algorithmbase._02case.ratelimit;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimit_01_fixed_win {

    @Test
    public void test01() throws InterruptedException {

        RateLimitFixedWindow rateLimitFixedWindow = new RateLimitFixedWindow(10, 1);

        TestReporter reporter = new TestReporter();

        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(() -> {
                if (rateLimitFixedWindow.get()) {
                    reporter.success.getAndIncrement();

                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                    }
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
        RateLimitFixedWindow02 rateLimitFixedWindow = new RateLimitFixedWindow02(10, 1);

        TestReporter reporter = new TestReporter();

        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(() -> {
                if (rateLimitFixedWindow.get()) {
                    reporter.success.getAndIncrement();

                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                    }
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
                            .divide(BigDecimal.valueOf(success.get() + fail.get()), 2, BigDecimal.ROUND_HALF_UP));
        }
    }

    /**
     * 固定窗口,
     * 采用 计数器 + 定时器
     */
    static class RateLimitFixedWindow {

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
    static class RateLimitFixedWindow02 {

        private final AtomicInteger[] countWin;
        private final int maxLimit;

        /**
         * @param maxLimit 最大限流数量
         * @param seconds  单位秒, 必须 > 0
         */
        public RateLimitFixedWindow02(int maxLimit, int seconds) {
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
}
