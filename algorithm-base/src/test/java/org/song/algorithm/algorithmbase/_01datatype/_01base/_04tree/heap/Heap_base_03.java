package org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap;

/**
 * heap 原地建堆
 */
public class Heap_base_03<T> extends AbstractHeap<T> {

    public Heap_base_03(T[] datas) {
        this(true, datas);
    }

    public Heap_base_03(boolean little, T[] datas) {
        this.little = little;
        this.datas = datas;
    }

    /**
     * 原地建堆, 将数组数据逐个放入堆中
     */
    public void start() {
        for (T data : datas) {
            push(data);
        }
    }

    protected void push(T v) {
        // 新元素存储到数组下一位,
        datas[size++] = v;
        if (size == 1) {
            // 只有一个元素
            return;
        }
        shiftUp(size - 1);
    }

    protected T pop() {
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

}
