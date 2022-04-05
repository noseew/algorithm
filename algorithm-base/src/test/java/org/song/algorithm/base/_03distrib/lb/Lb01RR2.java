package org.song.algorithm.base._03distrib.lb;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询调度
 * 如果任务数量是2的次幂数, 可以优化算法使用位运算替换%运算, 提高效率
 * 摘自 Netty 源码
 */
public class Lb01RR2 extends AbstractLB {
    
    private final AtomicInteger count = new AtomicInteger();

    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
        // tasks.size() 必须是2的次幂数
        return tasks.get(count.getAndIncrement() & (tasks.size() - 1));
    }
}
