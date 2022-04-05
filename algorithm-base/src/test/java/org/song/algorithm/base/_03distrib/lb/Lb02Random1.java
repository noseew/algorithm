package org.song.algorithm.base._03distrib.lb;

import java.util.List;
import java.util.Random;

/**
 * 随机 Random
 * 优点: 1) 实现简单, 2) 和RR相比, 不需要全局状态
 * 缺点: 1) 不公平, 调度者不确定, 仅仅在统计上具有公平性 2) 没有权重
 */
public class Lb02Random1 extends AbstractLB {
    private final Random random = new Random();

    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
        return tasks.get(random.nextInt(tasks.size()));
    }
}