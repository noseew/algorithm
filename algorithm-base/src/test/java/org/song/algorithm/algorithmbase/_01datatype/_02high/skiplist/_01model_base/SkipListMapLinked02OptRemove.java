package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import java.util.Objects;

/**
 * 使用延迟删除优化效率
 *
 * @param <K>
 * @param <V>
 */
public class SkipListMapLinked02OptRemove<K extends Comparable<K>, V> extends SkipListMapLinked<K, V> {

    public SkipListMapLinked02OptRemove() {
        super();
    }

    protected Node<K, V> getNode(K k) {
        Node<K, V> prev = getPrecursorNode(k);
        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode == null) {
                break;
            }
            int cpr = compare(nextNode.k, k);
            if (cpr == 0) {
                return nextNode;
            }
            if (cpr > 0) {
                break;
            }
            prev = nextNode;
        }
        return null;
    }

    protected V putOrUpdate(K k, V v) {
        Node<K, V> prev = getPrecursorNode(k);
        // 跳链表
        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode == null || gather(nextNode.k, k)) {
                Node<K, V> newNode = Node.<K, V>builder().k(k).v(v).next(nextNode).build();
                prev.next = newNode;
                size++;
                // 新增节点
                int level = buildLevel(header.level + 1);
                Index<K, V> newIndex = buildIndex(level, newNode);

                if (level == 0) {
                    // 无需串索引
                    return null;
                }

                Index<K, V> y = header, n = newIndex;
                if (newIndex.level > header.level) {
                    // 需要升索引
                    upHead(newIndex);
                    y = header.down;
                    n = newIndex.down;
                } else {
                    for (int l = y.level; l > newIndex.level; l--) {
                        y = y.down;
                    }
                }
                // 串索引
                addIndex(y, n);
                break;
            } else if (Objects.equals(nextNode.k, k)) {
                // 相等则更新
                V oldV = nextNode.v;
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
                if (next.node.k == null) {
                    // 均摊复杂度删除索引
                    x.right = next.right;
                    next = x.right;
                    continue;
                }
                if (gather(next.node.k, newIndex.node.k)) {
                    // 跳过了, 串索引, 新索引在中间
                    newIndex.right = next;
                    x.right = newIndex;
                    break;
                }
                x = next;
                next = x.right;
            }
            if (next == null) {
                x.right = newIndex;
            }
            x = x.down;
            // 新索引同时向下
            newIndex = newIndex.down;
        }
    }

    protected V doRemove(K k) {
        Node<K, V> prev = getPrecursorNode(k);
        // 跳链表
        while (prev != null) {
            Node<K, V> nextNode = prev.next;
            if (nextNode != null && nextNode.k != null) {
                int cpr = compare(nextNode.k, k);
                if (cpr < 0) {
                    prev = nextNode;
                    continue;
                }
                if (cpr == 0) {
                    size--;
                    V oldV = nextNode.v;
                    nextNode.k = null;
                    nextNode.v = null;
                    prev.next = nextNode.next;
                    return oldV;
                }
                break;
            }
            prev = nextNode;
        }
        return null;
    }

    protected Node<K, V> getPrecursorNode(K k) {
        Index<K, V> x = header, next;
        while (x != null) { // y轴遍历
            next = x.right;
            while (next != null) { // x轴遍历
                if (next.node.k == null) {
                    // 均摊复杂度删除索引
                    x.right = next.right;
                    next = x.right;
                    continue;
                }
                int cpr = compare(next.node.k, k);
                if (cpr == 0) {
                    return x.node;
                } else if (cpr < 0) {
                    x = next;
                    next = next.right;
                    continue;
                }
                break;
            }
            // 向下
            if (x.down == null) {
                return x.node;
            }
            x = x.down;
        }
        return x.node;
    }
}
