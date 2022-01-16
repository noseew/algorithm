package org.song.algorithm.algorithmbase.datatype._02high.hashmap.jdk;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 *
 * 相比较 HashMap_base_01
 * 1. 扩容的头插法, 改成优化后的尾插法
 * 2. 链表中部分元素无用重新计算索引
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_03<K, V> {

    private Entry<K, V>[] datas;

    private double dilatationRatio = 0.75;

    private int initCapacity = 1 << 3;

    private int size;

    public HashMap_base_03() {
        datas = new Entry[initCapacity];
    }

    public HashMap_base_03(int capacity) {
        datas = new Entry[initCapacity = upPower(capacity)];
    }

    public V get(K k) {
        int hash = hash(k);
        Entry<K, V> head = datas[hash & (datas.length - 1)];
        if (head == null) {
            return null;
        } else if (head.k.equals(k)) {
            return head.val;
        } else {
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) {
                    break;
                }
                if (next.k.equals(k)) {
                    return next.val;
                }
                pre = next;
            }
        }
        return null;
    }

    public V put(K k, V v) {
        int hash = hash(k);
        int len = datas.length;
        int index = hash & (len - 1);

        Entry<K, V> oldEntry = null;
        Entry<K, V> head = datas[index];
        if (head == null) {
            datas[index] = new Entry<>(k, v, null, hash);
        } else if (head.k.equals(k)) {
            datas[index] = new Entry<>(k, v, head.next, hash);
        } else {
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) {
                    break;
                }
                if (next.k.equals(k)) {
                    oldEntry = next;
                    pre.next = new Entry<>(k, v, next.next, hash);
                    return oldEntry.val;
                }
                pre = next;
            }
            pre.next = new Entry<>(k, v, null, hash);
        }
        size++;
        ensureCapacity();
        return null;
    }

    public V remove(K k) {
        int hash = hash(k);
        int index = hash & (datas.length - 1);

        Entry<K, V> head = datas[index];
        if (head == null) {
            return null;
        } else if (head.k.equals(k)) {
            datas[index] = head.next;
            size--;
            return head.val;
        } else {
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) {
                    break;
                }
                if (next.k.equals(k)) {
                    pre.next = next.next;
                    size--;
                    return next.val;
                }
                pre = next;
            }
        }
        return null;
    }

    private static int upPower(int n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 16 : n + 1;
    }

    /**
     * 确保容量
     */
    private void ensureCapacity() {
        if ((double) size / (double) datas.length >= dilatationRatio) {
            dilatation();
        }
    }

    /**
     * 扩容
     */
    private void dilatation() {
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];

        for (int i = 0; i < datas.length; i++) {
            if (datas[i] == null) {
                continue;
            }
            /*
             把原有的队列分成两个队列
             headOld 老队列头, 表示到新的数组中索引不会发生改变
             headNew 新队列头, 表示到新的数组中索引会发生改变
             直接将队列头元素, 放置到新数组指定位置即可

             和JDK7中对比,
                 1. 都只遍历一次, 但是不会出现列表倒转的情况
                 2. 部分元素不需要重复计算hash值和索引值

             难点:
                1. 遍历单向链表, 详情参见 Linked_single_02 单向链表遍历删除问题
                2. 由于需要保持头元素用于移动整个列表, 所以要多出两个变量 headOld headNew
             */
            Entry<K, V> headOld = datas[i],
                    prevOld = headOld,
                    headNew = null,
                    prevNew = null,
                    n = prevOld;

            while (n != null) {
                Entry<K, V> next = n.next;
                if ((n.hash & datas.length) == 0) {
                    // 不需要移动
                    if (n != prevOld) {
                        prevOld = n;
                    }
                } else {
                    // 需要移动
                    if (n == prevOld) {
                        // 是头元素, 更换头元素
                        headOld = next;
                        datas[i] = next;
                        prevOld = datas[i];
                    } else {
                        // 不是头元素, 跳过需要移动的元素
                        prevOld.next = next;
                    }
                    // 开始移动
                    // 新链表
                    if (prevNew == null) {
                        // 初始化新链表
                        headNew = n;
                        prevNew = headNew;
                        headNew.next = null;
                    } else {
                        // 在新链表尾部添加
                        prevNew.next = n;
                        prevNew = prevNew.next;
                    }
                }
                n = next;
            }

            if (headOld != null) {
                // 不需要移动的链表
//                newDatas[headOld.hash & (newDatas.length - 1)] = headOld;
                newDatas[i] = headOld;
                datas[i] = null;
            }
            if (headNew != null) {
                // 需要移动的链表
                int index = headNew.hash & (newDatas.length - 1);
                newDatas[index] = headNew;
            }
        }
        datas = newDatas;
    }

    public int hash(K k) {
        if (k == null) {
            return 0;
        }
        // 相同的值 x, 在不同的JVM进程中返回的值可能不同, 但在同一个JVM进程中相同
        int hash = System.identityHashCode(k);
//        int hash = k.hashCode();
        return hash ^ (hash >>> 16);
    }

    class Entry<K, V> {

        K k;
        V val;
        Entry<K, V> next;
        int hash;

        public Entry(K k, V val, Entry<K, V> next) {
            this.k = k;
            this.val = val;
            this.next = next;
        }

        public Entry(K k, V val, Entry<K, V> next, int hash) {
            this.k = k;
            this.val = val;
            this.next = next;
            this.hash = hash;
        }
    }
}
