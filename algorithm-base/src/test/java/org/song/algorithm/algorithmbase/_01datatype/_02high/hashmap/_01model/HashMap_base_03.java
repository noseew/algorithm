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

        Entry<K, V> oldEntry = null;
        Entry<K, V> head = datas[index];
        if (head == null) {
            // 没有 则新增
            datas[index] = new Entry<>(k, v, null, hash);
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
                    oldEntry = next;
                    pre.next = new Entry<>(k, v, next.next, hash);
                    return oldEntry.val;
                }
                pre = next;
            }
            // 放在链表尾部
            pre.next = new Entry<>(k, v, null, hash);
        }
        size++; // 容量增加
        ensureCapacity();
        return oldEntry != null ? oldEntry.val : null;
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
            Entry<K, V> headOld = datas[i],
                    prevOld = headOld,
                    headNew = null,
                    prevNew = null,
                    n = prevOld;

            while (n != null) {
                Entry<K, V> next = n.next;
                int index = getIndex(n.hash, newDatas.length);
                if (index == i) {
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
