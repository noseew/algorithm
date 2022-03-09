package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 * 1. 数组+链表
 * 2. 数组容量随意
 * 3. 链表采用 头插法或尾插法
 * 4. 扩容采用2倍
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_01<K, V> extends AbstractMap<K, V> {

    protected Entry<K, V>[] datas;

    protected double dilatationRatio = 0.75;

    protected int initCapacity = 1 << 3;

    protected int size;

    public HashMap_base_01() {
        datas = new Entry[initCapacity];
    }

    public HashMap_base_01(int capacity) {
        datas = new Entry[initCapacity = capacity];
    }

    @Override
    public V get(K k) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);
        // 表示链表头
        Entry<K, V> head = datas[index];
        if (head == null) {
            return null;
        } else if (hash == hash(head.k) && (k == head.k || head.k.equals(k))) {
            return head.val;
        } else {
            // 遍历链表
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) break;
                // 找到了, 返回他
                if (hash == hash(next.k) && (k == next.k || next.k.equals(k))) return next.val;
                pre = next;
            }
        }
        return null;
    }

    @Override
    public V put(K k, V v) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = datas[index];
        boolean added = false;
        if (head == null) {
            // 没有 则新增
            datas[index] = new Entry<>(k, v, null);
            added = true;
        } else if (hash == hash(head.k) && (k == head.k || head.k.equals(k))) {
            // 有 则替换
            datas[index] = new Entry<>(k, v, head.next);
            return head.val;
        } else {
            // 在链表中查找
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                // 没找到 啥也不做
                if (next == null) break;
                if (hash == hash(next.k) && (k == next.k || next.k.equals(k))) {
                    // 找到了, 则替换
                    pre.next = new Entry<>(k, v, next.next);
                    return next.val;
                }
                pre = next;
            }
            // 放在链表尾部
            pre.next = new Entry<>(k, v, null);
            added = true;
        }
        size++; // 容量增加
        ensureCapacity();
        return added ? null : v;
    }

    @Override
    public V remove(K k) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = datas[index];
        if (head == null) {
            // 没有 返回空
            return null;
        } else if (hash == hash(head.k) && (k == head.k || head.k.equals(k))) {
            // 找到了 直接返回
            datas[index] = head.next; // 用下一个节点替换
            size--;
            return head.val;
        } else {
            // 在链表中查找
            Entry<K, V> pre = head, next;
            while (pre != null) {
                next = pre.next;
                if (next == null) break;
                // 找到了 删除并返回他
                if (hash == hash(next.k) && (k == next.k || next.k.equals(k))) {
                    pre.next = next.next;
                    size--;
                    return next.val;
                }
                pre = next;
            }
        }
        return null;
    }

    /**
     * 确保容量
     */
    protected void ensureCapacity() {
        if ((double) size / (double) datas.length > dilatationRatio) {
            dilatation();
        }
    }

    /**
     * 扩容
     */
    protected void dilatation() {
        // 扩容2倍
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];
        // 遍历数组
        for (Entry<K, V> head : datas) {
            // 遍历链表
            while (head != null) {
                Entry<K, V> next = head.next;
                head.next = null;
                putNewEntry(newDatas, head);
                head = next;
            }
        }
        datas = newDatas;
    }

    protected void putNewEntry(Entry<K, V>[] newDatas, Entry<K, V> entry) {
        K k = entry.k;
        V v = entry.val;

        /*
        重新计算 hash 和 index, 来判断它的具体位置
         */
        int index = getIndex(hash(k), newDatas.length);

        Entry<K, V> head = newDatas[index];
        if (head == null) {
            newDatas[index] = entry;
        } else {
            // 头插法
            entry.next = head;
            newDatas[index] = entry;

            // 尾插法
//            Entry<K, V> pre = head, next = pre.next;
//            while (next != null) {
//                pre = next;
//                next = pre.next;
//            }
//            pre.next = entry;
        }
    }

    protected int hash(K k) {
        if (k == null) {
            return 0;
        }
//        return System.identityHashCode(k); // 注意, 此函数同样的值计算的hash可能不相等(128以内相等), 待研究
        return Math.abs(k.hashCode());
    }

    protected int getIndex(int hash, int length) {
        return hash % length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(size);
        sb.append("\r\n");
        int count = 0;
        for (int i = 0; i < datas.length; i++) {
            Entry<K, V> data = datas[i];
            if (data != null) {
                // 遍历链表
                Entry<K, V> pre = data, next;
                sb.append(count++).append("-").append(i).append(": ");
                while (pre != null) {
                    next = pre.next;
                    sb.append(pre.toString());
                    if (next == null) break;
                    sb.append(",");
                    pre = next;
                }
                sb.append("\r\n");
            }
        }
        
        return sb.toString();
    }
}
