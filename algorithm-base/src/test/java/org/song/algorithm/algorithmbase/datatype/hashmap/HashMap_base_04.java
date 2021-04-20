package org.song.algorithm.algorithmbase.datatype.hashmap;

/**
 * 实现简单功能的 HashMap, 模仿JDK中的HashMap
 * 增加树华, 未完成
 *
 * @param <K>
 * @param <V>
 */
public class HashMap_base_04<K, V> {

    private Entry<K, V>[] datas;

    private double dilatationRatio = 0.75;

    private int initCapacity = 1 << 3;

    private int size;

    public HashMap_base_04() {
        datas = new Entry[initCapacity];
    }

    public HashMap_base_04(int capacity) {
        datas = new Entry[initCapacity = upPower(capacity)];
    }

    public V get(K k) {
        return null;
    }

    public V put(K k, V v) {
        return null;
    }

    public V remove(K k) {
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
     */
    private void dilatation() {
    }

    private void putNewEntry(Entry<K, V>[] newDatas, Entry<K, V> entry) {
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

        public Entry() {
        }

        public Entry(K k, V val, Entry<K, V> next) {
            this.k = k;
            this.val = val;
            this.next = next;
        }
    }

    class TreeNode<K, V> extends Entry<K, V> {

        TreeNode<K, V> parent;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        /**
         * 和 next 形成双向链表
         */
        TreeNode<K, V> prev;

        public TreeNode(TreeNode<K, V> parent, TreeNode<K, V> left, TreeNode<K, V> right) {
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        public TreeNode(K k, V val, TreeNode<K, V> prev, Entry<K, V> next) {
            super(k, val, next);
            this.prev = prev;
        }
    }
}
