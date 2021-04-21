package org.song.algorithm.algorithmbase.datatype.tree;

import java.lang.reflect.Array;

/**
 * heap 基于数组存储
 *
 * @param <T>
 */
public class Heap_base_01<T extends Comparable<T>> {

    private Comparable[] datas;
    private int size;
    /**
     * 0: 小堆(默认)
     * 1: 大堆
     */
    private int property;

    private double dilatationRatio = 0.8;
    private static int initCapacity = 7;

    public Heap_base_01() {
        this.property = 0;
        datas = (T[]) Array.newInstance(Comparable.class, initCapacity);
    }

    public Heap_base_01(int property) {
        this.property = property;
        datas = (T[]) Array.newInstance(Comparable.class, initCapacity);
    }

    /*
     堆节点关系
        i: 当前节点
        parent(i) = floor((i - 1)/2)
        child(i)  = 2*i + 1
        left(i)   = 2*i + 1
        right(i)  = 2*i + 2
     */

    public void push(T v) {
        datas[size++] = v;
        if (size <= 1) {
            // 只有一个元素
            return;
        }

        int childIndex = size - 1;
        int parenIndex = (childIndex - 1) / 2;

        while (parenIndex >= 0 && parenIndex < childIndex) {
            // 父子对比并交换
            if (isExchange(parenIndex, childIndex)) {
                exchange(parenIndex, childIndex);
            }
            // 兄弟对比并交换
            int brotherIndex = isLeft(childIndex) ? childIndex + 1 : parenIndex * 2 + 1;
            if (isExchange(parenIndex, brotherIndex)) {
                exchange(parenIndex, brotherIndex);
            }

            // 索引上移
            childIndex = parenIndex;
            parenIndex = (parenIndex - 1) / 2;
        }
        ensureCapacity();
    }

    private boolean isLeft(int i) {
        // 是奇数则是左子节点
        return i % 2 == 1;
    }

    /**
     * 父子交换
     *
     * @param parentIndex
     * @param childIndex
     */
    private void exchange(int parentIndex, int childIndex) {
        if (parentIndex == childIndex) {
            return;
        }
        Comparable parent = datas[parentIndex];
        datas[parentIndex] = datas[childIndex];
        datas[childIndex] = parent;
    }

    /**
     * 是否需要交换
     *
     * @param parentIndex
     * @param childIndex
     * @return
     */
    private boolean isExchange(int parentIndex, int childIndex) {
        if (parentIndex == childIndex) {
            return false;
        }

        T parent = (T) datas[parentIndex];
        T child = (T) datas[childIndex];

        if (child == null) {
            return false;
        }

        if (property == 1) {
            // 大堆
            return (parent).compareTo(child) < 0;
        } else {
            // 小堆
            return (parent).compareTo(child) > 0;
        }
    }

    /**
     * 确保容量
     */
    private void ensureCapacity() {
        if ((double) size / (double) datas.length > dilatationRatio) {
            // 扩容 1.5 倍
            T[] newDatas = (T[]) Array.newInstance(Comparable.class, datas.length + (datas.length >> 1));
            System.arraycopy(datas, 0, newDatas, 0, size);
            datas = newDatas;
        }
    }


    //    class TreeNode<K, V> {
//
//        TreeNode<K, V> parent;
//        TreeNode<K, V> left;
//        TreeNode<K, V> right;
//        K k;
//        V v;
//
//        public TreeNode(TreeNode<K, V> parent, TreeNode<K, V> left, TreeNode<K, V> right) {
//            this.parent = parent;
//            this.left = left;
//            this.right = right;
//        }
//
//        public TreeNode(K k, V v) {
//            this.k = k;
//            this.v = v;
//        }
//    }
}
