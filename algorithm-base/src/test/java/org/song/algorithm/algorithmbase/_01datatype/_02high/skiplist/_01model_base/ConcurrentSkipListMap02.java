
package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_base;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class ConcurrentSkipListMap02<K, V> {

    private static final Object BASE_HEADER = new Object();

    private transient volatile HeadIndex<K, V> head;

    final Comparator<? super K> comparator;

    private void initialize() {
        head = new HeadIndex<K, V>(new Node<K, V>(null, BASE_HEADER, null),
                null, null, 1);
    }

    private boolean casHead(HeadIndex<K, V> cmp, HeadIndex<K, V> val) {
        return UNSAFE.compareAndSwapObject(this, headOffset, cmp, val);
    }

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

        boolean casValue(Object cmp, Object val) {
            return UNSAFE.compareAndSwapObject(this, valueOffset, cmp, val);
        }

        boolean casNext(Node<K, V> cmp, Node<K, V> val) {
            return UNSAFE.compareAndSwapObject(this, nextOffset, cmp, val);
        }

        boolean appendMarker(Node<K, V> f) {
            return casNext(f, new Node<K, V>(f));
        }

        void helpDelete(Node<K, V> b, Node<K, V> f) {
            if (f == next && this == b.next) {
                if (f == null || f.value != f) { // not already marked
                    casNext(f, new Node<K, V>(f));
                } else {
                    b.casNext(this, f.next);
                }
            }
        }

        private static final sun.misc.Unsafe UNSAFE;
        private static final long valueOffset;
        private static final long nextOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                UNSAFE = (Unsafe) f.get(null);
                
//                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Node.class;
                valueOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("value"));
                nextOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("next"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    static class Index<K, V> {
        final Node<K, V> node;
        final Index<K, V> down;
        volatile Index<K, V> right;

        Index(Node<K, V> node, Index<K, V> down, Index<K, V> right) {
            this.node = node;
            this.down = down;
            this.right = right;
        }

        final boolean casRight(Index<K, V> cmp, Index<K, V> val) {
            return UNSAFE.compareAndSwapObject(this, rightOffset, cmp, val);
        }

        final boolean link(Index<K, V> succ, Index<K, V> newSucc) {
            Node<K, V> n = node;
            newSucc.right = succ;
            return n.value != null && casRight(succ, newSucc);
        }

        final boolean unlink(Index<K, V> succ) {
            return node.value != null && casRight(succ, succ.right);
        }

        // Unsafe mechanics
        private static final sun.misc.Unsafe UNSAFE;
        private static final long rightOffset;

        static {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                UNSAFE = (Unsafe) f.get(null);
                
//                UNSAFE = sun.misc.Unsafe.getUnsafe();
                Class<?> k = Index.class;
                rightOffset = UNSAFE.objectFieldOffset
                        (k.getDeclaredField("right"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    static final class HeadIndex<K, V> extends Index<K, V> {
        final int level;

        HeadIndex(Node<K, V> node, Index<K, V> down, Index<K, V> right, int level) {
            super(node, down, right);
            this.level = level;
        }
    }

    static final int cpr(Comparator c, Object x, Object y) {
        return (c != null) ? c.compare(x, y) : ((Comparable) x).compareTo(y);
    }

    private Node<K, V> findPredecessor(Object key, Comparator<? super K> cmp) {
        if (key == null)
            throw new NullPointerException(); // don't postpone errors
        for (; ; ) {
            for (Index<K, V> q = head, r = q.right, d; ; ) {
                if (r != null) {
                    Node<K, V> n = r.node;
                    K k = n.key;
                    if (n.value == null) {
                        if (!q.unlink(r))
                            break;           // restart
                        r = q.right;         // reread r
                        continue;
                    }
                    if (cpr(cmp, key, k) > 0) {
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
    }

    private Node<K, V> findNode(Object key) {
        if (key == null)
            throw new NullPointerException(); // don't postpone errors
        Comparator<? super K> cmp = comparator;
        outer:
        for (; ; ) {
            for (Node<K, V> b = findPredecessor(key, cmp), n = b.next; ; ) {
                Object v;
                int c;
                if (n == null)
                    break outer;
                Node<K, V> f = n.next;
                if (n != b.next)                // inconsistent read
                    break;
                if ((v = n.value) == null) {    // n is deleted
                    n.helpDelete(b, f);
                    break;
                }
                if (b.value == null || v == n)  // b is deleted
                    break;
                if ((c = cpr(cmp, key, n.key)) == 0)
                    return n;
                if (c < 0)
                    break outer;
                b = n;
                n = f;
            }
        }
        return null;
    }

    private V doGet(Object key) {
        if (key == null)
            throw new NullPointerException();
        Comparator<? super K> cmp = comparator;
        outer:
        for (; ; ) {
            for (Node<K, V> b = findPredecessor(key, cmp), n = b.next; ; ) {
                Object v;
                int c;
                if (n == null)
                    break outer;
                Node<K, V> f = n.next;
                if (n != b.next)                // inconsistent read
                    break;
                if ((v = n.value) == null) {    // n is deleted
                    n.helpDelete(b, f);
                    break;
                }
                if (b.value == null || v == n)  // b is deleted
                    break;
                if ((c = cpr(cmp, key, n.key)) == 0) {
                    @SuppressWarnings("unchecked") V vv = (V) v;
                    return vv;
                }
                if (c < 0)
                    break outer;
                b = n;
                n = f;
            }
        }
        return null;
    }

    private V doPut(K key, V value, boolean onlyIfAbsent) {
        Node<K, V> z;             // added node
        if (key == null) {
            throw new NullPointerException();
        }
        Comparator<? super K> cmp = comparator;
        outer:
        for (; ; ) {
            for (Node<K, V> b = findPredecessor(key, cmp), // 需要插入点, 需要找到节点的前驱节点
                 n = b.next; // 元素需要插入在 [b, n] 之间
                    ; ) {
                if (n != null) {
                    Object v;
                    int c;
                    Node<K, V> f = n.next;
                    if (n != b.next) {               // inconsistent read
                        break;
                    }
                    if ((v = n.value) == null) {   // n is deleted
                        n.helpDelete(b, f);
                        break;
                    }
                    if (b.value == null || v == n) { // b is deleted
                        break;
                    }
                    if ((c = cpr(cmp, key, n.key)) > 0) {
                        b = n;
                        n = f;
                        continue;
                    }
                    if (c == 0) {
                        if (onlyIfAbsent || n.casValue(v, value)) {
                            @SuppressWarnings("unchecked") V vv = (V) v;
                            return vv;
                        }
                        break; // restart if lost race to replace value 如果race失败, 重新启动以替换value
                    }
                    // else c < 0; fall through
                }

                z = new Node<K, V>(key, value, n);
                if (!b.casNext(n, z)) {
                    break;         // restart if lost race to append to b}
                }
                break outer;
            }
        }

//        int rnd = ThreadLocalRandom.nextSecondarySeed();
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
                @SuppressWarnings("unchecked") Index<K, V>[] idxs =
                        (Index<K, V>[]) new Index<?, ?>[level + 1];
                for (int i = 1; i <= level; ++i) {
                    idxs[i] = idx = new Index<K, V>(z, idx, null);
                }
                for (; ; ) {
                    h = head;
                    int oldLevel = h.level;
                    if (level <= oldLevel) { // lost race to add level
                        break;
                    }
                    HeadIndex<K, V> newh = h;
                    Node<K, V> oldbase = h.node;
                    for (int j = oldLevel + 1; j <= level; ++j) {
                        newh = new HeadIndex<K, V>(oldbase, newh, idxs[j], j);
                    }
                    if (casHead(h, newh)) {
                        h = newh;
                        idx = idxs[level = oldLevel];
                        break;
                    }
                }
            }
            splice:
            for (int insertionLevel = level; ; ) {
                // j 当前正在遍历的索引链表的层级
                int j = h.level;
                for (Index<K, V> q = h, r = q.right, t = idx; ; ) {
                    if (q == null || t == null)
                        break splice;
                    if (r != null) {
                        Node<K, V> n = r.node;
                        int c = cpr(cmp, key, n.key);
                        if (n.value == null) {
                            if (!q.unlink(r))
                                break;
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
                        if (!q.link(r, t))
                            break; // restart
                        if (t.node.value == null) {
                            findNode(key);
                            break splice;
                        }
                        if (--insertionLevel == 0) {
                            break splice;
                        }
                    }

                    if (--j >= insertionLevel && j < level) {
                        t = t.down;
                    }
                    q = q.down;
                    r = q.right;
                }
            }
        }
        return null;
    }

    final V doRemove(Object key, Object value) {
        if (key == null)
            throw new NullPointerException();
        Comparator<? super K> cmp = comparator;
        outer:
        for (; ; ) {
            for (Node<K, V> b = findPredecessor(key, cmp), n = b.next; ; ) {
                Object v;
                int c;
                if (n == null)
                    break outer;
                Node<K, V> f = n.next;
                if (n != b.next)                    // inconsistent read
                    break;
                if ((v = n.value) == null) {        // n is deleted
                    n.helpDelete(b, f);
                    break;
                }
                if (b.value == null || v == n)      // b is deleted
                    break;
                if ((c = cpr(cmp, key, n.key)) < 0)
                    break outer;
                if (c > 0) {
                    b = n;
                    n = f;
                    continue;
                }
                if (value != null && !value.equals(v))
                    break outer;
                if (!n.casValue(v, null))
                    break;
                if (!n.appendMarker(f) || !b.casNext(n, f))
                    findNode(key);                  // retry via findNode
                else {
                    findPredecessor(key, cmp);      // clean index
                    if (head.right == null)
                        tryReduceLevel();
                }
                @SuppressWarnings("unchecked") V vv = (V) v;
                return vv;
            }
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

    public ConcurrentSkipListMap02() {
        this.comparator = null;
        initialize();
    }

    public ConcurrentSkipListMap02(Comparator<? super K> comparator) {
        this.comparator = comparator;
        initialize();
    }

    public V get(Object key) {
        return doGet(key);
    }

    public V put(K key, V value) {
        if (value == null) {
            // 不允许 null 值
            throw new NullPointerException();
        }
        return doPut(key, value, false);
    }

    public V remove(Object key) {
        return doRemove(key, null);
    }

    public void clear() {
        initialize();
    }

    public boolean remove(Object key, Object value) {
        if (key == null)
            throw new NullPointerException();
        return value != null && doRemove(key, value) != null;
    }

    public Comparator<? super K> comparator() {
        return comparator;
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe UNSAFE;
    private static final long headOffset;
    private static final long SECONDARY;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            UNSAFE = (Unsafe) f.get(null);
            
//            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ConcurrentSkipListMap02.class;
            headOffset = UNSAFE.objectFieldOffset
                    (k.getDeclaredField("head"));
            Class<?> tk = Thread.class;
            SECONDARY = UNSAFE.objectFieldOffset
                    (tk.getDeclaredField("threadLocalRandomSecondarySeed"));

        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
