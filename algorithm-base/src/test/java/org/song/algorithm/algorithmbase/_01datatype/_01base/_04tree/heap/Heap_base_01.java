package org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

/**
 * heap 基于数组存储
 * 逻辑结构 heap, 存储结构 数组
 * 堆节点关系
 * i: 当前节点
 * parent(i) = floor((i - 1)/2) = (i - 1) >> 1
 * child(i)  = 2*i + 1     =   i << 1 + 1
 * left(i)   = 2*i + 1     =   i << 1 + 1
 * right(i)  = 2*i + 2     =   i << 1 + 2
 *
 * 思路: 以小堆为例
 * 1. push: 存入heap尾, 依次上升
 * -    存储在数组最后一个元素(完全二叉树最后一个元素, 叶子节点)
 * -    和父节点比较, 子<父 则向上升
 * 2. pop: 取出heap顶, heap尾移动到heap顶, 选择分支, 依次下降
 * -    取出heap顶, 将heap尾元素移动到heap顶
 * -    heap顶作为父节点 parent, 选一个最小的 child = Min(left, right),
 * -    parent 和 child 对比并互换, 依次下降
 *
 *
 * @param <T>
 */
public class Heap_base_01<T> extends AbstractHeap<T> {

    private double dilatationRatio = 0.8;
    private static int initCapacity = 7;

    public Heap_base_01() {
        this.little = true;
        datas = (T[]) new Object[initCapacity];
    }

    public Heap_base_01(int property) {
        this.little = true;
        datas = (T[]) new Object[initCapacity];
    }

    /*
     堆节点关系
        i: 当前节点
        parent(i) = floor((i - 1)/2) = (i - 1) >> 1
        child(i)  = 2*i + 1     =   i << 1 + 1
        left(i)   = 2*i + 1     =   i << 1 + 1
        right(i)  = 2*i + 2     =   i << 1 + 2
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

    public static void appendEnter(StringBuilder sb, String e, int times) {
        for (int i = 0; i < times; i++) {
            sb.append(e);
        }
    }

}
