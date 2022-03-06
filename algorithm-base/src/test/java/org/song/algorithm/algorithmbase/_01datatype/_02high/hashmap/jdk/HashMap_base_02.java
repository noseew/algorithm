package org.song.algorithm.algorithmbase._01datatype._02high.hashmap.jdk;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 *
 * 相比较 HashMap_base_01
 * 1. 增加 hash 扰动算法
 * 2. 采用 & 替换 % 计算下标
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_02<K, V> extends HashMap_base_01<K, V> {

    public HashMap_base_02() {
        datas = new Entry[initCapacity];
    }

    public HashMap_base_02(int capacity) {
        datas = new Entry[initCapacity = upPower(capacity)];
    }

    @Override
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

    @Override
    public V put(K k, V v) {
        int hash = hash(k);
        int len = datas.length;
        int index = hash & (len - 1);

        Entry<K, V> oldEntry = null;
        Entry<K, V> head = datas[index];
        if (head == null) {
            datas[index] = new Entry<>(k, v, null);
        } else if (head.k.equals(k)) {
            datas[index] = new Entry<>(k, v, head.next);
        } else {
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) {
                    break;
                }
                if (next.k.equals(k)) {
                    oldEntry = next;
                    pre.next = new Entry<>(k, v, next.next);
                    return oldEntry.val;
                }
                pre = next;
            }
            pre.next = new Entry<>(k, v, null);
        }
        size++;
        ensureCapacity();
        return null;
    }

    @Override
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

    protected int upPower(int n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 16 : n + 1;
    }

    /**
     * 扩容
     */
    @Override
    protected void dilatation() {
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];
        for (Entry<K, V> head : datas) {
            while (head != null) {
                Entry<K, V> next = head.next;
                head.next = null;
                putNewEntry(newDatas, head);
                head = next;
            }
        }
        datas = newDatas;
    }

    @Override
    protected void putNewEntry(Entry<K, V>[] newDatas, Entry<K, V> entry) {

        int hash = hash(entry.k);
        int len = newDatas.length;
        int index = hash & (len - 1);

        Entry<K, V> head = newDatas[index];
        if (head == null) {
            newDatas[index] = entry;
        } else {
            // 头插法, 只需要遍历一次链表
            entry.next = head;
            newDatas[index] = entry;

            // 尾插法, 需要多遍历一次链表
//            Entry<K, V> pre = head, next = pre.next;
//            while (next != null) {
//                pre = next;
//                next = pre.next;
//            }
//            pre.next = entry;
        }
    }

    @Override
    public int hash(K k) {
        if (k == null) {
            return 0;
        }
        int hash = System.identityHashCode(k);
        return hash ^ (hash >>> 16);
    }
}
