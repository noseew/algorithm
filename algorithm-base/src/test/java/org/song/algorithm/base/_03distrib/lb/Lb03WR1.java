package org.song.algorithm.base._03distrib.lb;

import java.util.List;
import java.util.Random;

/**
 * 权重随机 WR WeightRandom
 * 带有权重的随机的方式
 * 类似于彩票算法, 各种叫法不同
 */
public class Lb03WR1 extends AbstractLB {
    
    private final Random random = new Random();

    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
        int totalWeight = 0;
        for (AbstractLB.Task task : tasks) {
            totalWeight += task.getWight();
        }
        // 随机范围, 0~30, 其中 0~10属于 第一个 task, 10~30属于第二个task
        int nextInt = random.nextInt(totalWeight);
        for (AbstractLB.Task task : tasks) {
            // 判断这个随机数落到了哪个范围,
            if (nextInt > task.getWight()) {
                // 不是这个范围, 减去后继续判断
                nextInt -= task.getWight();
            } else {
                // 是这个范围, 执行
                return task;
            }
        }
        return tasks.get(0);
    }
}
