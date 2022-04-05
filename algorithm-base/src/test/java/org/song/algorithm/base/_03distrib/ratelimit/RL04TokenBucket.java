package org.song.algorithm.base._03distrib.ratelimit;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 令牌桶
 * 使用了阻塞队列, 获取不到令牌先排队
 */
public class RL04TokenBucket extends AbstractRateLimit {

    /**
     * 当前水位
     */
    private AtomicInteger waterLevel = new AtomicInteger();

    private BlockingQueue<Thread> queue;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * @param permitsPerSeconds 每秒生产令牌数量(控制每秒最大并发数)
     * @param capacity          桶的容量(控制排队等待数)
     */
    public RL04TokenBucket(double permitsPerSeconds, int capacity) {
        // 放入窗口, 多长时间放入一个, 单位毫秒
        int win = (int) (1000d / permitsPerSeconds);
        // 桶容量
        this.queue = new ArrayBlockingQueue<>(capacity);

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
            // 取出阻塞的任务, 如果存在则解阻塞, poll() 如果没有队列任务, 则返回null
            Thread thread = queue.poll();
            if (thread != null) {
                LockSupport.unpark(thread);
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
        Thread thread = Thread.currentThread();
        // 令牌数量不够, 判断队列有没有满, 如果没有满就排队, offer()如果满了, 返回false
        if (queue.offer(thread)) {
            LockSupport.park(thread);
            return true;
        }
        // 令牌数量不够, 先归还, 确保数量一致
        waterLevel.incrementAndGet();
        // 直接限流
        return false;
    }
}
