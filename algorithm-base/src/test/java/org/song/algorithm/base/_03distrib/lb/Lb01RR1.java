package org.song.algorithm.base._03distrib.lb;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询 RR RoundRobin
 * 优点: 1)属于公平调度, 2)实现简单
 * 缺点: 1)无法将被调度者按性能区分, 也就是没有权重的概念; 2)需要有个全局状态记录上次调度的任务
 */
public class Lb01RR1 extends AbstractLB {
//    private final AtomicInteger count = new AtomicInteger();
    private int count;

    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
//        return tasks.get(count.getAndIncrement() % tasks.size());
        return tasks.get(count++ % tasks.size());
    }
}
