package org.song.algorithm.base._03distrib.lb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.base.utils.AlgorithmUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 权重轮询调度步长
 * 带有权重的轮询方式
 * 步长算法
 * 思路: 将权重划分成行进的步数, 相同的距离, 权重越高步数也就越高, 同时步长也就越短
 * 执行的时候, 就选择行进最慢的那个执行, 这样就永远不会出现在一定范围内某些任务执行次数极少另一些任务极多的不平衡现象
 * <p>
 * 和权重随机相比, 随机权重只是在统计意义上满足权重的分配, 同时在统计意义上比较均匀, 特别情况, 还会出现极端的极少或者极多的情况
 * 缺点:
 * 1. 实现稍显复杂, 需要保存全局状态
 * 2. 适用于CPU调度, 也就是不会频繁切换CPU, 但是对于负载均衡来说, 并不平滑, 因为会连续多次调度会分配给同一个调度者
 * <p>
 * 使用场景:
 * 1. CPU进程调度算法
 * 由于CPU简称调度有时间片的概念(这里就可以对应步长), 且最好不要出现用户任务长时间不执行的情况, 所以步长算法比随机权重轮询有一定优势
 */
public class Lb03WRR1 extends AbstractLB {

    private volatile List<TaskInfo> taskInfos;

    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
//        initStep(tasks);
        initStep2(tasks);
        // 获取到最短行程的下标
        int index = minIndexAndForward();
        // 执行跑的最慢的那个
        return tasks.get(index);
    }

    /**
     * 初始化步长
     *
     * @param tasks
     */
    private void initStep(List<AbstractLB.Task> tasks) {
        boolean needInit = taskInfos == null || taskInfos.size() == 0 || taskInfos.size() != tasks.size();
        if (needInit) {
            synchronized (this) {
                // 每个任务的 步长
                taskInfos = new ArrayList<>(tasks.size());
                int lcm = tasks.get(0).getWight();
                for (AbstractLB.Task task : tasks) {
                    lcm = AlgorithmUtils.lcm(lcm, task.getWight());
                }
                for (AbstractLB.Task task : tasks) {
                        /*
                        权重越大, 步长越小, 权重和步长成反比
                        为了步长能够得到整数, 
                        step = 最小公倍数 / weight
                        为什么这样算? 每个任务都有前进的机会, 前进的最慢的获得执行机会, 同时获得前进一次的机会, 从而达到权重轮询的效果
                         */
                    int step = lcm / task.getWight();
                    taskInfos.add(new TaskInfo(step, new AtomicLong(0)));
                }
            }
        }
    }
    
    private void initStep2(List<AbstractLB.Task> tasks) {
        boolean needInit = taskInfos == null || taskInfos.size() == 0 || taskInfos.size() != tasks.size();
        if (needInit) {
            // 每个任务的 步长
            taskInfos = new ArrayList<>(tasks.size());
            int lcm = tasks.get(0).getWight();
            for (AbstractLB.Task task : tasks) {
                lcm = AlgorithmUtils.lcm(lcm, task.getWight());
            }
            for (AbstractLB.Task task : tasks) {
                        /*
                        权重越大, 步长越小, 权重和步长成反比
                        为了步长能够得到整数, 
                        step = 最小公倍数 / weight
                        为什么这样算? 每个任务都有前进的机会, 前进的最慢的获得执行机会, 同时获得前进一次的机会, 从而达到权重轮询的效果
                         */
                int step = lcm / task.getWight();
                taskInfos.add(new TaskInfo(step, new AtomicLong(0)));
            }
        }
    }

    /**
     * 获取数组中最小元素的下标
     * 注意这里采用简单的O(n)算法,
     * 在Linux中的CFS调度算法中, 也是一种步进调度, 此数据结构采用的是红黑树, 可以更快的找到指定的最小时间片, 从而执行
     */
    private int minIndexAndForward() {
        TaskInfo minPass = taskInfos.get(0);
        int minIndex = 0;
        for (int i = 0; i < taskInfos.size(); i++) {
            if (taskInfos.get(i).getPass().get() < minPass.getPass().get()) {
                minIndex = i;
            }
        }
        // 获取行程最小的
        minPass = taskInfos.get(minIndex);
        // 前进一步
        minPass.getPass().addAndGet(minPass.getStep());
        return minIndex;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class TaskInfo {
        /*
        步长
        参考数 800, 他们的一个公倍数
        假设他们的步长 = 参考数 / 权重
        得到他们的步长分别为 {40, 20}

        步长的作用是, 在task行进的过程中, 各自task走各自的步长, 由于是公倍数, 所以他们最终在某些次数之后再次相遇齐头并进
         */
        private int step;
        /*
        行程, 表示每个task行进的距离,
            行程 = 步长 * 前进次数
            这里的前进次数, 实际上就等于任务的执行次数, 其次数和权重成正比, 从而实现带有权重的轮训
        默认从0开始, 当他们都相同时, 等价于再次从0开始
        找到行程最短的那个task位置, 行驶他的行程
         */
        private AtomicLong pass;
    }
}
