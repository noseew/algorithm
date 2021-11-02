package org.song.algorithm.algorithmbase._02case.lb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

/**
 * 负载均衡算法
 */
public class LoadBalancing {

    @Data
    @AllArgsConstructor
    static class Task {
        private String name;
        private int wight;
        void invoke(Object o) {System.out.println(this.name + o);}
    }
    static List<Task> tasks = Lists.newArrayList( new Task("task1", 40),  new Task("task2", 20));

    interface LoadBalancer {
        Task select(List<Task> tasks);
    }


    @Test
    public void pollingLoadBalance() {
        PollingLoadBalance pollingLoadBalance = new PollingLoadBalance();
        IntStream.range(1, 50).forEach(e -> {
            pollingLoadBalance.select(tasks).invoke(e);
        });
    }

    static class PollingLoadBalance implements  LoadBalancer {
        private AtomicInteger count = new AtomicInteger();
        @Override
        public Task select(List<Task> tasks) {
            return tasks.get(count.getAndIncrement() % tasks.size());
        }
    }

    /**
     * 随机的方式
     * 要有随机数发生器, 但是不需要全局状态
     */
    @Test
    public void randomLoadBalance1() {
        RandomLoadBalance randomLoadBalance = new RandomLoadBalance();
        IntStream.range(1, 50).forEach(e -> {
            randomLoadBalance.select(tasks).invoke(e);
        });
    }

    static class RandomLoadBalance implements LoadBalancer {
        private Random random = new Random();
        @Override
        public Task select(List<Task> tasks) {
            return tasks.get(random.nextInt(tasks.size()));
        }
    }

    /**
     * 带有权重的随机的方式
     * 类似于彩票算法, 各种叫法不同
     */
    @Test
    public void randomLoadBalance2() {
        RandomLoadBalance2 randomLoadBalance = new RandomLoadBalance2();
        IntStream.range(1, 50).forEach(e -> {
            randomLoadBalance.select(tasks).invoke(e);
        });
    }

    static class RandomLoadBalance2 implements LoadBalancer {
        private Random random = new Random();
        @Override
        public Task select(List<Task> tasks) {
            int weightCount = 0;
            for (Task task : tasks) {
                weightCount += task.getWight();
            }
            // 随机范围, 0~30, 其中 0~10属于 第一个 task, 10~30属于第二个task
            int nextInt = random.nextInt(weightCount);
            for (Task task : tasks) {
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

    /**
     * 带有权重的轮询方式
     * 步长算法
     * 思路: 将权重划分成行进的步数, 相同的距离, 权重越高步数也就越高, 同时步长也就越短
     * 执行的时候, 就选择行进最慢的那个执行, 这样就永远不会出现在一定范围内某些任务执行次数极少另一些任务极多的不平衡现象
     * 
     * 和权重随机相比, 随机权重只是在统计意义上满足权重的分配, 同时在统计意义上比较均匀, 特别情况, 还会出现极端的极少或者极多的情况
     * 缺点: 实现稍显复杂, 需要保存全局状态
     * 
     * 使用场景: 
     * 1. CPU进程调度算法
     * 由于CPU简称调度有时间片的概念(这里就可以对应步长), 且最好不要出现用户任务长时间不执行的情况, 所以步长算法比随机权重轮询有一定优势
     */
    @Test
    public void roundRobinLoadBalance() {
        RoundRobinLoadBalance randomLoadBalance = new RoundRobinLoadBalance();
        IntStream.range(1, 50).forEach(e -> {
            randomLoadBalance.select(tasks).invoke(e);
        });
    }

    static class RoundRobinLoadBalance implements LoadBalancer {

        /*
        步长
        参考数 800, 他们的一个公倍数
        假设他们的步长 = 参考数 / 权重
        得到他们的步长分别为 {40, 20}

        步长的作用是, 在task行进的过程中, 各自task走各自的步长, 由于是公倍数, 所以他们最终在某些次数之后再次相遇齐头并进
         */
        private int[] steps;
        /*
        行程, 表示每个task行进的距离,
            行程 = 步长 * 前进次数
            这里的前进次数, 实际上就等于任务的执行次数, 其次数和权重成正比, 从而实现带有权重的轮训
        默认从0开始, 当他们都相同时, 等价于再次从0开始
        找到行程最短的那个task位置, 行驶他的行程
         */
        private int[] pass;


        @Override
        public Task select(List<Task> tasks) {
            if (steps == null || steps.length == 0 || steps.length != tasks.size()) {
                steps = new int[tasks.size()];
                for (int i = 0; i < steps.length; i++) {
                    steps[i] = (100 * tasks.size())  / tasks.get(i).getWight();
                }
            }
            if (pass == null || pass.length == 0 || pass.length != tasks.size()) {
                pass = new int[tasks.size()];
            }
            // 获取到最短行程的下标
            int index = minIndex(pass);
            // 跑的最慢的那个向前行驶一个步长
            pass[index] += steps[index];
            // 执行跑的最慢的那个
            return tasks.get(index);
        }

        /**
         * 获取数组中最小元素的下标
         * 注意这里采用简单的O(n)算法,
         * 在Linux中的CFS调度算法中, 也是一种步进调度, 此数据结构采用的是红黑树, 可以更快的找到指定的最小时间片, 从而执行
         */
        private static int minIndex(int[] steps) {
            int min = steps[0];
            int minIndex = 0;
            for (int i = 0; i < steps.length; i++) {
                if (steps[i] < min) {
                    min = steps[i];
                    minIndex = i;
                }
            }
            return minIndex;
        }
    }

    private ConcurrentMap<String, WeightedRoundRobin> methodWeightMap = new ConcurrentHashMap<>();
    /**
     * 轮询调度
     * 和其类似的调度有: 补偿调度, 随机调度, 都可以有权重和没有权重
     */
    @Test
    public void roundRobinLoadBalance2() {
        for (int e : IntStream.range(1, 50).toArray()) {
            doRoundRobinLoadBalance().invoke(e);
        }
    }

    private Task doRoundRobinLoadBalance() {

        int[] weights = new int[]{20, 40};
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        long now = System.currentTimeMillis();
        Task selectedInvoker = null;
        WeightedRoundRobin selectedWRR = null;

        for (int i = 0; i < tasks.size(); i++) {
            Task invoker = tasks.get(i);
            int weight = weights[i];
            // 如果val(权重)已存在, 则直接使用已存在的
            WeightedRoundRobin weightedRoundRobin = methodWeightMap.computeIfAbsent(invoker.getName(), k -> {
                WeightedRoundRobin wrr = new WeightedRoundRobin();
                wrr.setWeight(weight);
                return wrr;
            });
            if (weight != weightedRoundRobin.getWeight()) {
                //weight changed
                weightedRoundRobin.setWeight(weight);
            }
            long cur = weightedRoundRobin.increaseCurrent();
            weightedRoundRobin.setLastUpdate(now);
            if (cur > maxCurrent) {
                maxCurrent = cur;
                selectedInvoker = invoker;
                selectedWRR = weightedRoundRobin;
            }
            totalWeight += weight;
        }
        if (tasks.size() != methodWeightMap.size()) {
            methodWeightMap.entrySet().removeIf(item -> now - item.getValue().getLastUpdate() > 60000);
        }
        if (selectedInvoker != null) {
            selectedWRR.sel(totalWeight);
            return selectedInvoker;
        }
        // should not happen here
        return tasks.get(0);
    }

    @Data
    protected static class WeightedRoundRobin {
        // 当前资源的权重
        private int weight;
        // 执行次数
        private AtomicLong current = new AtomicLong(0);
        // 最后更新时间
        private long lastUpdate;

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
            current.set(0);
        }

        public long increaseCurrent() {
            return current.addAndGet(weight);
        }

        public void sel(int total) {
            current.addAndGet(-1 * total);
        }

        public long getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(long lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }


    private static int getWeight() {
        int weight = 0;
        return Math.max(weight, 0);
    }

}
