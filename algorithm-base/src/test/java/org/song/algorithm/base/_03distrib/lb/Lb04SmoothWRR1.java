package org.song.algorithm.base._03distrib.lb;

import lombok.Data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 平滑权重轮询调度
 * 算法摘自 dubbo 的权重轮询
 * NGINX默认的负载均衡算法也是基于此
 */
public class Lb04SmoothWRR1 extends AbstractLB {
    private final ConcurrentMap<String, TaskInfo> taskProgress = new ConcurrentHashMap<>();

    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks) {
        int totalWeight = 0;
        long maxCurrent = Long.MIN_VALUE;
        AbstractLB.Task selected = null;

        for (AbstractLB.Task invoker : tasks) {
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
