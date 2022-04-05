package org.song.algorithm.base._03distrib.lb;

/*
LVS中负载均衡采用的算法

算法思路:
1. 找出一个标记, 等于最大的权重数
2. 在所有任务重一次对比, 满足任务重的权重数>=标记的权重数的任务, 则依次执行
3. 每次轮询调用, 这个标记都会减去(所有权重)最大公约数
4. 直到这个标记为0, 一切从头开始, 回到任务1
在轮询的过程中, 这个标记数会逐渐减少, 也就是能满足调用执行的任务就越来越多, 也就对应着不同任务的权重不同

http://kb.linuxvirtualserver.org/wiki/Weighted_Round-Robin_Scheduling

加权轮询调度是为了更好地处理具有不同处理能力的服务器. 每个服务器都可以分配一个权重, 这是一个表示处理能力的整数值. 
权重较高的服务器比权重较低的服务器优先接收新连接, 权重较高的服务器比权重较低的服务器获得更多的连接, 权重相等的服务器获得相同的连接. 
加权轮循调度的伪代码如下:

    设有一个服务器集S = {S0, S1, ..., Sn-1};
    W(Si)表示Si的权重;
    I表示上次选择的服务器, I初始化为-1;
    Cw为调度中的当前权重, Cw初始化为零;
    max(S)为S中所有服务器的最大权值;
    gcd(S)是S中所有服务器权重的最大公约数;
    
    while (true) {
        i = (i + 1) mod n;
        if (i == 0) {
            cw = cw - gcd(S); 
            if (cw <= 0) {
                cw = max(S);
                if (cw == 0)
                return NULL;
            }
        } 
        if (W(Si) >= cw) 
            return Si;
    }

    
例如实服务器A/实服务器B/实服务器C的权值分别为4/3/2, 则在一个调度周期内, 调度顺序为AABABCABC (mod sum(Wi)). 
在优化的加权轮循调度实现中, 修改IPVS规则后, 会根据服务器权重生成一个调度顺序. 网络连接按照调度顺序依次连接到不同的实服务器. 
当实服务器处理能力不同时, 加权轮询调度优于轮询调度. 但是, 如果请求的负载差异很大, 可能会导致实服务器之间的动态负载不平衡. 
简而言之, 需要大量响应的大多数请求可能被定向到同一台实服务器. 
实际上, 轮循调度是加权轮循调度的一个特殊实例, 其中所有权值相等. 

LVS 算法对比 NGINX 算法
如果服务器的权重差别很大, 出于平滑的考虑, 避免短时间内会对服务器造成冲击, 你可以选择Nginx的算法, 
如果服务器的差别不是很大, 可以考虑使用LVS的算法, 因为测试可以看到它的性能要好于Nginx的算法:
LVS 性能更高
NGINX 平滑性更好
 */

import org.song.algorithm.base.utils.AlgorithmUtils;

import java.util.List;

/**
 * 权重轮询调度
 */
public class Lb03WRR2 extends AbstractLB {
    /**
     * 权重的最大公约数
     */
    private int divisor = 0;
    /**
     * 最大的权重
     */
    private int maxWeight = 0;
    /**
     * 上次选中的下标
     */
    private int lastIndex = -1;
    /**
     * 上次选中的权重
     */
    private int currWeight = 0;
    private int size;

    private void init(List<AbstractLB.Task> tasks) {
        divisor = tasks.get(0).getWight();
        for (AbstractLB.Task task : tasks) {
            if (task.getWight() > maxWeight) {
                maxWeight = task.getWight();
            }
            // 计算最大公约数
            divisor = AlgorithmUtils.gcd(divisor, task.getWight());
        }
        size = tasks.size();
    }

    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
        if (tasks.size() != size) {
            init(tasks);
        }
        while (true) {
            // 轮询所有的资源, 并记住上一次调用的资源下标
            lastIndex = (lastIndex + 1) % tasks.size();
            if (lastIndex == 0) {
                /*
                1. 刚开始轮询
                    currWeight = maxWeight
                2. 当轮询一圈后
                    currWeight = maxWeight - divisor
                    表示上次执行的任务的权重减去公约数后再次计算(也就是已经执行了一个公约数的机会)
                 */
                currWeight = currWeight - divisor;
                if (currWeight <= 0) {
                    currWeight = maxWeight;
                    if (currWeight == 0) {
                        return null;
                    }
                }
            }
            AbstractLB.Task task = tasks.get(lastIndex);
            if (task.getWight() >= currWeight) {
                /*
                 1. 只有当前任务的权重 >= 上次调用的, 才会返回
                 2. 上次调用的权重随着每次的执行会减去一个公约数
                 */
                return task;
            }
        }
    }
}
