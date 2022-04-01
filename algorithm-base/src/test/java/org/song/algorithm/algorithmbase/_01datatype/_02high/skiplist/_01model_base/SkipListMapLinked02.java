package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import lombok.Data;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 代码参考自 ConcurrentSkipListMap
 *
 * @param <K>
 * @param <V>
 */
public class SkipListMapLinked02<K extends Comparable<K>, V> extends AbstractSkipListMap<K, V>  {

    private static final Object BASE_HEADER = new Object();

    private transient volatile Index<K, V> head;

    final Comparator<? super K> comparator;

    private void initialize() {
        head = new Index<K, V>(new Node<K, V>(null, BASE_HEADER, null),
                null, null, 1);
    }

    private boolean casHead(Index<K, V> cmp, Index<K, V> val) {
        this.head = val;
        return true;
    }

    final int cpr(Comparator c, K x, K y) {
        return x.compareTo(y);
    }

    final int cpr(K x, K y) {
        return x.compareTo(y);
    }

    private Node<K, V> findPredecessor(K key) {
        if (key == null)
            throw new NullPointerException(); // don't postpone errors

        /*
          q, r, d 表示索引层的三个节点,
          使用该三个节点不断替换跳跃从而定位到索引层能够定位到的数据节点的大致范围
          q: 当前索引节点
          r: 右侧索引节点
          d: 下一层节点
         */
        for (Index<K, V> q = head, r = q.right, d; ; ) {
            if (r != null) {
                if (cpr(key, r.node.key) > 0) {
                    r = r.right;
                    continue;
                }
            }
            if ((d = q.down) == null) {
                return q.node;
            }
            q = d;
            r = d.right;
        }
    }

    private V doGet(K key) {
        if (key == null)
            throw new NullPointerException();
        Comparator<? super K> cmp = comparator;
        for (Node<K, V> b = findPredecessor(key), n = b.next; n != null; ) {
            if (cpr(cmp, key, n.key) == 0) {
                return (V) n.value;
            }
            n = n.next;
        }
        return null;
    }

    private V doPut(K key, V value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node<K, V> z = null;             // added node
        Comparator<? super K> cmp = comparator;
        for (Node<K, V> b = findPredecessor(key), // 需要插入点, 需要找到节点的前驱节点
             n = b.next; // 元素需要插入在 [b, n] 之间
                ; ) {
            if (n != null) {
                int c;
                if ((c = cpr(cmp, key, n.key)) > 0) {
                    b = n;
                    n = n.next;
                    continue;
                }
                if (c == 0) {
                    Object oldV = n.value;
                    n.value = value;
                    return (V) oldV;
                }
            }

            // 本次需要添加的节点
            z = new Node<K, V>(key, value, n);
            b.next = z;
            break;
        }

        // 开始为节点添加索引层, rnd 范围int全值(正负)
        int rnd = ThreadLocalRandom.current().nextInt();
        if ((rnd & 0x80000001) == 0) {
            int level = 1, max = head.level; // head最高索引高度
            while (((rnd >>>= 1) & 1) != 0) {
                ++level; // 本次索引高度
            }
            Index<K, V> idx = null;
            Index<K, V> h = head;

            if (level <= max) {
                for (int i = 1; i <= level; ++i) {
                    idx = new Index<K, V>(z, idx, null, i);
                }
                z.ic = level;
            } else {
                level = max + 1;
                z.ic = level;
                Index<K, V>[] idxs = (Index<K, V>[]) new Index<?, ?>[level + 1];
                for (int i = 1; i <= level; ++i) {
                    // 循环创建索引层级, 将其串成链表
                    idxs[i] = idx = new Index<K, V>(z, idx, null, i);
                }
                // 跳跃表原最大高度
                int oldLevel = h.level;
                if (level > oldLevel) {
                    Index<K, V> newh = h;
                    Node<K, V> oldbase = h.node;
                    for (int j = oldLevel + 1; j <= level; ++j) {
                        newh = new Index<K, V>(oldbase, newh, idxs[j], j);
                    }
                    this.head = newh;
                    h = newh;
                    idx = idxs[level = oldLevel];
                }
            }
            
            // 串索引
            int insertionLevel = level;
                int j = h.level;
                for (Index<K, V> q = h, right = q.right, t = idx; ; ) {
                    if (q == null || t == null){
                        return null;
                    }
                    if (right != null) {
                        Node<K, V> n = right.node;
                        int c = cpr(key, n.key);
                        if (n.value == null) {
                            q.right = right.right;
                            right = q.right;
                            continue;
                        }
                        if (c > 0) {
                            q = right;
                            right = right.right;
                            continue;
                        }
                    }

                    if (j == insertionLevel) {
                        t.right = right;
                        q.right = t;
                        --insertionLevel;
                    }

                    if (--j >= insertionLevel && j < level) {
                        t = t.down;
                    }
                    q = q.down;
                    if (q == null) {
                        break;
                    }
                    right = q.right;
                }
        }
        return null;
    }

    final V doRemove(K key, Object value) {
        if (key == null)
            throw new NullPointerException();
        for (Node<K, V> b = findPredecessor(key), n = b.next; ; ) {
            if (n == null) {
                return null;
            }
            Object v = n.value;
            int c = cpr(key, n.key);
            Node<K, V> f = n.next;
            if (c > 0) {
                b = n;
                n = f;
                continue;
            }
            n.value = null;
            n.next = new Node<K, V>(f);
            b.next = f;
            if (head.right == null) tryReduceLevel();
            return (V) v;
        }
    }

    private void tryReduceLevel() {
        Index<K, V> h = head;
        Index<K, V> d;
        Index<K, V> e;
        if (h.level > 3 &&
                (d = (Index<K, V>) h.down) != null &&
                (e = (Index<K, V>) d.down) != null &&
                e.right == null &&
                d.right == null &&
                h.right == null &&
                casHead(h, d) && // try to set
                h.right != null) // recheck
            casHead(d, h);   // try to backout
    }

    final Node<K, V> findFirst() {
        for (Node<K, V> b, n; ; ) {
            if ((n = (b = head.node).next) == null)
                return null;
            if (n.value != null)
                return n;
            n.helpDelete(b, n.next);
        }
    }

    final Node<K, V> findLast() {
        Index<K, V> q = head;
        for (; ; ) {
            Index<K, V> d, r;
            if ((r = q.right) != null) {
                if (r.indexesDeletedNode()) {
                    q.right = r.right;
                    q = head; // restart
                } else
                    q = r;
            } else if ((d = q.down) != null) {
                q = d;
            } else {
                for (Node<K, V> b = q.node, n = b.next; ; ) {
                    if (n == null)
                        return b.isBaseHeader() ? null : b;
                    Node<K, V> f = n.next;            // inconsistent read
                    if (n != b.next)
                        break;
                    Object v = n.value;
                    if (v == null) {                 // n is deleted
                        n.helpDelete(b, f);
                        break;
                    }
                    if (b.value == null || v == n)      // b is deleted
                        break;
                    b = n;
                    n = f;
                }
                q = head; // restart
            }
        }
    }

    public SkipListMapLinked02() {
        this.comparator = null;
        initialize();
    }

    public SkipListMapLinked02(Comparator<? super K> comparator) {
        this.comparator = comparator;
        initialize();
    }

    @Override
    public V get(K key) {
        return doGet(key);
    }

    @Override
    public V put(K key, V value) {
        if (value == null) {
            throw new NullPointerException();
        }
        return doPut(key, value);
    }

    @Override
    public void clean() {

    }

    @Override
    public V remove(K key) {
        return doRemove(key, null);
    }

    public int size() {
        long count = 0;
        for (Node<K, V> n = findFirst(); n != null; n = n.next) {
            if (n.getValidValue() != null)
                ++count;
        }
        return (count >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) count;
    }

    public boolean isEmpty() {
        return findFirst() == null;
    }

    public void clear() {
        initialize();
    }

    public boolean remove(K key, Object value) {
        if (key == null)
            throw new NullPointerException();
        return value != null && doRemove(key, value) != null;
    }

    public K firstKey() {
        Node<K, V> n = findFirst();
        if (n == null)
            throw new NoSuchElementException();
        return n.key;
    }

    public K lastKey() {
        Node<K, V> n = findLast();
        if (n == null)
            throw new NoSuchElementException();
        return n.key;
    }

    @Data
    static final class Node<K, V> {
        final K key;
        Object value;
        Node<K, V> next;
        int ic;

        Node(K key, Object value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        Node(Node<K, V> next) {
            this.key = null;
            this.value = this;
            this.next = next;
        }

        boolean isBaseHeader() {
            return value == BASE_HEADER;
        }

        void helpDelete(Node<K, V> b, Node<K, V> f) {
            if (f == next && this == b.next) {
                if (f == null || f.value != f) { // not already marked
                    next = new Node<K, V>(f);
                } else {
                    b.next = f.next;
                }
            }
        }

        V getValidValue() {
            Object v = value;
            if (v == this || v == BASE_HEADER)
                return null;
            return (V) v;
        }
    }

    @Data
    static class Index<K, V> {
        final Node<K, V> node;
        final Index<K, V> down;
        Index<K, V> right;
        int level;

        Index(Node<K, V> node, Index<K, V> down, Index<K, V> right) {
            this.node = node;
            this.down = down;
            this.right = right;
        }
        Index(Node<K, V> node, Index<K, V> down, Index<K, V> right, int level) {
            this.node = node;
            this.down = down;
            this.right = right;
            this.level = level;
        }

        final boolean indexesDeletedNode() {
            return node.value == null;
        }

    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(this.size())
                .append("\r\n");

        Index<K, V> hi = head;
        Node<K, V> hn = hi.node;

        int count = 0;
        while (hn != null) {
            // 链表遍历
            sb.append(count++).append(". ")
                    .append(" ic=").append(hn.ic)
                    .append("\t")
                    .append("{").append(wrap(hn.key)).append(":").append(wrap(hn.value)).append("} ");
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
        String s = String.valueOf(o == null || BASE_HEADER == o ? "" : o );
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
        Index<K, V> oneLevel = head;
        while (oneLevel != null && oneLevel.level > 1) oneLevel = (Index<K, V>) oneLevel.down;
        Node<K, V> nextIndexNode = null;
        Index<K, V> oneLevelIndex = oneLevel;
        while (oneLevelIndex != null) {
            if (gather(oneLevelIndex.node.key, node.key)) {
                nextIndexNode = oneLevelIndex.node;
                break;
            }
            oneLevelIndex = oneLevelIndex.right;
        }
        if (nextIndexNode == null) {
            return null;
        }
        Index<K, V> x = head;
        while (x != null) { // y轴遍历
            Index<K, V> next = x.right;
            while (next != null) { // x轴遍历
                if (next.node == nextIndexNode) {
                    return next; // 找到了
                } else if (gather(next.node.key, nextIndexNode.key)) {
                    break; // 超过了
                }
                next = next.right;
            }
            x = x.down;
        }
        return null;
    }

}
