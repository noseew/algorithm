package org.song.algorithm.algorithmbase._02case.ratelimit;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.ThreadUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

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
        RateLimitLeakyBucket limit = new RateLimitLeakyBucket(2, 10);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (limit.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁 " + LocalDateTime.now());
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
    public void test06() throws InterruptedException {
        /*
         */
        RateLimitTokenBucket limit = new RateLimitTokenBucket(2, 10);

        // 等待攒够一定量的令牌
        ThreadUtils.sleepRandom(TimeUnit.SECONDS, 5);
        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 400);
                if (limit.get()) {
                    reporter.success.getAndIncrement();
                    System.out.println(Thread.currentThread().getName() + "获取到锁 " + LocalDateTime.now());
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
     * 和固定窗口相比, 
     * 1. 流入无速率, 流出有速率, 使用定时器实现
     * 2. 流入的请求被阻挡了一阵子, 需要阻塞线程, 使用 LockSupport 实现
     */
    public static class RateLimitLeakyBucket {

        /**
         * 当前水位
         */
        private AtomicInteger waterLevel = new AtomicInteger();
        /**
         * 桶的容量
         */
        private volatile int capacity;
        /**
         * 队列就是桶, 队列的容量就是桶的容量
         */
        private BlockingQueue<Thread> queue;

        private ScheduledExecutorService scheduledExecutorService;

        public RateLimitLeakyBucket(int permitsPerSeconds, int capacity) {
            // 漏出窗口
            int win = 1000 / permitsPerSeconds;
            // 桶容量
            this.capacity = capacity;
            this.queue = new ArrayBlockingQueue<>(capacity);

            scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("RateLimitLeakyBucket");
                    return thread;
                }
            });
            
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                // 每固定窗口时间, 执行一次, 将线程取出并运行, 解除阻塞
                Thread thread = queue.poll();
                LockSupport.unpark(thread);
                // 同时水位降低
                this.waterLevel.decrementAndGet();
            }, 0, win, TimeUnit.MILLISECONDS);
        }

        /*
         */
        public boolean get() {
            Thread thread = Thread.currentThread();
            // 增加水位并判断水位是否超出
            if (waterLevel.incrementAndGet() < capacity) {
                // 未超出桶容量, 添加同步队列, 等待定时器调度, 同时阻塞线程
                queue.add(thread); // TODO 并发问题
                LockSupport.park(thread);
                return true;
            }
            // 水位超出桶容量, 降低水位, 直接限流
            waterLevel.decrementAndGet();
            return false;
        }
    }

    /**
     * 令牌桶
     * TODO song 待完成
     */
    public static class RateLimitTokenBucket {

        /**
         * 当前水位
         */
        private AtomicInteger waterLevel = new AtomicInteger();
        /**
         * 桶的容量
         */
        private volatile int capacity;
        
        private BlockingQueue<Thread> queue;

        private ScheduledExecutorService scheduledExecutorService;

        public RateLimitTokenBucket(int permitsPerSeconds, int capacity) {
            // 放入窗口, 放入一个需要多长时间
            int win = 1000 / permitsPerSeconds;
            // 桶容量
            this.capacity = capacity;
            this.queue = new ArrayBlockingQueue<>(capacity);

            scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setDaemon(true);
                    thread.setName("RateLimitLeakyBucket");
                    return thread;
                }
            });

            scheduledExecutorService.scheduleAtFixedRate(() -> {
                if (this.waterLevel.get() < this.capacity) {
                    // 固定速率放入令牌
                    this.waterLevel.incrementAndGet();
                }
                if (!queue.isEmpty()) {
                    Thread thread = queue.poll();
                    LockSupport.unpark(thread);
                }
                
            }, 0, win, TimeUnit.MILLISECONDS);
        }
        /**
         */
        public boolean get() {
            int permits = waterLevel.decrementAndGet();
            if (permits > 0) {
                // 令牌数量足够, 获取到自后直接放行
                return true;
            }
            // 令牌数量不够, 判断队列有没有满, 如果没有满, 就排队
            if (queue.size() < this.capacity) {
                Thread thread = Thread.currentThread();
                queue.add(thread);
                LockSupport.park(thread);
                return true;
            }
            // 令牌数量不够, 先归还, 确保数量一致
            waterLevel.incrementAndGet();
            // 直接限流
            return false;
        }
    }
}
