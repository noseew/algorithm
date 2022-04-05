package org.song.algorithm.base._03distrib.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 滑动窗口,
 * RateLimitFixedWindow03 的优化版本, 可以自定义窗口大小, 和窗口精度
 */
public class RL02SlidingWindow extends AbstractRateLimit {
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
    public RL02SlidingWindow(int maxLimit, int seconds, int secondsCount) {
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
        int currentIndex = (int) (now % this.win % countWin.length);
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
        for (int i = (int) (now % this.win); i < countWin.length; i++) {
            countWin[i % countWin.length].get();
            sb.append("i=").append(i).append(" count=").append(countWin[i % countWin.length].get()).append("\r\n");
        }
        return sb.toString();
    }
}
