package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_redis.AbstractSkipList;
import org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_redis.SkipListLinked01;

import java.util.Objects;

/**
 * 使用跳表实现 map, 区别于 JDK 跳表
 *
 * @param <K>
 * @param <V>
 */
public class SkipListMapLinked<K extends Comparable<K>, V> extends AbstractSkipListMap<K, V> {
    /**
     * 头临时节点
     * k=null
     */
    protected Index<K, V> header;

    public SkipListMapLinked() {
        Node<K, V> newNode = Node.<K, V>builder().build();
        header = buildIndex(1, newNode);
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
        Node<K, V> newNode = Node.<K, V>builder().build();
        header = buildIndex(1, newNode);
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

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
        if (head != null) {
            newNode.ic = head.level;
        }
        return head;
    }

    protected Node<K, V> getNode(K k) {
        Index<K, V> x = header, next;
        while (x != null) { // y轴遍历
            next = x.right;
            while (next != null) { // x轴遍历
                if (Objects.equals(next.node.k, k)) {
                    return next.node;
                } else if (less(next.node.k, k)) {
                    x = next;
                    // 向右跳
                    next = next.right;
                    continue;
                }
                // 跳过了 终止
                break;
            }
            // 向下
            if (x.down != null) {
                x = x.down;
            } else {
                break;
            }
        }
        Node<K, V> prev = x.node;
        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode != null && Objects.equals(nextNode.k, k)) {
                return nextNode;
            }
            prev = nextNode;
        }
        return null;
    }

    protected V putOrUpdate(K k, V v) {
        Index<K, V> x = this.header, next;
        V oldV = null;
        while (x != null) { // y轴遍历
            next = x.right;
            while (next != null) { // x轴遍历
                if (Objects.equals(next.node.k, k)) {
                    oldV = next.node.v;
                    next.node.v = v;
                    return oldV;
                } else if (less(next.node.k, k)) {
                    x = next;
                    // 向右跳
                    next = next.right;
                    continue;
                }
                // 跳过了 终止
                break;
            }
            // 向下
            if (x.down != null) {
                x = x.down;
            } else {
                break;
            }
        }
        Node<K, V> prev = x.node;
        // 跳链表
        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode == null || gather(nextNode.k, k)) {
                Node<K, V> newNode = Node.<K, V>builder().k(k).v(v).build();
                size++;
                // 新增节点
                int level = buildLevel(header.level + 1);
                Index<K, V> newIndex = buildIndex(level, newNode);
                // 串链表
                prev.next = newNode;
                newNode.next = nextNode;

                if (level == 0) {
                    // 无需串索引
                    return null;
                }

                Index<K, V> y, n;
                if (newIndex.level > header.level) {
                    // 需要升索引
                    upHead(newIndex);
                    y = header.down;
                    n = newIndex.down;
                } else {
                    y = header;
                    for (int l = y.level; l > newIndex.level; l--) {
                        y = y.down;
                    }
                    n = newIndex;
                }
                // 串索引
                addIndex(y, n);
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

    protected void addIndex(Index<K, V> indexHead, Index<K, V> newIndex) {
        Index<K, V> x = indexHead, next;
        while (x != null) { // y轴遍历
            next = x.right;
            while (next != null) { // x轴遍历
                if (gather(next.node.k, newIndex.node.k)) {
                    // 跳过了, 串索引, 新索引在中间
                    newIndex.right = next;
                    x.right = newIndex;
                    break;
                }
                x = next;
                // 向右
                next = next.right;
            }
            if (next == null) {
                // 跳过了, 串索引, 新索引在右边
                x.right = newIndex;
            }
            // 向下
            x = x.down;
            // 新索引同时向下
            newIndex = newIndex.down;
        }
    }

    protected V doRemove(K k) {
        Index<K, V> x = this.header, next;
        V oldV = null;
        while (x != null) { // y轴遍历
            next = x.right;
            while (next != null) { // x轴遍历
                if (Objects.equals(next.node.k, k)) {
                    x.right = next.right;
                } else if (less(next.node.k, k)) {
                    x = next;
                    // 向右跳
                    next = next.right;
                    continue;
                }
                // 跳过了 终止
                break;
            }
            // 向下
            if (x.down != null) {
                x = x.down;
            } else {
                break;
            }
        }

        Node<K, V> prev = x.node;
        // 跳链表
        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode != null && Objects.equals(nextNode.k, k)) {
                size--;
                oldV = nextNode.v;
                prev.next = nextNode.next;
                return oldV;
            }
            prev = nextNode;
        }
        return null;
    }

    protected void upHead(Index<K, V> newIndex) {
        Index<K, V> index = new Index<>();
        index.right = newIndex;
        index.down = header;
        index.node = header.node;
        index.level = header.level + 1;
        header = index;
        header.node.ic = header.level;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(this.size())
                .append("\r\n");

        Index<K, V> hi = header;
        Node<K, V> hn = hi.node;

        int count = 0;
        while (hn != null) {
            // 链表遍历
            sb.append(count++).append(". ")
                    .append(" ic=").append(hn.ic)
                    .append("\t")
                    .append("{").append(wrap(hn.k)).append(":").append(wrap(hn.v)).append("} ");
            if (hi != null && hi.node == hn) {
                // 索引遍历
                reversed(hi, sb);
                // 下一个索引头
                hi = nextIndexHead(hn);
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
        Index<K, V> oneLevel = header;
        while (oneLevel != null && oneLevel.level > 1) oneLevel = oneLevel.down;
        // 找到下一个索引的第1层
        Node<K, V> nextIndexNode = null;
        while (oneLevel != null) {
            if (gather(oneLevel.node.k, node.k)) {
                nextIndexNode = oneLevel.node;
                break;
            }
            oneLevel = oneLevel.right;
        }
        // 如果接下来没有索引, 返回空
        if (nextIndexNode == null) {
            return null;
        }

        // 跳索引, 跳到下一个索引的索引头
        Index<K, V> x = header;
        while (x != null) { // y轴遍历
            Index<K, V> next = x.right;
            while (next != null) { // x轴遍历
                if (next.node == nextIndexNode) {
                    return next; // 找到了
                } else if (gather(next.node.k, nextIndexNode.k)) {
                    break; // 超过了
                }
                // 向右
                next = next.right;
            }
            // 向下
            x = x.down;
        }
        return null;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Index<K, V> {
        Index<K, V> right;
        Index<K, V> down;
        Node<K, V> node;
        int level;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Node<K, V> {
        K k;
        V v;
        Node<K, V> next;
        int ic; // 用于debug调试, 拥有索引层数

        @Override
        public String toString() {
            return "(key=" + k + ",val=" + v + ")";
        }
    }
}
