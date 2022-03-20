package org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap;

/**
 * heap 原地建堆
 * 3种方式 原地建堆
 * 1. 将数组数据逐个放入堆中
 * 2. 自上而下上滤
 * 3. 自下而上下滤
 */
public class Heap_base_03<T> extends Heap_base_02<T> {

    public Heap_base_03(T[] datas) {
        this(true, datas);
    }

    public Heap_base_03(boolean little, T[] datas) {
        this.little = little;
        this.datas = datas;
    }

    /**
     * 原地建堆, 1. 将数组数据逐个放入堆中
     */
    public void build1() {
        for (T data : datas) {
            push(data);
        }
    }

    /**
     * 原地建堆, 2. 自上而下上滤
     */
    public void build2() {
        for (int i = 0; i < datas.length; i++) {
            shiftUp(i);
        }
    }

    /**
     * 原地建堆, 3. 自下而上下滤
     */
    public void build3() {
        int length = datas.length; // 忽略叶子结点
        for (int i = (length >> 1) - 1; i >= 0; i--) {
            shiftDown(i);
        }
    }

}
