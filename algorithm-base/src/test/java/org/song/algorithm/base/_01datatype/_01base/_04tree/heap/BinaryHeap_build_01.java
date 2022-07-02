package org.song.algorithm.base._01datatype._01base._04tree.heap;

import java.util.Comparator;

/**
 * heap 原地建堆
 * 3种方式 原地建堆
 * 1. 将数组数据逐个放入堆中, 效率等价2
 *  O(nlogn)
 * 2. 自上而下上滤, 效率等价1
 *  O(nlogn)所有节点深度之和?
 * 3. 自下而上下滤, 
 *  O(n)效率更高些, 
 *      1)处理节点数较少, 
 *      2)对比交换的总路程更短少, 所有节点高度之和?
 *      
 * 注意: 其他的(自上而下下滤/自下而上上滤) 都不行
 */
public class BinaryHeap_build_01<T> extends BinaryHeap_base_02<T> {

    public BinaryHeap_build_01(T[] datas, Comparator<T> comparator) {
        this(true, datas, comparator);
    }

    public BinaryHeap_build_01(boolean little, T[] datas, Comparator<T> comparator) {
        super(little, comparator);
        this.little = little;
        this.datas = datas;
    }

    /**
     * 原地建堆, 1. 将数组数据逐个放入堆中
     */
    public void heapify1() {
        for (T data : datas) {
            push(data);
        }
    }

    /**
     * 原地建堆, 2. 自上而下上滤
     * 注意由于是直接操作数组, 所以要考虑size
     */
    public void heapify2() {
        size = datas.length;
        for (int i = 0; i < datas.length; i++) {
            shiftUp(i);
        }
    }

    /**
     * 原地建堆, 3. 自下而上下滤 (推荐)
     * 注意由于是直接操作数组, 所以要考虑size
     */
    public void heapify3() {
        size = datas.length;
        int length = datas.length; // 忽略叶子结点
        for (int i = (length >> 1) - 1; i >= 0; i--) {
            shiftDown(i);
        }
    }

}
