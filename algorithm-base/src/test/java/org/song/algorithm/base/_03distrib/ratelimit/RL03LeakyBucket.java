package org.song.algorithm.base._03distrib.ratelimit;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * 漏桶算法
 * 和固定窗口相比,
 * 1. 流入无速率, 流出有速率, 使用定时器实现
 * 2. 流入的请求被阻挡了一阵子, 需要阻塞线程, 使用 LockSupport 实现
 */
public class RL03LeakyBucket extends AbstractRateLimit {

    /**
     * 队列就是桶, 队列的容量就是桶的容量
     */
    private BlockingQueue<Thread> queue;

    private ScheduledExecutorService scheduledExecutorService;

    /**
     * @param permitsPerSeconds 每秒漏出数量(控制每秒并发数)
     * @param capacity          桶的容量(控制排队等待数)
     */
    public RL03LeakyBucket(double permitsPerSeconds, int capacity) {
        // 漏出窗口时间, 单位毫秒
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
            try {
                    /*
                    每固定窗口时间, 执行一次, 将线程取出并运行, 解除阻塞
                    take() 如果空的 取出阻塞
                     */
                Thread thread = queue.take();
                LockSupport.unpark(thread);
            } catch (InterruptedException e) {
            }
        }, 0, win, TimeUnit.MILLISECONDS);
    }

    /*

     */
    public boolean get() {
        Thread thread = Thread.currentThread();
            /*
            容量是否已满, 添加同步队列, 等待定时器调度, 同时阻塞线程
            offer() 如果满了 返回false
             */
        if (queue.offer(thread)) {
            LockSupport.park(thread);
            return true;
        }
        return false;
    }
}
