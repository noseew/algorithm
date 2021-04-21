package org.song.algorithm.algorithmbase.datatype.tree;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * heap 基于数组存储
 *
 * @param <T>
 */
public class Heap_base_01<T> {

    private T[] datas;
    private int size;
    /**
     * 0: 小堆(默认)
     * 1: 大堆
     */
    private int property;
    private Comparator<T> comparator;

    private double dilatationRatio = 0.8;
    private static int initCapacity = 7;

    public Heap_base_01() {
        this.property = 0;
        datas = (T[]) new Object[initCapacity];
    }

    public Heap_base_01(int property, Comparator<T> comparator) {
        this.property = property;
        this.comparator = comparator;
        datas = (T[]) new Object[initCapacity];
    }

    /*
     堆节点关系
        i: 当前节点
        parent(i) = floor((i - 1)/2) = (i - 1) >> 1
        child(i)  = 2*i + 1     =   i >> 1 + 1
        left(i)   = 2*i + 1     =   i >> 1 + 1
        right(i)  = 2*i + 2     =   i >> 1 + 2
     */

    public void push(T v) {
        // 新元素存储到数组下一位,
        datas[size++] = v;
        if (size == 1) {
            // 只有一个元素
            return;
        }
        shiftUp(size - 1);
        ensureCapacity();
    }

    public T pop() {
        if (size == 0) {
            return null;
        }
        T v = datas[0];
        datas[0] = datas[size - 1];
        datas[size - 1] = null;
        shiftDown(0);
        size--;
        return v;
    }

    /**
     * 子节点上升
     * 新元素存储到数组下一位, 新元素在叶子节点判断一次上升, 直到合适的位置
     */
    private void shiftUp(int child) {

        int childIndex = child;
        int parenIndex = (childIndex - 1) >> 1;
        while (parenIndex >= 0) {
            // 父子对比并交换
            if (isExchange(parenIndex, childIndex)) {
                exchange(parenIndex, childIndex);
            } else {
                break;
            }
            // 兄弟对比并交换
            int brotherIndex = isLeft(childIndex) ? childIndex + 1 : parenIndex * 2 + 1;
            if (isExchange(parenIndex, brotherIndex)) {
                exchange(parenIndex, brotherIndex);
                break;
            }
            // 索引上移
            childIndex = parenIndex;
            parenIndex = (parenIndex - 1) >> 1;
        }
    }

    /**
     * 父节点下降
     * 现将尾结点元素移动到根节点上, 然后对根节点进行下降调整
     */
    private void shiftDown(int parent) {
        int parenIndex = parent;
        int childIndex = (parenIndex << 1) + 1;
        while (childIndex < size ) {
            // 父子对比并交换
            if (isExchange(parenIndex, childIndex)) {
                exchange(parenIndex, childIndex);
            }
            // 兄弟对比并交换
            int brotherIndex = isLeft(childIndex) ? childIndex + 1 :(parenIndex << 1) + 1;
            if (isExchange(parenIndex, brotherIndex)) {
                exchange(parenIndex, brotherIndex);
            }
            parenIndex = childIndex;
            childIndex = (parenIndex << 1) + 1;
            if (childIndex == parenIndex) {
                break;
            }
        }
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
        T parent = datas[parentIndex];
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
            if (comparator != null) {
                return comparator.compare(parent, child) < 0;
            }
            return ((Comparable) parent).compareTo(child) < 0;
        } else {
            // 小堆
            if (comparator != null) {
                return comparator.compare(parent, child) > 0;
            }
            return ((Comparable) parent).compareTo(child) > 0;
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

    public String toPretty() {
        // 层数, 从1开始
        int totalLevel = (int) Math.floor(log2(size)) + 1;
        if (totalLevel<=0) {
            return "";
        }
        // 每层元素数
        int[] levelCount = new int[totalLevel];
        for (int i = 0; i < totalLevel; i++) {
            if (i == 0) {
                levelCount[i] =  1;
            } else {
                levelCount[i] = i << 1;
            }
        }
        StringBuilder sb = new StringBuilder();

        int count = 0;
        for (int i = 0; i < levelCount.length; i++) {
            boolean inter = false;
            for (int j = 0; j < levelCount[i]; j++) {
                sb.append(datas[count++]);
                if (count >= size) {
                    inter = true;
                    break;
                }
            }
            if (inter) {
                break;
            }
            sb.append("\r\n");
        }
        return sb.toString();
    }

    public static void appendEnter(StringBuilder sb, String e, int times) {
        for (int i = 0; i < times; i++) {
            sb.append(e);
        }
    }

    public static double log2(double x) {
        double n = 2;//底数
        double m = x;//指数
        return Math.log(m) / Math.log(n);
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
