package org.song.algorithm.base._01datatype._02high.skiplist._02redis;

import java.util.Objects;

/**
 * 跳表
 * 相比较 SkipListBase01Linked
 * 
 * 1. 更新时, 直接复用原node上的索引, 而不是删除后新增
 * 2. 更新时, 直接更新map中的值, 不需要重新put
 * 
 */
public class SkipListLinked02<K extends Comparable<K>, V> extends SkipListLinked01<K, V> {

    @Override
    public Node<K, V> put(K k, V v, double score) {
        checkMinScorePut(score);
        Node<K, V> exitNode = hashMap.get(k);
        if (exitNode == null) {
            exitNode = Node.<K, V>builder().k(k).v(v).score(score).build();
            hashMap.put(k, exitNode);
            // 不存在
            put(exitNode);
            return null;
        } else {
            // 存在则更新, map直接更新值
            update(exitNode, v, score);
            return exitNode;
        }
    }

    protected void update(Node<K, V> exitNode, V newV, double newScore) {
        // 删除和使用已有的索引
        LinkIndex<K, V> exitIndex = removeIndexAndNode(exitNode.k, exitNode.score);
        if (exitIndex == null) {
        }
        // 删除原节点
        removeNode(exitNode.k, exitNode.score);

        exitNode.v = newV;
        exitNode.score = newScore;
        
        Node<K, V> prev = getPrevNodeByScore(newScore);
        // 串链表
        exitNode.next = prev.next;
        prev.next = exitNode;

        if (exitIndex == null) {
            return;
        }
        // 不需要升索引
        LinkIndex<K, V> y, n;
        y = headerIndex.down;
        for (int level = y.level; level > exitIndex.level; level--) {
            y = y.down;
        }
        n = exitIndex;

        // 串索引
        addIndex(y, n);
    }

    protected LinkIndex<K, V> removeIndexAndNode(K k, double score) {

        LinkIndex<K, V> h = null;

        Node<K, V> prev = null;
        LinkIndex<K, V> x = headerIndex, next;
        while (x != null) { // y轴遍历
            next = x.next;
            while (next != null) { // x轴遍历
                if (next.node.score == score && Objects.equals(k, next.node.k)) {
                    x.next = next.next;

                    if (h == null) h = next; // 记录已有的索引头

                    if (prev == null) prev = x.node;
                } else if (next.node.score < score) {
                    x = next;
                    // 向右跳
                    next = next.next;
                    continue;
                }
                break;
            }
            // 向下
            x = x.down;
        }
        // 删除链表节点
        if (prev != null && prev.next != null) {
            prev.next = prev.next.next;
            indexCount--;
        }

        return h;
    }
}
