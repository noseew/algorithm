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

        public RateLimitFixedWindow02(int maxLimit, int seconds) {
            this.maxLimit = maxLimit;
            seconds = seconds <= 0 ? 1 : seconds;
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
            // 当前秒
            int s = (int)(System.currentTimeMillis() / 1000);
            // 清空下一个窗口计数
            countWin[(s + 1) % countWin.length].set(0);

            int last = s;
            long count = 0;
            for (int i = 0; i < countWin.length - 1; i++) {
                count += countWin[(last -= i) % countWin.length].get();
            }
            if (count >= maxLimit) {
                return false;
            }
            // 当前窗口计数+1
            countWin[s % countWin.length].getAndIncrement();

            return true;
        }
    }
}
