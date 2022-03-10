package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 *
 * 相比较 HashMap_base_01
 * 1. 数组+链表
 * 2. 数组容量随意
 * 3. 链表采用 头插法或尾插法
 * 4. 扩容采用2倍
 * 1. 增加 hash 扰动算法, hash计算将高16位纳入计算范围
 * 2. 采用 & 替换 % 计算下标, 效率较高
 * 变化
 * 1. 扩容的头插法, 改成优化后的尾插法
 * 2. 链表中部分元素无用重新计算索引, 节点中存储有当前节点的hash值, 扩容的时候, 就不需要重新计算hash值
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_03<K, V> extends HashMap_base_02<K, V> {

    public HashMap_base_03() {
        super();
    }

    public HashMap_base_03(int capacity) {
        super(capacity);
    }


    @Override
    public V put(K k, V v) {
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
            // 在链表中查找, 新增插入的时候必须用尾插法, 因为还需要和链表中数据对比
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

    /**
     * 扩容
     */
    @Override
    protected void dilatation() {
        // 扩容2倍
        Entry<K, V>[] newDatas = new Entry[datas.length << 1];
        // 遍历数组
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
            Entry<K, V> headOld = null, // 原位置头
                    tailOld = null, // 原位置尾
                    headNew = null, // 新位置头
                    tailNew = null, // 新位置尾
                    n = datas[i];

            while (n != null) {
                Entry<K, V> next = n.next;
                int index = getIndex(n.hash, newDatas.length);
                if (index == i) {
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
                newDatas[i] = headOld;
                datas[i] = null;
            }
            if (headNew != null) {
                // 需要移动的链表
                int index = getIndex(headNew.hash, newDatas.length);
                newDatas[index] = headNew;
            }
        }
        datas = newDatas;
    }

    @Override
    protected int getIndex(int hash, int length) {
        return hash & (length - 1);
    }

}
