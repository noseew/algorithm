package org.song.algorithm.algorithmbase.datatype;

/**
 * 实现简单功能的 HashMap
 * 相比较 HashMap_base_01
 * 1. 扩容的头插法, 改成优化后的尾插法
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
        if ((double) size / (double) datas.length > dilatationRatio) {
            dilatation();
        }
    }

    /**
     * 扩容
     * TODO 未完成
     */
    private void dilatation() {
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];

        for (int i = 0; i < datas.length && datas[i] != null; i++) {
            Entry<K, V> headOld = null,
                    lastOld = null,
                    headNew = null,
                    lastNew = null,
                    next = datas[i];
            do {
                if ((next.hash & datas.length) == 0) {
                    // 不需要移动
                    if (headOld == null) {
                        headOld = next;
                        lastOld = next;
                    } else {
                        lastOld.next = next;
                        lastOld = lastOld.next;
                    }
                } else {
                    // 需要移动
                    if (headOld != null) {
                        headOld.next = next.next;
                    }
                    if (headNew == null) {
                        headNew = next;
                        headNew.next = null;
                        lastNew = headNew;
                    } else {
                        lastNew.next = next;
                        lastNew.next.next = null;
                    }
                }
            } while ((next = next.next) != null);

            if (headOld != null) {
                int index = headOld.hash & (newDatas.length - 1);
                newDatas[index] = headOld;
            }
            if (headNew != null) {
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
        int hash = System.identityHashCode(k);
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
