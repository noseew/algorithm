package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model;

import lombok.*;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_04;

import java.util.Random;

/**
 */
public class SkipListBase01<K extends Comparable<K>, V> {
    /**
     * map为了O(1)的方式定位到结点, 同时做到结点去重
     */
    private HashMap_base_04<K, Node<K, V>> hashMap;
    /**
     * 跳表的所有的遍历都是从 headerIndex 开始
     * 最高层的索引永远在 headerIndex 中, 并且永远比其他最高索引高1层
     * 索引层的node结点中 分值为-1, 用于标记他是空结点或者头结点
     */
    private Index<K, V> headerIndex;
    /**
     * 为了O(1)的方式方便获取最小和最大节点
     * 同时也可以全量遍历链表
     */
    private Node<K, V> head, tail;

    private long size;

    /**
     * 索引层从1开始
     * 数据链表不属于任何层
     * 默认最大索引层为32层
     */
    private int maxLevel;
    /**
     * 当前索引中的最高level, 
     * 等于headerIndex的level - 1
     */
    private int currentLevel = 1;
    
    private Random r = new Random();
    
    public SkipListBase01() {
        // 临时头索引节点
        headerIndex = new Index<>();
        // 临时头node节点
        headerIndex.node = new Node<>();
        headerIndex.node.score = -1; // 头结点分值最小, 其他分值必须 >= 0

        headerIndex.down = new Index<>();
        headerIndex.down.node = headerIndex.node;
        hashMap = new HashMap_base_04<>(8);
        maxLevel = 32;
    }
    
    public V put(K k, V v, double score) {
        Node<K, V> newNode = new Node<>(k, v, score, null);
        Node<K, V> exitNode = hashMap.get(k);
        if (exitNode == null) {
            // 不存在, 加入map
            hashMap.put(k, newNode);
            size++;
            // 加入跳表

            // 跳索引
            Index<K, V> y = headerIndex, yh = null;
            while (y != null) { // y轴遍历
                Index<K, V> x = y.next, xh = y;
                while (x != null) { // x轴遍历
                    if (x.node.score > score) {
                        // 跳过了, 停止
                        break;
                    }
                    xh = x;
                    // 向右
                    x = x.next;
                }
                yh = y;
                // 向下
                y = xh.down;
            }
            // 到了最底层
            Node<K, V> prev = null, next = null;
            prev = yh.node;

            // 遍历链表
            while (prev != null && prev.score < score) {
                next = prev.next;
                if (next == null) {
                    // 到了链表尾部
                    break;
                }
                if (next.score > score) {
                    break;
                }
                prev = next;
            }

            // 串链表
            newNode.next = prev.next;
            prev.next = newNode;

            // 新建索引
            Index<K, V> newIndex = buildIndex(newNode);
            int level = 0;
            Index<K, V> index = newIndex;
            while (index != null) {
                level++;
                index = index.down;
            }
            // 索引升层
            if (level > currentLevel) {
                // 需要升层数, 但是只升1层
                currentLevel++;
                // 生层同时串最上层索引
                headerIndex.down = new Index<>(newIndex, headerIndex.down.down, headerIndex.node);
            }

            // 串索引, 从下1层开始
            y = headerIndex.down.down;
            Index<K, V> n = newIndex.down; // 从下1层开始
            while (y != null) { // y轴遍历
                Index<K, V> x = y.next, xh = y;
                while (x != null) { // x轴遍历
                    if (x.node.score > score) {
                        // 跳过了, 串索引
                        n.next = x;
                        xh.next = n;
                        break;
                    }
                    xh = x;
                    // 向右
                    x = x.next;
                }
                // 向下
                y = xh.down;
                // 新索引同时向下
                n = n.down;
            }
        } else {
            // 存在则更新
        }
        head = headerIndex.node;
        return null;
    }
    
    protected Index<K, V> buildIndex(Node<K, V> newNode) {
        // 随机层高
        int nextInt = r.nextInt(Integer.MAX_VALUE);
        int level = 1;
        for (int i = 0; i < currentLevel; i++) {
            if ((nextInt & 0B1) != 0B1) {
                break;
            }
            nextInt = nextInt >>> 1;
            level++;
        }
        // 构建索引
        Index<K, V> head = new Index<>(null, null, newNode);
        Index<K, V> down = null;
        for (int i = 0; i < level; i++) {
            if (down == null) {
                down = head;
            }
            down.down = new Index<>(null, null, newNode);
            down = down.down;
        }
        return head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<K, V> hn = head.next;
        // 链表遍历
        while (hn != null) {

            sb.append(hn.k).append("=").append(hn.v);

            Index<K, V> hi = headerIndex;
            // 索引遍历
            hi = nextIndexHead(hi.node);
            if (hi != null && hi.node == hn) {
                while (hi != null) {
                    sb.append("[]");
                    hi = hi.down;
                }
                sb.append("\r\n");
            } else {
                // 链表遍历
                sb.append("\r\n");
                hn = hn.next;
            }
        }
        return sb.toString();
    }

    private Index<K, V> nextIndexHead(Node<K, V> node) {
        if (node == null || node.next == null) {
            return null;
        }
        Node<K, V> nextNode = node.next;

        // 跳索引
        Index<K, V> y = headerIndex;
        while (y != null) { // y轴遍历
            Index<K, V> x = y.next, xh = y;
            while (x != null) { // x轴遍历
                if (x.node == nextNode) {
                    // 停止
                    return x;
                }
                if (x.node.score > nextNode.score) {
                    // 遍历链表
                    nextNode = nextNode.next;
                    continue;
                }

                xh = x;
                // 向右
                x = x.next;
            }
            // 向下
            y = xh.down;
        }
        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class Index<K extends Comparable<K>, V> {
        Index<K, V> next;
        Index<K, V> down;
        Node<K, V> node;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    protected static class Node<K, V> {
        K k;
        V v;
        double score;
        Node<K, V> next;
    }
    
}
