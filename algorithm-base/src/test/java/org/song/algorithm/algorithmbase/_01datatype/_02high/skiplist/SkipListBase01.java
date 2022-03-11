package org.song.algorithm.algorithmbase._01datatype._02high.skiplist;

import lombok.*;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_04;

import java.util.Random;

/**
 */
public class SkipListBase01<K extends Comparable<K>, V> {
    
    private HashMap_base_04<K, Node<K, V>> hashMap;

    private Index<K, V> headerIndex;
    private Node<K, V> head, tail;

    private long size;

    /**
     * 索引层从1开始, 第一层也就是数据节点所在, 
     * 数据链表不属于任何层
     */
    private int maxLevel;
    
    private int currentLevel = 1;
    
    private Random r = new Random();
    
    public SkipListBase01() {
        // 临时头索引节点
        headerIndex = new Index<>();
        // 临时头node节点
        headerIndex.node = new Node<>(null, null, -1, null);
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
