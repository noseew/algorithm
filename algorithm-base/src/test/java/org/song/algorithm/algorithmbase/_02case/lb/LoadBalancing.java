package org.song.algorithm.algorithmbase._02case.lb;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 负载均衡算法
 */
public class LoadBalancing {

    interface Task {
        void invoke(Object o);
    }

    private static Task task1() {
        return o -> System.out.println("task1 " + o);
    }

    private static Task task2() {
        return o -> System.out.println("task2 " + o);
    }

    private static List<Task> tasks = Lists.newArrayList(task1(), task2());

    @Test
    public void test01() {
//        polling();
//        random();
//        weightRandom();
        weightPolling();
    }

    /**
     * 轮询的方式
     * 需要有全局变量来记住轮询的状态
     */
    private AtomicInteger count = new AtomicInteger();

    public void polling() {
        IntStream.range(1, 50).forEach(e -> {
            Task task = tasks.get(count.getAndIncrement() % tasks.size());
            task.invoke(e);
        });
    }

    /**
     * 随机的方式
     * 要有随机数发生器, 但是不需要全局状态
     */
    public void random() {
        Random random = new Random();
        IntStream.range(1, 50).forEach(e -> {
            Task task = tasks.get(random.nextInt(tasks.size()));
            task.invoke(e);
        });
    }

    /**
     * 带有权重的随机的方式
     * 类似于彩票算法, 各种叫法不同
     */
    public void weightRandom() {
        Random random = new Random();
        // 假设 Task 的权重
        int[] weight = new int[]{10, 20};

        int weightCount = 0;
        for (int i : weight) {
            weightCount += i;
        }
        for (int e : IntStream.range(1, 50).toArray()) {
            // 随机范围, 0~30, 其中 0~10属于 第一个 task, 10~30属于第二个task
            int nextInt = random.nextInt(weightCount);
            for (int wi = 0; wi < weight.length; wi++) {
                // 判断这个随机数落到了哪个范围, 
                if (nextInt > weight[wi]) {
                    // 不是这个范围, 减去后继续判断
                    nextInt -= weight[wi];
                } else {
                    // 是这个范围, 执行
                    Task task = tasks.get(wi);
                    task.invoke(e);
                }
            }
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
    public void weightPolling() {
        // 假设 Task 的权重
        int[] weight = new int[]{20, 40};
        /*
        步长
        参考数 800, 他们的一个公倍数
        假设他们的步长 = 参考数 / 权重
        得到他们的步长分别为 {40, 20}
        
        步长的作用是, 在task行进的过程中, 各自task走各自的步长, 由于是公倍数, 所以他们最终在某些次数之后再次相遇齐头并进
         */
        int[] steps = new int[2];
        for (int i = 0; i < steps.length; i++) {
            steps[i] = 800 / weight[i];
        }
        /*
        行程, 表示每个task行进的距离, 
            行程 = 步长 * 前进次数
            这里的前进次数, 实际上就等于任务的执行次数, 其次数和权重成正比, 从而实现带有权重的轮训
        默认从0开始, 当他们都相同时, 等价于再次从0开始
        找到行程最短的那个task位置, 行驶他的行程
         */
        int[] pass = new int[2];

        for (int e : IntStream.range(1, 50).toArray()) {

            // 获取到最短行程的下标
            int index = minIndex(pass);
            // 跑的最慢的那个向前行驶一个步长
            pass[index] += steps[index];
            
            // 执行跑的最慢的那个
            Task task = tasks.get(index);
            task.invoke(e);
        }
    }

    /**
     * 获取数组中最小元素的下标
     */
    private int minIndex(int[] steps) {
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
