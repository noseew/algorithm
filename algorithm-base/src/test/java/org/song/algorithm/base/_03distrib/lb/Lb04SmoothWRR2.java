package org.song.algorithm.base._03distrib.lb;

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
        n个节点的初始化值为[0, 0, ..., 0], 数组长度为n, 值都为0. 第一轮选择的第1步执行后, 数组的值为[x1, x2, ..., xn]. 
        假设第1步后, 最大的节点为j, 则第j个节点减去S. 
        所以第2步的数组为[x1, x2, ..., xj-S, ..., xn]. 执行完第2步后, 数组的和为
            x1 + x2 + ... + xj-S + ... + xn => x1 + x2 + ... + xn - S = S - S = 0
    由此可见, 每轮选择, 第1步操作都是数组的总和加上S, 第2步总和再减去S, 所以每轮选择完后的数组总和都为0.
    
假设总共执行S轮选择, 记第mi个结点选择次. 第i个结点的当前权重为wi. 
假设节点j在第t轮(t < S)之前, 已经被选择了xj次, 记此时第j个结点的当前权重为
wj = t*xj-xj*S = (t-S)*xj < 0
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 平滑权重轮询调度
 */
public class Lb04SmoothWRR2 extends AbstractLB {
    /**
     * key=任务标识
     * val=任务的行程
     */
    private final Map<String, AtomicInteger> wightMap = new HashMap<>();

    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
        AbstractLB.Task selected = null;
        int maxStep = Integer.MIN_VALUE;
        int totalWight = 0;

        for (AbstractLB.Task task : tasks) {
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