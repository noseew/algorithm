package org.song.algorithm.base._03distrib.ratelimit;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 令牌桶
 * 没有使用阻塞队列, 获取不到令牌, 直接限流
 */
public class RL04TokenBucket02 extends AbstractRateLimit {

    /**
     * 当前水位
     */
    private AtomicInteger waterLevel = new AtomicInteger();

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * @param permitsPerSeconds 每秒生产令牌数量(控制每秒最大并发数)
     * @param capacity          桶的容量(控制排队等待数)
     */
    public RL04TokenBucket02(double permitsPerSeconds, int capacity) {
        // 放入窗口, 多长时间放入一个, 单位毫秒
        int win = (int) (1000d / permitsPerSeconds);

        scheduledExecutorService = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("RateLimitLeakyBucket");
            return thread;
        });

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (this.waterLevel.get() < capacity) {
                // 固定速率放入令牌
                this.waterLevel.incrementAndGet();
            }
        }, 0, win, TimeUnit.MILLISECONDS);
    }

    /**
     *
     */
    public boolean get() {
        int permits = waterLevel.decrementAndGet();
        if (permits > 0) {
            // 没有多余的 令牌数量足够, 获取到自后直接放行
            return true;
        }
        // 令牌数量不够, 先归还, 确保数量一致
        waterLevel.incrementAndGet();
        // 直接限流
        return false;
    }
}
