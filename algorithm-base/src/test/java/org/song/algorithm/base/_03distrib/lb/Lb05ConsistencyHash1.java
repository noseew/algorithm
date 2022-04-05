package org.song.algorithm.base._03distrib.lb;

    /*
    0___64___128___192___256
    Node01 [0-63]
    Node02 [64-127]
    Node02 [128-191]
    Node02 [182-256]
     */

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 一致性 Hash 调度
 */
public class Lb05ConsistencyHash1 extends AbstractLB {
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
    private void ensure(List<AbstractLB.Task> tasks) {
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
    private synchronized void initRing(List<AbstractLB.Task> tasks) {
        if (hashRingRange != null) {
            return; // DCL
        }
        hashRingRange = new TreeMap<>();
        int step = range / tasks.size();
        int start = 0;
        for (AbstractLB.Task task : tasks) {
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
    public synchronized void adjustNode(List<AbstractLB.Task> tasks) {
        if (hashRingRange.size() == tasks.size()) {
            return; // DCL
        }
        Set<String> newTaskSet = tasks.stream().map(AbstractLB.Task::getName).collect(Collectors.toSet());
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
    public AbstractLB.Task select(List<AbstractLB.Task> tasks, Object param) {
        ensure(tasks);
        int i = System.identityHashCode(param) % range;
        Node node = hashRingRange.floorEntry(i).getValue();
        for (AbstractLB.Task task : tasks) {
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