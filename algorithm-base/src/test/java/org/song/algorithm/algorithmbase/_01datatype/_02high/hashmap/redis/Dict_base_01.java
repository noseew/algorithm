package org.song.algorithm.algorithmbase._01datatype._02high.hashmap.redis;

import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.AbstractMap;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_03;

/**
 * 实现简单功能的 dict, 模仿redis中的dict
 * 和 HashMap_base_03 相比
 * 1. 扩容采用渐进式rehash, 而不是一次性全部扩容完成, 复杂度均摊
 * 2. 扩容的时候, 如果datas取不到数据再次从datas1中重试
 * 
 * TODO 未完成
 *
 * @param <K>
 * @param <V>
 */
public class Dict_base_01<K, V> extends HashMap_base_03<K, V> {

    /**
     * 用于扩容
     */
    protected AbstractMap.Entry<K, V>[] datas1;
    /**
     * -1: 没有扩容
     * >=0: 正在扩容, 扩容进度, 表示下次需要复制的原数组的下标
     */
    protected int dilatation = -1;

    @Override
    public V get(K k) {
        dilatationIndex(dilatation);
        V v = get(datas, k);
        if (v == null && dilatation >= 0) {
            return get(datas1, k);
        }
        return v;
    }

    protected V get(AbstractMap.Entry<K, V>[] datas, K k) {
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
        dilatationIndex(dilatation);
        if (dilatation >= 0) {
            V put = put(datas1, k, v);
            remove(datas, k);
            return put;
        }
        return put(datas, k, v);
    }
    
    protected V put(AbstractMap.Entry<K, V>[] datas, K k, V v) {
        int hash = hash(k);
        int index = getIndex(hash, datas.length);

        Entry<K, V> head = datas[index];
        boolean added = false;
        if (head == null) {
            // 没有 则新增
            datas[index] = new Entry<>(k, v, null, hash);
            added = true;
        } else if (hash == hash(head.k) && (k == head.k || head.k.equals(k))) {
            // 有 则替换
            datas[index] = new Entry<>(k, v, head.next, hash);
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
                    pre.next = new Entry<>(k, v, next.next, hash);
                    return next.val;
                }
                pre = next;
            }
            // 放在链表尾部
            pre.next = new Entry<>(k, v, null, hash);
            added = true;
        }
        size++; // 容量增加
        ensureCapacity();
        return added ? null : v;
    }

    @Override
    public V remove(K k) {
        dilatationIndex(dilatation);
        V v = remove(datas, k);
        V v2 = remove(datas1, k);
        return v != null ? v : v2;
    }
    
    protected V remove(AbstractMap.Entry<K, V>[] datas, K k) {
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
    @Override
    protected void ensureCapacity() {
        if ((double) size / (double) datas.length > dilatationRatio) {
            if (dilatation == -1) dilatation = 0;
            dilatationIndex(dilatation);
        }
    }

    /**
     * 扩容
     * 
     * @param index 本次需要处理的原数组下标
     */
    protected void dilatationIndex(int index) {
        if (index < 0) {
            return;
        }
        // 扩容2倍
        if (datas1 == null) {
            datas1 = new Entry[datas.length << 1];
        }
        Entry<K, V> head = datas[index];
        while (head == null && index < datas.length) {
            head = datas[index++];
        }
        if (head == null) {
            // 扩容完成
            dilatation = -1;
            datas = datas1;
            datas1 = null;
            return;
        }
        
        dilatation = index;
        
        Entry<K, V> headOld = null, // 原位置头
                tailOld = null, // 原位置尾
                headNew = null, // 新位置头
                tailNew = null, // 新位置尾
                n = head;

        while (n != null) {
            Entry<K, V> next = n.next;
            int newIndex = getIndex(n.hash, datas1.length);
            if (index == newIndex) {
                // 不需要移动
                if (headOld == null) {
                    headOld = n;
                    tailOld = n;
                    headOld.next = null;
                } else {
                    tailOld.next = n;
                    tailOld = tailOld.next;
                    tailOld.next = null;
                }
            } else {
                // 需要移动
                if (headNew == null) {
                    headNew = n;
                    tailNew = n;
                    headNew.next = null;
                } else {
                    tailNew.next = n;
                    tailNew = tailNew.next;
                    tailNew.next = null;
                }
            }
            n = next;
        }

        if (headOld != null) {
            // 不需要移动的链表
            datas1[index] = headOld;
        }
        datas[index] = null;
        if (headNew != null) {
            // 需要移动的链表
            int newIndex = getIndex(headNew.hash, datas1.length);
            datas1[newIndex] = headNew;
        }
    }

}
