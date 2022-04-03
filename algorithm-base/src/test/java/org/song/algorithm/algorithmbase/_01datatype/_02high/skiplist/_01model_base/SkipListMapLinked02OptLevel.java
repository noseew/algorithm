package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import java.util.Objects;

/**
 * 优化构建索引, 效果并不好 TODO
 *
 * @param <K>
 * @param <V>
 */
public class SkipListMapLinked02OptLevel<K extends Comparable<K>, V> extends SkipListMapLinked02OptRemove<K, V> {

    public SkipListMapLinked02OptLevel() {
        super();
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
                    n = newIndex.down;
                    y = header.down;
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
                    if (next.level == newIndex.level) {
                        // 串索引, 新索引在中间
                        newIndex.right = next;
                        x.right = newIndex;
                    }
                    break;
                }
                x = next;
                next = x.right;
            }
            if (next == null && x.level == newIndex.level) {
                x.right = newIndex;
            }
            if (x.level == newIndex.level) {
                // 新索引同时向下
                newIndex = newIndex.down;
            }
            x = x.down;
        }
    }

}
