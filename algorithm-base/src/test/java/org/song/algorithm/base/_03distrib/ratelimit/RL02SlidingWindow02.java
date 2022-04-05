package org.song.algorithm.base._03distrib.ratelimit;

import lombok.Data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口,
 * RateLimitFixedWindow03 的优化版本, 可以自定义窗口大小, 和窗口精度
 */
public class RL02SlidingWindow02 extends AbstractRateLimit {
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

    public RL02SlidingWindow02() {

    }

    /**
     * @param maxLimit 最大限流数量
     * @param seconds  单位限流窗口, 单位秒, 必须 > 0
     */
    public RL02SlidingWindow02(int maxLimit, int seconds, int secondsCount) {
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
        RL02SlidingWindow02 rateLimit;
        long now;
        boolean pass;

        public RateLimitWrapper(RL02SlidingWindow02 rateLimit, long now, boolean pass) {
            this.rateLimit = new RL02SlidingWindow02();
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
