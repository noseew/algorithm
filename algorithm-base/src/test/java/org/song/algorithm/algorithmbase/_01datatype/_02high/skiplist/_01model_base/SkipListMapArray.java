package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 使用跳表实现 map, 区别于 JDK 跳表, 这里的索引采用数组实现
 *
 * @param <K>
 * @param <V>
 */
public class SkipListMapArray<K extends Comparable<K>, V> extends AbstractSkipListMap<K, V> {
    /**
     * 头临时节点
     * k=null
     */
    protected Node<K, V> header;
    protected int currentLevel = 1;

    public SkipListMapArray(int indexStrategy) {
        Node<K, V> newNode = Node.<K, V>builder().build();
        buildIndex(maxLevel, newNode);
        header = newNode;
        this.indexStrategy = indexStrategy;
    }

    public SkipListMapArray() {
        Node<K, V> newNode = Node.<K, V>builder().build();
        buildIndex(maxLevel, newNode);
        header = newNode;
    }

    @Override
    public V put(K k, V v) {
        return putOrUpdate(k, v);
    }

    @Override
    public V get(K k) {
        Node<K, V> node = getNode(k);
        if (node != null) {
            return node.v;
        }
        return null;
    }

    @Override
    public V remove(K k) {
        return doRemove(k);
    }

    @Override
    public void clean() {
        currentLevel = 1;
        for (Index<K, V> level : header.levels) {
            level.next = null;
        }
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    protected void buildIndex(int level, Node<K, V> newNode) {
        // 构建索引
        Index<K, V>[] levels = new Index[level];
        for (int i = 0; i < level; i++) {
            levels[i] = new Index<>();
        }
        newNode.ic = level;
        newNode.levels = levels;
    }

    protected Node<K, V> getNode(K k) {
        Node<K, V> x = this.header, next;
        for (int i = currentLevel - 1; i >= 0; i--) { // y轴遍历
            next = x.levels[i].next;
            while (next != null) { // x轴遍历
                if (less(next.k, k)) { // 向右跳
                    x = next;
                    next = next.levels[i].next;
                    continue;
                } else if (Objects.equals(next.k, k)) { // 相等则返回
                    return next;
                }
                // 跳过了 终止
                break;
            }
        }
        return null;
    }

    protected V putOrUpdate(K k, V v) {
        Node<K, V> x = this.header, next;
        V oldV = null;
        for (int i = currentLevel - 1; i >= 0; i--) { // y轴遍历
            next = x.levels[i].next;
            while (next != null) {  // x轴遍历
                if (less(next.k, k)) { // 向右跳
                    x = next;
                    next = next.levels[i].next;
                    continue;
                } else if (Objects.equals(next.k, k)) { // 相等则更新
                    oldV = next.v;
                    next.v = v;
                    return oldV;
                }
                break; // 跳过了 终止
            }
        }
        Node<K, V> prev = x;
        // 跳链表
        while (prev != null) {
            Node<K, V> nextNode = prev.levels[0].next;
            if (nextNode == null || gather(nextNode.k, k)) {
                Node<K, V> newNode = Node.<K, V>builder().k(k).v(v).build();
                size++;
                // 新增节点
                int level = buildLevel(currentLevel + 1);
                level = Math.max(1, level);
                buildIndex(level, newNode);
                // 串链表
                prev.levels[0].next = newNode;
                newNode.levels[0].next = nextNode;

                if (level == 1) {
                    // 无需串索引
                    return null;
                }

                int startIndex = newNode.levels.length - 1;
                if (newNode.levels.length > currentLevel) {
                    // 需要升索引
                    upHead(newNode);
                    startIndex = newNode.levels.length - 2;
                }
                // 串索引
                addIndex(startIndex, newNode);
                break;
            } else if (Objects.equals(nextNode.k, k)) {
                // 相等则更新
                oldV = nextNode.v;
                nextNode.v = v;
                return oldV;
            }
            prev = nextNode;
        }
        return null;
    }

    protected void addIndex(int startIndex, Node<K, V> newNode) {
        Node<K, V> x = this.header, next;
        for (int i = startIndex; i > 0; i--) { // y轴遍历
            next = x.levels[i].next;
            while (next != null) {  // x轴遍历
                if (less(next.k, newNode.k)) {
                    // 向右跳
                    x = next;
                    next = next.levels[i].next;
                    continue;
                } else if (i < newNode.levels.length) {
                    // 添加新索引
                    newNode.levels[i].next = next;
                    x.levels[i].next = newNode;
                }
                break;
            }
            if (next == null && less(x.k, newNode.k) && i < newNode.levels.length) {
                // 添加新索引
                x.levels[i].next = newNode;
            }
        }
    }

    protected V doRemove(K k) {
        Node<K, V> x = this.header, next;
        V oldV = null;
        for (int i = currentLevel - 1; i >= 0; i--) { // y轴遍历
            next = x.levels[i].next;
            while (next != null) {  // x轴遍历
                if (Objects.equals(next.k, k)) {
                    // 相等则更新
                    if (oldV == null) oldV = next.v;
                    // 删索引
                    x.levels[i].next = next.levels[i].next;
                } else if (less(next.k, k)) {
                    x = next;
                    // 向右跳
                    next = next.levels[i].next;
                    continue;
                }
                // 跳过了 终止
                break;
            }
        }
        return oldV;
    }

    protected void upHead(Node<K, V> newNode) {
        header.levels[newNode.levels.length - 1].next = newNode;
        currentLevel++;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(this.size())
                .append("\r\n");

        Node<K, V> hi = header;
        Node<K, V> hn = hi;

        int count = 0;
        while (hn != null) {
            // 链表遍历
            sb.append(count++).append(". ")
                    .append(" ic=").append(hn.ic)
                    .append("\t")
                    .append("{").append(wrap(hn.k)).append(":").append(wrap(hn.v)).append("} ");
            if (hi != null && hi == hn) {
                // 索引遍历
                reversed(hi, sb);
                // 下一个索引头
                hi = nextIndexHead(hi);
            }
            sb.append("\r\n");
            hn = hn.levels[0].next;
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

    private void reversed(Node<K, V> index, StringBuilder sb) {
        if (index == null) {
            return;
        }
        sb.append("<-[");
        for (int i = 2; i <= index.levels.length && i <= currentLevel; i++) {
            sb.append(i);
            if (i < index.levels.length) sb.append(", ");
        }
        sb.append("]");
    }

    /**
     * 获取下一个索引的头索引节点
     *
     * @param hi
     * @return
     */
    private Node<K, V> nextIndexHead(Node<K, V> hi) {
        if (hi == null) {
            return null;
        }
        return hi.levels[1].next;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Index<K, V> {
        Node<K, V> next;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Node<K, V> {
        K k;
        V v;
        Index<K, V>[] levels;
        int ic; // 用于debug调试, 拥有索引层数

        @Override
        public String toString() {
            return "(key=" + k + ",val=" + v + ")";
        }
    }
}
