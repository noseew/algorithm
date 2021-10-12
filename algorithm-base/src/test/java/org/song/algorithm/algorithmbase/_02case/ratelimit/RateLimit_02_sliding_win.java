package org.song.algorithm.algorithmbase._02case.ratelimit;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimit_02_sliding_win {

    @Test
    public void test01() {
    }


    static class RateLimitSlidingWindow {

        private final AtomicInteger count = new AtomicInteger();
        private final int maxLimit;

        private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

        public RateLimitSlidingWindow(int maxLimit, int seconds) {
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
