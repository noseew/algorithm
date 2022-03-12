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
    private HashMap_base_04<K, Node<K, V>> hashMap = new HashMap_base_04<>(8);
    /**
     * 跳表的所有的遍历都是从 headerIndex 开始
     * 最高层的索引永远在 headerIndex 中, 并且永远比其他最高索引高1层
     * 最底层索引层从开始
     * 索引层的node结点中 分值为-1, 用于标记他是空结点或者头结点
     */
    private Index<K, V> headerIndex;
    /**
     * 为了O(1)的方式方便获取最小和最大节点
     * 同时也可以全量遍历链表
     */
//    private Node<K, V> head, tail;

    private long size;

    /**
     * 索引层从1开始
     * 数据链表不属于任何层
     * 默认最大索引层为32层
     */
    private int maxLevel = 32;
    /**
     * 当前索引中的最高level, 
     * 等于headerIndex的level - 1
     */
    
    private Random r = new Random();
    
    public SkipListBase01() {
        // 临时头node节点
        Node<K, V> node = new Node<>();
        node.score = -1; // 头结点分值最小, 其他分值必须 >= 0
        // 临时头索引节点, 初始1层
        headerIndex = buildIndex(1, node);
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
            Index<K, V> newIndex = buildIndex(buildLevel(), newNode);

            if (newIndex != null) {
                Index<K, V> n;
                // 索引升层
                if (newIndex.level >= headerIndex.level) {
                    // 生层同时串最上层索引
                    headerIndex = new Index<>(null, headerIndex, headerIndex.node, headerIndex.level + 1);
                    headerIndex.down.next = newIndex;

                    // 从下1层开始, 因为上一层索引已经关联, 由于headerIndex比其他都高1层, 所以这里是下2层
                    y = headerIndex.down.down;
                    // 新的 需要生层的索引, 最高层因为是新的, 这里已经关联, 所以需要从下面层开始
                    n = newIndex.down;
                } else {
                    // head索引下降到和新索引相同的高度在进行串索引
                    y = headerIndex.down;
                    for (int level = y.level; level > newIndex.level; level--) {
                        // head 索引下跳到和新索引相同层
                        y = y.down;
                    }
                    n = newIndex;
                }

                // 串索引
                while (y != null) { // y轴遍历
                    Index<K, V> x = y.next, xh = y;
                    while (x != null) { // x轴遍历
                        if (x.node.score > score) {
                            // 跳过了, 串索引, 新索引在中间
                            n.next = x;
                            xh.next = n;
                            break;
                        }
                        xh = x;
                        // 向右
                        x = x.next;
                    }
                    if (x == null) {
                        // 跳过了, 串索引, 新索引在右边
                        xh.next = n;
                    }
                    
                    // 向下
                    y = xh.down;
                    // 新索引同时向下
                    n = n.down;
                }
            }
        } else {
            // 存在则更新
        }
        return null;
    }

    /**
     * 随机获取层数, 从1开始, 最低0层, 表示不构建索引, 最高层数 == headerIndex 的层数
     * 有0.5的概率不会生成索引
     * 从低位开始, 连续1的个数就是索引的层数
     * 
     * @return
     */
    protected int buildLevel() {
        // 随机层高
        int nextInt = r.nextInt(Integer.MAX_VALUE);
        int level = 0;
        // 最高层数 == headerIndex 的层数
        for (int i = 0; i < headerIndex.level; i++) {
            if ((nextInt & 0B1) != 0B1) break;
            nextInt = nextInt >>> 1;
            level++;
        }
        return level;
    }

    /**
     * 构建索引, 并返回索引头
     * 
     * @param level 需要生成多少层索引
     * @param newNode 索引关联的node
     * @return
     */
    protected Index<K, V> buildIndex(int level, Node<K, V> newNode) {
        // 构建索引
        Index<K, V> head = null;
        for (int i = 1; i <= level; i++) {
            Index<K, V> down = head;
            head = new Index<>();
            head.node = newNode;
            head.level = i;

            head.down = down;
        }
        return head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(size).append("\r\n");
        Index<K, V> hi = headerIndex;
        Node<K, V> hn = headerIndex.node;
        
        int count = 0;
        while (hn != null) {
            // 链表遍历
            sb.append(count++).append(": ").append(hn.score).append(": ")
                    .append("(").append(wrap(hn.k)).append("=").append(wrap(hn.v)).append(") ");
            if (hi != null && hi.node == hn) {
                // 索引遍历, 逆序
                reversed(hi, sb);
                // 下一个索引头
                hi = nextIndexHead(hi.node);
            }
            sb.append("\r\n");
            hn = hn.next;
        }
        return sb.toString();
    }
    
    private String wrap(Object o) {
        String s = String.valueOf(o == null ? "" : o);
        StringBuilder sb = new StringBuilder();
        int max = 4;
        for (int i = 0; i < max - s.length(); i++) {
            sb.append(" ");
        }
        return sb.append(s).toString();
    }
    
    private void reversed(Index<K, V> index, StringBuilder sb) {
        if (index == null) {
            return;
        }
        reversed(index.down, sb);
        sb.append("<-[").append(index.level).append("]");
    }

    /**
     * 获取下一个索引的头索引节点
     * 
     * @param node
     * @return
     */
    private Index<K, V> nextIndexHead(Node<K, V> node) {
        if (node == null || node.next == null) {
            return null;
        }
        // 从第1层开始
        Index<K, V> oneLevel = headerIndex;
        while (oneLevel != null && oneLevel.level > 1) oneLevel = oneLevel.down;
        // 找到下一个索引的第1层
        Node<K, V> nextIndexNode = null;
        while (oneLevel != null) {
            if (oneLevel.node.score >= node.score && node != oneLevel.node) {
                nextIndexNode = oneLevel.node;
                break;
            }
            oneLevel = oneLevel.next;
        }
        // 如果接下来没有索引, 返回空
        if (nextIndexNode == null) {
            return null;
        }
        
        // 跳索引, 跳到下一个索引的索引头
        Index<K, V> y = headerIndex;
        while (y != null) { // y轴遍历
            Index<K, V> x = y.next;
            while (x != null) { // x轴遍历
                if (x.node == nextIndexNode) {
                    // 停止
                    return x;
                } else if (x.node.score < nextIndexNode.score) {
                    // 向右
                    x = x.next;
                } else {
                    break;
                }
            }
            // 向下
            y = y.down;
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
        int level;
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
