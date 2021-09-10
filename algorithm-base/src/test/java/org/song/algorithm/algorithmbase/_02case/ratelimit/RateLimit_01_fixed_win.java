package org.song.algorithm.algorithmbase._02case.ratelimit;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimit_01_fixed_win {

    @Test
    public void test01() {
        RateLimitFixedWindow rateLimitFixedWindow = new RateLimitFixedWindow(10, 1);

        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(() -> {
                if (rateLimitFixedWindow.get()) {
                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                    try {
                        TimeUnit.MILLISECONDS.sleep(200);
                    } catch (InterruptedException e) {
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + "被限流");
                }
            }, "T" + i);
            thread.start();
        }
    }


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

        public boolean get() {
            return count.getAndIncrement() <= maxLimit;
        }
    }
}
