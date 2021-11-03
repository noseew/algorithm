package org.song.algorithm.algorithmbase._02case.lb;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
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
        void invoke(Object o) {System.out.println(this.name +" "+ o);}
    }
    static List<Task> tasks = Lists.newArrayList( new Task("task1", 40),  new Task("task2", 20));

    interface LoadBalancer {
        default Task select(List<Task> tasks) {
            return null;
        }
        default Task select(List<Task> tasks, Object param) {
            return select(tasks);
        }
    }

    /**
     * 轮询
     */
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

    /**
     * 轮询调度
     * 和其类似的调度有: 补偿调度, 随机调度, 都可以有权重和没有权重
     */
    @Test
    public void roundRobinLoadBalance2() {
        RoundRobinLoadBalance2 roundRobinLoadBalance2 = new RoundRobinLoadBalance2();
        for (int e : IntStream.range(1, 50).toArray()) {
            roundRobinLoadBalance2.select(tasks).invoke(e);
        }
    }

    static class RoundRobinLoadBalance2 implements LoadBalancer {
        private ConcurrentMap<String, WeightedRoundRobin> methodWeightMap = new ConcurrentHashMap<>();
        @Override
        public Task select(List<Task> tasks) {
            int totalWeight = 0;
            long maxCurrent = Long.MIN_VALUE;
            Task selectedInvoker = null;
            WeightedRoundRobin selectedWRR = null;

            for (Task invoker : tasks) {
                int weight = invoker.getWight();
                // 初始化或记录权重信息
                WeightedRoundRobin weightedRoundRobin = methodWeightMap.computeIfAbsent(invoker.getName(), k -> {
                    WeightedRoundRobin wrr = new WeightedRoundRobin();
                    wrr.setWeight(weight);
                    return wrr;
                });
                // 每走一步, 增加自己的步进(按照权重单位)
                long cur = weightedRoundRobin.increaseCurrent();
                // 标记出步进走的步数最快的那个任务
                if (cur > maxCurrent) {
                    maxCurrent = cur;
                    selectedInvoker = invoker;
                    selectedWRR = weightedRoundRobin;
                }
                totalWeight += weight;
            }
            if (selectedInvoker != null) {
                // 将走得最快的那个任务步进归零
                selectedWRR.sel(totalWeight);
                return selectedInvoker;
            }
            // should not happen here
            return tasks.get(0);
        }

        @Data
        static class WeightedRoundRobin {
            // 当前资源的权重
            private int weight;
            // 执行次数
            private AtomicLong current = new AtomicLong(0);

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
        }
    }

    /**
     * Hash 调度
     */
    @Test
    public void hashLoadBalance() {
        HashLoadBalance loadBalance = new HashLoadBalance();
        for (int e : IntStream.range(1, 5).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
        System.out.println("********************************");
        for (int e : IntStream.range(1, 5).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
        System.out.println("********************************");
        for (int e : IntStream.range(1, 5).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
    }

    static class HashLoadBalance implements LoadBalancer {
        @Override
        public Task select(List<Task> tasks, Object param) {
            int i = System.identityHashCode(param) % tasks.size();
            return tasks.get(i);
        }
    }

    /**
     * 一致性 Hash 调度
     */
    @Test
    public void consistencyHashLoadBalance() {
        ConsistencyHashLoadBalance loadBalance = new ConsistencyHashLoadBalance();
        System.out.println("*********************1**********************");
        for (int e : IntStream.range(1, 10).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
//        tasks.add(new Task("task3", 0));
        tasks.add(new Task("task4", 0));
        System.out.println("*********************2**********************");
        for (int e : IntStream.range(1, 10).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
//        tasks.removeIf(t -> t.getName().equals("task3"));
        tasks.removeIf(t -> t.getName().equals("task4"));
        System.out.println("*********************3**********************");
        for (int e : IntStream.range(1, 10).toArray()) {
            loadBalance.select(tasks, e).invoke(e);
        }
    }

    /*
    0___64___128___192___256
    Node01 [0-63]
    Node02 [64-127]
    Node02 [128-191]
    Node02 [182-256]
     */
    static class ConsistencyHashLoadBalance implements LoadBalancer {
        /**
         * Hash环大小, 默认1024
         */
        private final int range = 1 << 10;
        /**
         * Hash环节点范围
         * key=开始offset
         */
        private volatile TreeMap<Integer, Node> hashRingRange;
        /**
         * Hash环节点快捷访问Map
         */
        private Map<String, Node> hashKeyCache;

        /**
         * 确保Hash环是否需要调整
         * 
         * @param tasks
         */
        private void ensure(List<Task> tasks) {
            if (hashRingRange == null) {
                initRing(tasks);
            }
            if (hashRingRange.size() != tasks.size()){
                adjustNode(tasks);
            }
        }

        /**
         * 初始化Hash环和节点
         * 
         * @param tasks
         */
        private synchronized void initRing(List<Task> tasks) {
            if (hashRingRange != null) {
                return; // DCL
            }
            hashRingRange = new TreeMap<>();
            int step = range / tasks.size();
            int start = 0;
            for (Task task : tasks) {
                Node node = new Node(task.getName(), start, start + step);
                if (range - node.getEnd() < step) {
                    // 如果是尾节点, 直接取到最后
                    node.setEnd(range);
                }
                hashRingRange.put(start, node);
                start += step;
            }
            updateCache();
        }

        private void updateCache() {
            hashKeyCache = hashRingRange.values().stream().collect(Collectors.toMap(Node::getKey, Function.identity(), (k1, k2) -> k1));
        }

        /**
         * 调整Hash环
         */
        public synchronized void adjustNode(List<Task> tasks) {
            if (hashRingRange.size() == tasks.size()){
                return; // DCL
            }
            Set<String> newTaskSet = tasks.stream().map(Task::getName).collect(Collectors.toSet());
            Set<String> ringNodeSet = hashRingRange.values().stream().map(Node::getKey).collect(Collectors.toSet());

            // Hash环中需要删除的key
            Set<String> needRemove = Sets.difference(ringNodeSet, newTaskSet);
            // Hash环中需要添加的key
            Set<String> needInsert = Sets.difference(newTaskSet, ringNodeSet);

            if (!needRemove.isEmpty()) {
                // 节点全部删除
                if (needRemove.size() == hashRingRange.size()) {
                    hashRingRange = null;
                    initRing(tasks);
                    return;
                }
                // 节点部分删除
                for (String key : needRemove) {
                    removeNode(hashKeyCache.get(key));
                }
            }
            if (!needInsert.isEmpty()) {
                for (String key : needInsert) {
                    insertNode(new Node(key, 0, 0));
                }
            }
            updateCache();
        }

        /**
         * 删除Hash环中的节点
         * 
         * @param node
         */
        private void removeNode(Node node) {
            if (node == null) {
                return;
            }
            // 前一个节点
            Map.Entry<Integer, Node> preNodeEntry = hashRingRange.floorEntry(node.getStart() - 1);
            // 后一个节点
            Map.Entry<Integer, Node> nextNodeEntry = hashRingRange.floorEntry(node.getEnd() + 1);
            // 删除当前节点
            hashRingRange.remove(node.getStart());
            if (preNodeEntry != null) {
                // 删除后一个节点或者尾节点
                // 直接修改前一个node的范围
                preNodeEntry.getValue().setEnd(node.getEnd());
            } else if (nextNodeEntry != null) {
                // 如果前一个node没有, 则说明删除的是头结点
                nextNodeEntry.getValue().setStart(node.getStart());
            } else {
                // 不可能走到这里, 初始化 hashRangeMap
            }
        }

        /**
         * 插入一个节点
         * 
         * @param node
         */
        private synchronized void insertNode(Node node) {
            // 大节点 找到空隙最大的节点一分为二
            Node maxNode = hashRingRange.values().stream().max(Comparator.comparing(e -> e.getEnd() - e.getStart())).get();
            int middle = (maxNode.getEnd() - maxNode.getStart()) / 2;
            int oldEnd = maxNode.getEnd();
            // 大节点 放弃后半部分
            maxNode.setEnd(maxNode.getStart() + middle);
            // 新节点 使用大节点的后半部分
            node.setStart(maxNode.getEnd());
            node.setEnd(oldEnd);
            // 插入Node
            hashRingRange.put(node.getStart(), node);
        }

        @Override
        public Task select(List<Task> tasks, Object param) {
            ensure(tasks);
            int i = System.identityHashCode(param) % range;
            Node node = hashRingRange.floorEntry(i).getValue();
            Map<String, Task> taskMap = tasks.stream()
                    .collect(Collectors.toMap(Task::getName, Function.identity(), (k1, k2) -> k1));
            return taskMap.getOrDefault(node.getKey(), tasks.get(0));
        }

        @Data
        @AllArgsConstructor
        static class Node {
            // 节点和Task的对应标识
            private String key;
            // 拥有hash环中开始的offset
            private int start;
            // 拥有hash环中截止的offset
            private int end;
        }
    }

}
