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

    private transient volatile HeadIndex<K, V> head;

    final Comparator<? super K> comparator;

    private void initialize() {
        head = new HeadIndex<K, V>(new Node<K, V>(null, BASE_HEADER, null),
                null, null, 1);
    }

    private boolean casHead(HeadIndex<K, V> cmp, HeadIndex<K, V> val) {
        this.head = val;
        return true;
    }

    final int cpr(Comparator c, K x, K y) {
        return x.compareTo(y);
    }

    private Node<K, V> findPredecessor(K key, Comparator<? super K> cmp) {
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
                if (cpr(cmp, key, r.node.key) > 0) {
                    q = r;
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
        for (Node<K, V> b = findPredecessor(key, cmp), n = b.next; ; ) {
            if (n == null) {
                return null;
            }
            Object v = n.value;
            Node<K, V> f = n.next;
            if (cpr(cmp, key, n.key) == 0) {
                return (V) v;
            }
            b = n;
            n = f;
        }
    }

    private V doPut(K key, V value, boolean onlyIfAbsent) {
        if (key == null) {
            throw new NullPointerException();
        }
        Node<K, V> z = null;             // added node
        Comparator<? super K> cmp = comparator;
        for (Node<K, V> b = findPredecessor(key, cmp), // 需要插入点, 需要找到节点的前驱节点
             n = b.next; // 元素需要插入在 [b, n] 之间
                ; ) {
            if (n != null) {
                Object v;
                int c;
                // f 后继节点
                Node<K, V> f = n.next;
                if (n != b.next) {               // inconsistent read
                    break;
                }
                if ((v = n.value) == null) {   // n is deleted
                    // 发现 n 已经被删除, 执行删除清理逻辑, n.value == null 表示节点的删除状态, 链表的高效并发采用的就是 CAS+节点状态 实现的
                    n.helpDelete(b, f);
                    break;
                }
                if ((c = cpr(cmp, key, n.key)) > 0) {
                    b = n;
                    n = f;
                    continue;
                }
                if (c == 0) {
                    if (onlyIfAbsent) {
                        n.value = value;
                        return (V) v;
                    }
                    break; // restart if lost race to replace value 如果race失败, 重新启动以替换value
                }
            }

            // 本次需要添加的节点
            z = new Node<K, V>(key, value, n);
            b.next = z;
            break;
        }

        // 开始为节点添加索引层, rnd 范围int全值(正负)
        int rnd = ThreadLocalRandom.current().nextInt();
        if ((rnd & 0x80000001) == 0) { // test highest and lowest bits 测试最高和最低位
            int level = 1, max;
            while (((rnd >>>= 1) & 1) != 0) {
                ++level;
            }
            Index<K, V> idx = null;
            HeadIndex<K, V> h = head;

            if (level <= (max = h.level)) {
                for (int i = 1; i <= level; ++i) {
                    idx = new Index<K, V>(z, idx, null);
                }
            } else {
                level = max + 1; // hold in array and later pick the one to use 在数组中保存, 然后选择要使用的一个
                Index<K, V>[] idxs = (Index<K, V>[]) new Index<?, ?>[level + 1];
                for (int i = 1; i <= level; ++i) {
                    // 循环创建索引层级, 将其串成链表
                    idxs[i] = idx = new Index<K, V>(z, idx, null);
                }
                for (; ; ) {
                    // 左上角节点
                    h = head;
                    // 跳跃表原最大高度
                    int oldLevel = h.level;
                    if (level <= oldLevel) { // lost race to add level
                        break;
                    }
                    HeadIndex<K, V> newh = h;
                    Node<K, V> oldbase = h.node;
                    for (int j = oldLevel + 1; j <= level; ++j) {
                        newh = new HeadIndex<K, V>(oldbase, newh, idxs[j], j);
                    }
                    this.head = newh;
                    h = newh;
                    idx = idxs[level = oldLevel];
                    break;
                }
            }
            for (int insertionLevel = level; ; ) {
                // j 当前正在遍历的索引链表的层级
                int j = h.level;
                for (Index<K, V> q = h, r = q.right, t = idx; ; ) {
                    if (q == null || t == null){
                        return null;
                    }
                    if (r != null) {
                        Node<K, V> n = r.node;
                        int c = cpr(cmp, key, n.key);
                        if (n.value == null) {
                            q.unlink(r);
                            r = q.right;
                            continue;
                        }
                        if (c > 0) {
                            q = r;
                            r = r.right;
                            continue;
                        }
                    }

                    if (j == insertionLevel) {
                        q.link(r, t);
                        --insertionLevel;
                    }

                    if (--j >= insertionLevel && j < level) {
                        t = t.down;
                    }
                    q = q.down;
                    if (q == null) {
                        break;
                    }
                    r = q.right;
                }
            }
        }
        return null;
    }

    final V doRemove(K key, Object value) {
        if (key == null)
            throw new NullPointerException();
        Comparator<? super K> cmp = comparator;
        for (Node<K, V> b = findPredecessor(key, cmp), n = b.next; ; ) {
            Object v;
            int c;
            Node<K, V> f = n.next;
            if (n != b.next)                    // inconsistent read
                break;
            if ((v = n.value) == null) {        // n is deleted
                n.helpDelete(b, f);
                break;
            }
            if (b.value == null || v == n)      // b is deleted
                break;
            c = cpr(cmp, key, n.key);
            if (c > 0) {
                b = n;
                n = f;
                continue;
            }
            n.value = null;
            n.appendMarker(f);
            b.next = f;
            findPredecessor(key, cmp);      // clean index
            if (head.right == null) tryReduceLevel();

            return (V) v;
        }
        return null;
    }

    private void tryReduceLevel() {
        HeadIndex<K, V> h = head;
        HeadIndex<K, V> d;
        HeadIndex<K, V> e;
        if (h.level > 3 &&
                (d = (HeadIndex<K, V>) h.down) != null &&
                (e = (HeadIndex<K, V>) d.down) != null &&
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
                    q.unlink(r);
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
        return doPut(key, value, false);
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
        volatile Object value;
        volatile Node<K, V> next;

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

        boolean appendMarker(Node<K, V> f) {
            next = new Node<K, V>(f);
            return true;
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
        volatile Index<K, V> right;

        Index(Node<K, V> node, Index<K, V> down, Index<K, V> right) {
            this.node = node;
            this.down = down;
            this.right = right;
        }

        final boolean indexesDeletedNode() {
            return node.value == null;
        }

        final boolean link(Index<K, V> succ, Index<K, V> newSucc) {
            newSucc.right = succ;
            right = newSucc;
            return true;
        }

        final boolean unlink(Index<K, V> succ) {
            right = succ.right;
            return true;
        }

    }

    static final class HeadIndex<K, V> extends Index<K, V> {
        final int level;

        HeadIndex(Node<K, V> node, Index<K, V> down, Index<K, V> right, int level) {
            super(node, down, right);
            this.level = level;
        }
    }
}
