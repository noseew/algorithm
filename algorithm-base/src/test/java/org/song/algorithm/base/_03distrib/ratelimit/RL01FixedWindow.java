package org.song.algorithm.base._03distrib.ratelimit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口,
 * 采用 计数器 + 定时器
 */
public class RL01FixedWindow extends AbstractRateLimit {

    private final AtomicInteger count = new AtomicInteger();
    private final int maxLimit;

    private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public RL01FixedWindow(int maxLimit, int seconds) {
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
