package org.song.algorithm.algorithmbase._02case.ratelimit;

import lombok.Data;
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

        int tryTimes = 100;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 2000);
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

        int tryTimes = 100;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 2000);
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
        RateLimitSlidingWindow rateLimitSlidingWindow = new RateLimitSlidingWindow(10, 1, 5);

        TestReporter reporter = new TestReporter();

        int tryTimes = 100;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 2000);
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
    public void test04_2() throws InterruptedException {
        /*
        总数:1000, 成功:10, 失败:990, 通过率:0.0100
         */
        RateLimitSlidingWindow2 rateLimitSlidingWindow = new RateLimitSlidingWindow2(10, 1, 5);

        TestReporter reporter = new TestReporter();

        int tryTimes = 1000;

        for (int i = 0; i < tryTimes; i++) {
            Thread thread = new Thread(() -> {
                ThreadUtils.sleepRandom(TimeUnit.MILLISECONDS, 5000);
                if (rateLimitSlidingWindow.get()) {
                    reporter.success.getAndIncrement();
//                    System.out.println(Thread.currentThread().getName() + "获取到锁");
                } else {
                    reporter.fail.getAndIncrement();
//                    System.out.println(Thread.currentThread().getName() + "被限流");
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

    @Test
    public void test07() throws InterruptedException {
        /*
         */
        RateLimitTokenBucket2 limit = new RateLimitTokenBucket2(2, 10);

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
        // 每秒窗口数量
        private int secondsCount;
        // 窗口大小
        private int win;
        /**
         * 上一次请求通过的时间
         */
        private long last;

        /**
         * @param maxLimit 最大限流数量
         * @param seconds  单位限流窗口, 单位秒, 必须 > 0
         */
        public RateLimitSlidingWindow(int maxLimit, int seconds, int secondsCount) {
            // 转换成每秒限流大小
            this.maxLimit = maxLimit / seconds;
            // 每秒窗口数量
            this.secondsCount = secondsCount;
            // 窗口大小
            this.win = 1000 / secondsCount;
            /*
            将限流范围 
             */
            countWin = new AtomicInteger[secondsCount * 2];
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
            long now = System.currentTimeMillis();
            // 当前请求落点下标
            int currentIndex =  (int) (now % this.win % countWin.length);
            // 计算总数开始下标, currentIndex > i < startIndex, i都要清零
            int startIndex = (currentIndex + this.secondsCount) % countWin.length;
            // 上一次成功请求的下标, lastIndex > i < currentIndex, i都要清零
            int lastIndex = (int) (last % this.win % countWin.length);
            
            // 统计总数
            long count = 0;
            for (int i = 0; i < countWin.length; i++) {
                if (currentIndex > i && i < startIndex) {
                    countWin[i].set(0);
                } else if (lastIndex > i && i < currentIndex) {
                    countWin[i].set(0);
                } else {
                    // 计算各个窗口的总和
                    count += countWin[i].get();
                }
            }
            System.out.println(this);
            if (count >= this.maxLimit) {
                // 限流
                return false;
            }
            countWin[currentIndex].incrementAndGet();
            last = now;
            return true;
        }

        @Override
        public String toString() {
            long now = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            for (int i = (int)(now % this.win); i < countWin.length; i++) {
                countWin[i% countWin.length].get();
                sb.append("i=").append(i).append(" count=").append(countWin[i % countWin.length].get()).append("\r\n");
            }
            return sb.toString();
        }
    }

    /**
     * 滑动窗口,
     * RateLimitFixedWindow03 的优化版本, 可以自定义窗口大小, 和窗口精度
     */
    public static class RateLimitSlidingWindow2 {
        /**
         * 窗口数量
         */
        private Node[] countWin;
        /**
         * 限流窗口次数
         */
        private int maxLimit;
        // 每秒窗口数量
        private int secondsCount;
        // 窗口大小
        private int win;

        private static ExecutorService executorService = Executors.newSingleThreadExecutor();
        private static BlockingQueue<RateLimitWrapper> queue = new LinkedBlockingQueue<>();

        public RateLimitSlidingWindow2() {

        }

        /**
         * @param maxLimit 最大限流数量
         * @param seconds  单位限流窗口, 单位秒, 必须 > 0
         */
        public RateLimitSlidingWindow2(int maxLimit, int seconds, int secondsCount) {
            // 转换成每秒限流大小
            this.maxLimit = maxLimit / seconds;
            // 每秒窗口数量
            this.secondsCount = secondsCount;
            // 窗口大小
            this.win = 1000 / secondsCount;
            /*
            将限流范围
             */
            countWin = new Node[secondsCount * 2];
            for (int i = 0; i < countWin.length; i++) {
                Node node = new Node();
                node.counter = new AtomicInteger();
                countWin[i] = node;
            }

            executorService.execute(() -> {
                while (true) {
                    try {
                        RateLimitWrapper wrapper = queue.take();
                        System.out.println(wrapper.toString());
                    } catch (InterruptedException e) {
                    }
                }
            });
        }

        /**
         * 是否通过
         *
         * @return
         */
        public synchronized boolean get() {
            // 当前秒, 当前秒的访问会落到指定位置的数组中
            long now = System.currentTimeMillis();
            // 当前请求落点下标
            int currentIndex = (int) (now % this.win % countWin.length);
            // 计算总数开始下标, currentIndex > i < startIndex, i都要清零
            int startIndex = (currentIndex + this.secondsCount) % countWin.length;

            // 统计总数
            long count = 0;
            for (int i = 0; i < countWin.length; i++) {
                Node node = countWin[i];
                if (currentIndex > i && i < startIndex) {
                    node.counter.set(0);
                    node.last = now;
                } else if (node.last < now - 1000) {
                    node.counter.set(0);
                    node.last = now;
                } else {
                    // 计算各个窗口的总和
                    count += node.counter.get();
                }
            }
            if (count >= this.maxLimit) {
                // 限流
                queue.add(new RateLimitWrapper(this, now, false));
                return false;
            }
            queue.add(new RateLimitWrapper(this, now, true));
            countWin[currentIndex].counter.incrementAndGet();
            countWin[currentIndex].last = now;
            return true;
        }

        @Data
        static class Node {
            AtomicInteger counter;
            long last;

            @Override
            public String toString() {
                return String.valueOf(counter.get());
            }
        }

        @Data
        static class RateLimitWrapper {
            RateLimitSlidingWindow2 rateLimit;
            long now;
            boolean pass;

            public RateLimitWrapper(RateLimitSlidingWindow2 rateLimit, long now, boolean pass) {
                this.rateLimit = new RateLimitSlidingWindow2();
                this.rateLimit.maxLimit = rateLimit.maxLimit;
                this.rateLimit.secondsCount = rateLimit.secondsCount;
                this.rateLimit.win = rateLimit.win;

                Node[] countWin = new Node[rateLimit.countWin.length];
                System.arraycopy(rateLimit.countWin, 0, countWin, 0, rateLimit.countWin.length);
                this.rateLimit.countWin = countWin;
                this.now = now;
                this.pass = pass;
            }

            public String toString() {
                // 当前请求落点下标
                int currentIndex = (int) (now % rateLimit.win % rateLimit.countWin.length);
                // 计算总数开始下标, currentIndex > i < startIndex, i都要清零
                int startIndex = (currentIndex + rateLimit.secondsCount) % rateLimit.countWin.length;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < rateLimit.countWin.length; i++) {
                    Node node = rateLimit.countWin[i];
                    sb.append(" ");
                    if (startIndex < currentIndex && (startIndex <= i && i <= currentIndex)) {
                        sb.append("\033[32m")
                                .append(i).append("=").append(node.toString())
                                .append("\033[m");
                    } else if (startIndex > currentIndex && (startIndex <= i || i <= currentIndex)) {
                        sb.append("\033[32m")
                                .append(i).append("=").append(node.toString())
                                .append("\033[m");
                    } else {
                        sb.append(i).append("=").append(node.toString());
                    }
                }
                sb.append(" ").append(this.pass);
                return sb.toString();
            }
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
         * 队列就是桶, 队列的容量就是桶的容量
         */
        private BlockingQueue<Thread> queue;

        private ScheduledExecutorService scheduledExecutorService;

        /**
         * @param permitsPerSeconds 每秒漏出数量(控制每秒并发数)
         * @param capacity          桶的容量(控制排队等待数)
         */
        public RateLimitLeakyBucket(double permitsPerSeconds, int capacity) {
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

    /**
     * 令牌桶
     * 使用了阻塞队列, 获取不到令牌先排队
     */
    public static class RateLimitTokenBucket {

        /**
         * 当前水位
         */
        private AtomicInteger waterLevel = new AtomicInteger();

        private BlockingQueue<Thread> queue;

        private ScheduledExecutorService scheduledExecutorService;

        /**
         *
         * @param permitsPerSeconds 每秒生产令牌数量(控制每秒最大并发数)
         * @param capacity          桶的容量(控制排队等待数)
         */
        public RateLimitTokenBucket(double permitsPerSeconds, int capacity) {
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

    /**
     * 令牌桶
     * 没有使用阻塞队列, 获取不到令牌, 直接限流
     */
    public static class RateLimitTokenBucket2 {

        /**
         * 当前水位
         */
        private AtomicInteger waterLevel = new AtomicInteger();

        private ScheduledExecutorService scheduledExecutorService;

        /**
         *
         * @param permitsPerSeconds 每秒生产令牌数量(控制每秒最大并发数)
         * @param capacity          桶的容量(控制排队等待数)
         */
        public RateLimitTokenBucket2(double permitsPerSeconds, int capacity) {
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
}
