package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import java.util.Objects;

/**
 * 基于数组
 * 
 * 扩容采用复杂度均摊
 * 由于不够严谨, 这个均摊复杂度并没有提高效率, 仅仅是用于测试和实验
 * 什么时候使用均摊复杂度呢?
 * 连续多次操作都是低复杂度, 然后突然出现一个高复杂度的情况, 这种可以采用均摊复杂度来优化
 * 示例: 连续O(1), 突然O(n), 均摊后依然是O(1), 注意, 即使不均摊原平均也是O(1)
 *
 * @param <T>
 */
public class ArrayBase02<T> extends ArrayBase01<T> {

    protected T[] data2;
    // 复制进度, 从0开始, 截止到原数组末尾下表
    protected int process;
    // 是否正在复制
    protected boolean processing;
    

    public ArrayBase02() {
        this(10);
    }

    public ArrayBase02(int capacity) {
        super(capacity);
    }

    @Override
    public void clean() {
        size = 0;
        process = 0;
        processing = false;
        data2 = null;
        shrink();
    }
    
    @Override
    public T get(int index) {
        if (processing) {
            copy();
        }
        checkIndexBound(index);
        return currentData(index)[index];
    }

    @Override
    public void set(T v, int index) {
        if (processing) {
            copy();
        }
        checkIndexBound(index);
        currentData(index)[index] = v;
    }

    @Override
    public int indexOf(T v) {
        if (processing) {
            copy();
        }
        for (int i = 0; i < data.length; i++) {
            if (Objects.equals(currentData(i)[i], v)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void add(T v) {
        if (processing) {
            copy();
        }
        ensureCapacity();
        // 直接对主数组操作, 1. 空间足够, 2. 省的再次复制
        this.data[++size - 1] = v;
    }

    @Override
    public void insert(T v, int index) {
        throw new RuntimeException("暂时不支持的操作, 此类仅用作测试复杂度均摊");
    }

    @Override
    public T delete(int index) {
        throw new RuntimeException("暂时不支持的操作, 此类仅用作测试复杂度均摊");
    }

    @Override
    public T delete(T v) {
        throw new RuntimeException("暂时不支持的操作, 此类仅用作测试复杂度均摊");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(currentData(i)[i]);
            if (i != size - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return "size=" + size + ", datas=" + sb.toString();
    }

    /**
     * 获取访问的下标数据此时在哪个data中, 
     * 
     * @param index
     * @return
     */
    protected T[] currentData(int index) {
        if (!processing) {
            // 如果没有复制操作, 则走主数组
            return data;
        }
        // 还未复制的范围区域是 index >= process 且 index <= data2.length - 1
        
        if (index < process || index > data2.length - 1) {
            return data;
        }
        return data2;
    }
    
    /**
     * 确保容量
     */
    @Override
    protected void ensureCapacity() {
        if (size >= data.length) {
            if (processing) {
                // 如果出现, 扩容中且容量不够了, 则扩容倍数必须是原来的2倍
                throw new RuntimeException("不可能出现的异常");
            }
            dilatation();
            processing = true;
            copy();
        } else if (processing) {
            copy();
        }
        
    }

    /**
     * 扩容
     */
    protected void dilatation() {
        data2 = data;
        /*
        每次扩容一倍, 防止出现新数组已满, 但是还未转已完成的情况, 
        原数组扩容, 新的下标操作都在更长的原数组上操作
        复制方向 data2 -> data
         */
        data = (T[]) new Object[data.length << 1];
    }

    /**
     * 均摊复制, 每个操作(增删改查)都来均摊这个复制操作, 这就是均摊复杂度
     * 复制方向 data2 -> data
     */
    protected void copy() {
        if (!processing) {
            return;
        }
        if (process < data2.length) {
            // 均摊复制, 每次复制一次
            data[process] = data2[process];
            process++;
        } else {
            // 复制完成
            data2 = null;
            process = 0;
            processing = false;
        }
    }
}
