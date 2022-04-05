package org.song.algorithm.base._03distrib.lb;


import java.util.List;

/**
 * Hash 调度
 */
public class Lb05Hash1 extends AbstractLB {
    @Override
    public AbstractLB.Task select(List<AbstractLB.Task> tasks, Object param) {
        int i = param.hashCode() % tasks.size();
        return tasks.get(i);
    }
}