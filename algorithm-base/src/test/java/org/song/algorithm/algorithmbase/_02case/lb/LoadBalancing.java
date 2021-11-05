package org.song.algorithm.algorithmbase._02case.lb;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.DispatchUtils;
import org.springframework.util.StopWatch;

import java.util.*;
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

    @Test
    public void test_RR1() {
        RR_1 rr1 = new RR_1();
        DispatchUtils instance = DispatchUtils.getInstance();
        IntStream.range(1, 100).forEach(e -> {
            Task task = rr1.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        });
        System.out.println(instance.toPrettyString());
    }
    @Test
    public void test_RR2() {
        RR_1 rr1 = new RR_1();
        RR_2 rr2 = new RR_2();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("rr1");
        IntStream.range(1, 100).forEach(e -> {
            rr1.select(tasks).invoke(e);
        });
        stopWatch.stop();
        stopWatch.start("rr2");
        IntStream.range(1, 100).forEach(e -> {
            rr2.select(tasks).invoke(e);
        });
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }

    @Test
    public void test_Random() {
        Random_1 random1 = new Random_1();
        IntStream.range(1, 50).forEach(e -> {
            random1.select(tasks).invoke(e);
        });
    }

    @Test
    public void test_WR() {
        WR_1 wr_1 = new WR_1();
        IntStream.range(1, 50).forEach(e -> {
            wr_1.select(tasks).invoke(e);
        });
    }

    @Test
    public void test_WRR1() {
        WRR_1 wrr_1 = new WRR_1();
        IntStream.range(1, 50).forEach(e -> {
            wrr_1.select(tasks).invoke(e);
        });
    }

    @Test
    public void test_SWRR() {
        SmoothWRRLB_1 lb = new SmoothWRRLB_1();
        DispatchUtils instance = DispatchUtils.getInstance();
        tasks.add(new Task("task3", 30));
        for (int e : IntStream.range(1, 50).toArray()) {
            Task task = lb.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        }
        System.out.println(instance.toPrettyString());
    }

    @Test
    public void test_SWRR2() {
        SmoothWRRLB_2 lb = new SmoothWRRLB_2();
        DispatchUtils instance = DispatchUtils.getInstance();
        tasks.add(new Task("task3", 30));
        for (int e : IntStream.range(1, 50).toArray()) {
            Task task = lb.select(tasks);
            instance.increment(task.getName(), 1);
            task.invoke(e);
        }
        System.out.println(instance.toPrettyString());
    }
    
    @Test
    public void test_hash() {
        Hash_1 hash_1 = new Hash_1();
        for (int e : IntStream.range(1, 5).toArray()) {
            hash_1.select(tasks, e).invoke(e);
        }
        System.out.println("********************************");
        for (int e : IntStream.range(1, 5).toArray()) {
            hash_1.select(tasks, e).invoke(e);
        }
        System.out.println("********************************");
        for (int e : IntStream.range(1, 5).toArray()) {
            hash_1.select(tasks, e).invoke(e);
        }
    }

    @Test
    public void test_consistencyHash() {
        ConsistencyHash_1 loadBalance = new ConsistencyHash_1();
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
    调度算法(负载均衡算法)
    1. 轮询 RR RoundRobin
    2. 随机 Random
    3. 权重随机 WR WeightRandom
    4. 权重轮询 WRR WeightRoundRobin: 轮询+权重, 不会出现随机权重那样的极端情况, 适用于CPU时间片调度
    5. 平滑权重轮询 SWRR SmoothWeightRoundRobin: 轮询+权重+平滑, 不会出现权重轮询那样连续多次调度同一个任务的情况, 适用于请求负载均衡
    6. Hash
    7. 一致性Hash
     */
    interface LoadBalancer {
        default Task select(List<Task> tasks) {
            return null;
        }
        default Task select(List<Task> tasks, Object param) {
            return select(tasks);
        }
    }

    /**
     * 轮询 RR RoundRobin
     * 优点: 1)属于公平调度, 2)实现简单
     * 缺点: 1)无法将被调度者按性能区分, 也就是没有权重的概念; 2)需要有个全局状态记录上次调度的任务
     */
    static class RR_1 implements  LoadBalancer {
        private final AtomicInteger count = new AtomicInteger();
        @Override
        public Task select(List<Task> tasks) {
            return tasks.get(count.getAndIncrement() % tasks.size());
        }
    }

    /**
     * 轮询调度
     * 如果任务数量是2的次幂数, 可以优化算法使用位运算替换%运算, 提高效率
     * 摘自 Netty 源码
     */
    static class RR_2 implements  LoadBalancer {
        private final AtomicInteger count = new AtomicInteger();
        @Override
        public Task select(List<Task> tasks) {
            // tasks.size() 必须是2的次幂数
            return tasks.get(count.getAndIncrement() & (tasks.size() - 1));
        }
    }

    /**
     * 随机 Random
     * 优点: 1) 实现简单, 2) 和RR相比, 不需要全局状态
     * 缺点: 1) 不公平, 调度者不确定, 仅仅在统计上具有公平性 2) 没有权重
     */
    static class Random_1 implements LoadBalancer {
        private final Random random = new Random();
        @Override
        public Task select(List<Task> tasks) {
            return tasks.get(random.nextInt(tasks.size()));
        }
    }

    /**
     * 权重随机 WR WeightRandom
     * 带有权重的随机的方式
     * 类似于彩票算法, 各种叫法不同
     */
    static class WR_1 implements LoadBalancer {
        private final Random random = new Random();
        @Override
        public Task select(List<Task> tasks) {
            int totalWeight = 0;
            for (Task task : tasks) {
                totalWeight += task.getWight();
            }
            // 随机范围, 0~30, 其中 0~10属于 第一个 task, 10~30属于第二个task
            int nextInt = random.nextInt(totalWeight);
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
     * 权重轮询调度步长
     * 带有权重的轮询方式
     * 步长算法
     * 思路: 将权重划分成行进的步数, 相同的距离, 权重越高步数也就越高, 同时步长也就越短
     * 执行的时候, 就选择行进最慢的那个执行, 这样就永远不会出现在一定范围内某些任务执行次数极少另一些任务极多的不平衡现象
     *
     * 和权重随机相比, 随机权重只是在统计意义上满足权重的分配, 同时在统计意义上比较均匀, 特别情况, 还会出现极端的极少或者极多的情况
     * 缺点: 
     * 1. 实现稍显复杂, 需要保存全局状态
     * 2. 适用于CPU调度, 也就是不会频繁切换CPU, 但是对于负载均衡来说, 并不平滑, 因为会连续多次调度会分配给同一个调度者
     *
     * 使用场景: 
     * 1. CPU进程调度算法
     * 由于CPU简称调度有时间片的概念(这里就可以对应步长), 且最好不要出现用户任务长时间不执行的情况, 所以步长算法比随机权重轮询有一定优势
     */
    static class WRR_1 implements LoadBalancer {

        private volatile List<TaskInfo> taskInfos;

        @Override
        public Task select(List<Task> tasks) {
            initStep(tasks);
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
        private void initStep(List<Task> tasks) {
            boolean needInit = taskInfos == null || taskInfos.size() == 0 || taskInfos.size() != tasks.size();
            if (needInit) {
                synchronized (this){
                    // 每个任务的 步长
                    taskInfos = new ArrayList<>(tasks.size());
                    long totalWeight = 0;
                    for (Task task : tasks) {
                        totalWeight += task.getWight();
                    }
                    totalWeight = totalWeight < 0 ? Long.MAX_VALUE : totalWeight;
                    for (Task task : tasks) {
                        /*
                        权重越大, 步长越小, 权重和步长成反比
                        为了步长能够得到整数, 
                        step = sum(weight) / weight
                        为什么这样算? 每个任务都有前进的机会, 前进的最慢的获得执行机会, 同时获得前进一次的机会, 从而达到权重轮询的效果
                         */
                        int step = (int)(totalWeight / (long) task.getWight());
                        taskInfos.add(new TaskInfo(step, new AtomicLong(0)));
                    }
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

    /**
     * 权重轮询调度
     * 算法摘自 dubbo 的权重轮询
     * NGINX默认的负载均衡算法也是基于此
     */
    static class SmoothWRRLB_1 implements LoadBalancer {
        private final ConcurrentMap<String, TaskInfo> taskProgress = new ConcurrentHashMap<>();
        @Override
        public Task select(List<Task> tasks) {
            int totalWeight = 0;
            long maxCurrent = Long.MIN_VALUE;
            Task selected = null;

            for (Task invoker : tasks) {
                int weight = invoker.getWight();
                // 初始化或记录权重信息
                TaskInfo taskInfo = taskProgress.computeIfAbsent(invoker.getName(), k -> new TaskInfo());
                // 每走一步, 增加自己的步进(按照权重单位)
                long cur = taskInfo.forward(weight);
                // 标记出步进走的步数最快的那个任务
                if (cur > maxCurrent) {
                    maxCurrent = cur;
                    selected = invoker;
                }
                totalWeight += weight;
            }
            if (selected != null) {
                // 将走得最快的那个任务步进归零
                taskProgress.get(selected.getName()).reset(totalWeight);
                return selected;
            }
            // should not happen here
            return tasks.get(0);
        }

        @Data
        static class TaskInfo {
            // 执行次数
            private AtomicLong step = new AtomicLong(0);

            public long forward(int weight) {
                return step.addAndGet(weight);
            }

            public void reset(int total) {
                step.addAndGet(-1 * total);
            }
        }
    }
    
    /*
    权重轮询平滑
    SmoothWRRLB_1 的简化版本
    
    思路
    1. 轮询每个任务, 每个任务记录一个行程pass, 每一次轮询都会向前行进, 步长等于各自的权重
    2. 每一轮选出一个行程最大的任务, 作为执行任务, 同时将其行程归零(-总权重)
    证明过程: https://mp.weixin.qq.com/s/HQTpiEdbIC-OgMJTMIc3oQ
     */
    /*
    证明权重合理性
        假如有n个结点, 记第i个结点的权重是xi
        设总权重为 S=x1 + x2 + ... + xn
    选择分两步
        1. 为每个节点加上它的权重值
        2. 选择最大的节点减去总的权重值
            n个节点的初始化值为[0, 0, …, 0], 数组长度为n, 值都为0. 
            第一轮选择的第1步执行后, 数组的值为[x1, x2, ..., xn]. 
            假设第1步后, 最大的节点为j, 则第j个节点减去S. 
            所以第2步的数组为[x1, x2, ..., xj-S, ..., xn]. 执行完第2步后, 数组的和为
                x1 + x2 + ... + xj-S + ... + xn => x1 + x2 + ... + xn - S = S - S = 0
        由此可见, 每轮选择, 第1步操作都是数组的总和加上S, 第2步总和再减去S, 所以每轮选择完后的数组总和都为0.
        
    假设总共执行S轮选择, 记第mi个结点选择次. 第i个结点的当前权重为wi. 假设节点j在第t轮(t < S)之前, 已经被选择了xj次, 记此时第j个结点的当前权重为
    wj=t*xj-xj*S=(t-S)*xj<0
    因为t恒小于S, 所以wj<0. 
    前面假设总共执行S轮选择, 则剩下S-t轮, 上面的公式
    wj=(t-S)*xj+(S-t)*xj=0
    所以在剩下的选择中, wj永远小于等于0, 由于上面已经证明任何一轮选择后,  数组总和都为0, 则必定存在一个节点k使得wk>0, 永远不会再选中xj. 
    
    由此可以得出, 第i个结点最多被选中xi次, 即mi<=xi. 
    因为 S=m1+m2+...+mn
        S=x1 + x2 + ... + xn
    所以, 可以得出 mi==xi
     */
    /*
    平滑性证明
    证明平滑性, 只要证明不要一直都是连续选择那一个节点即可. 
    跟上面一样, 假设总权重为S, 假如某个节点xi连续选择了t(t<xi)次, 只要存在下一次选择的不是xi, 即可证明是平滑的. 
    假设t=xi-1, 此是第i个结点的当前权重为
        wi=t*xi-t*S=(xi-1)*xi-(xi-1)*S. 
    证明下一轮的第1步执行完的值wi+xi不是最大的即可. 
        wi+xi=> 
        (xi-1)*xi-(xi-1)*S+xi=>
        xi*xi-xi*S+S=>
        (xi-1)*(xi-S)+xi
    因为xi恒小于S, 所以xi-S<=-1. 所以上面：
        (xi-1)*(xi-S)+xi <= (xi-1)*-1+xi = -xi+1+xi=1. 
    所以, 第t轮后, 再执行完第1步的值wi+xi<=1. 
    如果这t轮刚好是最开始的t轮, 则必定存在另一个结点j的值为xj*t, 所以有
        wi+xi<=1<1*t<xj*t
    所以下一轮肯定不会选中x. 
     */
    static class SmoothWRRLB_2 implements LoadBalancer {
        /**
         * key=任务标识
         * val=任务的行程
         */
        private final Map<String, AtomicInteger> wightMap = new HashMap<>();

        public Task select(List<Task> tasks) {
            Task selected = null;
            int maxStep = Integer.MIN_VALUE;
            int totalWight = 0;

            for (Task task : tasks) {
                AtomicInteger pass = wightMap.computeIfAbsent(task.getName(), k -> new AtomicInteger(task.getWight()));
                // 每个任务向前行进自己的步进, 步长=权重
                int current = pass.addAndGet(task.getWight());
                totalWight += task.getWight();
                // 行程比较长的任务, 得到此次的执行权
                if (current > maxStep) {
                    selected = task;
                    maxStep = current;
                }
            }

            if (selected != null) {
                // 得到此次执行权的任务, 行程归零
                wightMap.get(selected.getName()).addAndGet(-totalWight);
                return selected;
            }
            return tasks.get(0);
        }
    }

    /**
     * Hash 调度
     */
    static class Hash_1 implements LoadBalancer {
        @Override
        public Task select(List<Task> tasks, Object param) {
            int i = System.identityHashCode(param) % tasks.size();
            return tasks.get(i);
        }
    }

    /*
    0___64___128___192___256
    Node01 [0-63]
    Node02 [64-127]
    Node02 [128-191]
    Node02 [182-256]
     */
    /**
     * 一致性 Hash 调度
     */
    static class ConsistencyHash_1 implements LoadBalancer {
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
            if (hashRingRange.size() != tasks.size()) {
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
            for (Task task : tasks) {
                if (task.getName().equals(node.getKey())) {
                    return task;
                }
            }
            return tasks.get(0);
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
