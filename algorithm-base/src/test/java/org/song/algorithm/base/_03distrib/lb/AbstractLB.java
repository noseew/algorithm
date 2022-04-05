package org.song.algorithm.base._03distrib.lb;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.assertj.core.util.Lists;

import java.util.List;

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
public abstract class AbstractLB {

    protected Task select(List<Task> tasks) {
        return null;
    }
    protected Task select(List<Task> tasks, Object param) {
        return select(tasks);
    }

    @Data
    @AllArgsConstructor
    protected static class Task {
        private String name;
        private int wight;
        void invoke(Object o) {System.out.println(this.name +" "+ o);}
    }
}
