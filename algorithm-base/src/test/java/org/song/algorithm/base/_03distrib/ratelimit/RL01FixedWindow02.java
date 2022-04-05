package org.song.algorithm.base._03distrib.ratelimit;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 固定窗口,
 * 采用 计数器 + 时间戳分组的数组
 */
public class RL01FixedWindow02 extends AbstractRateLimit {

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

    RL01FixedWindow02(int maxLimit, int winSecond) {
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
