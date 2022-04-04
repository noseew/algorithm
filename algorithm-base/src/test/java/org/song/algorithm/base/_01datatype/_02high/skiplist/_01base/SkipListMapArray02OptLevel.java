package org.song.algorithm.base._01datatype._02high.skiplist._01base;

/**
 *
 * @param <K>
 * @param <V>
 */
public class SkipListMapArray02OptLevel<K extends Comparable<K>, V> extends SkipListMapArray<K, V> {

    public SkipListMapArray02OptLevel() {
        super();
    }

    protected V putOrUpdate(K k, V v) {
        Node<K, V> x = this.header, next;
        for (int i = currentLevel - 1; i >= 0; i--) { // y轴遍历
            next = x.levels[i].next;
            while (next != null) {  // x轴遍历
                int cpr = compare(next.k, k);
                if (cpr < 0) { // 向右跳
                    x = next;
                    next = next.levels[i].next;
                    continue;
                } else if (cpr == 0) { // 相等则更新
                    V oldV = next.v;
                    next.v = v;
                    return oldV;
                }
                break; // 跳过了 终止
            }
        }
        
        if (x == null) return null;

        Node<K, V> nextNode = x.levels[0].next;
        if (nextNode == null || gather(nextNode.k, k)) {
            // 新增节点
            Node<K, V> newNode = Node.<K, V>builder().k(k).v(v).build();
            size++;
            int level = buildLevel(currentLevel + 1);
            level = Math.max(1, level);
            buildIndex(level, newNode);
            // 串链表
            x.levels[0].next = newNode;
            newNode.levels[0].next = nextNode;

            // 无需串索引
            if (level == 1) return null;

            int startIndex = newNode.levels.length - 1;
            if (newNode.levels.length > currentLevel) {
                // 需要升索引
                upHead(newNode);
                startIndex = newNode.levels.length - 2;
            }
            // 串索引
            addIndex(startIndex, newNode);
        }
        return null;
    }

    /**
     * 采用跳索引的方式定位并串索引
     * 
     * @param startIndex
     * @param newNode
     */
    protected void addIndex(int startIndex, Node<K, V> newNode) {
        Node<K, V> x = this.header, next;
        for (int i = currentLevel; i > 0; i--) { // y轴遍历
            next = x.levels[i].next;
            while (next != null) {  // x轴遍历
                int cpr = compare(next.k, newNode.k);
                if (cpr < 0) {
                    // 向右跳
                    x = next;
                    next = next.levels[i].next;
                    continue;
                } else if (i <= startIndex) {
                    // 添加新索引
                    newNode.levels[i].next = next;
                    x.levels[i].next = newNode;
                }
                break;
            }
            if (next == null && less(x.k, newNode.k) && i <= startIndex) {
                // 添加新索引
                x.levels[i].next = newNode;
            }
        }
    }
}
