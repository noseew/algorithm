package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import java.util.Objects;

/**
 * 基于数组的链表
 *
 * 优点
 * 1. 随机读写复杂度 O(1)
 * 缺点:
 * 1. 扩容复杂度 O(n)
 * 2. 删除复杂度 O(n)
 * 解决:
 * 1. 扩容复杂度均摊, 将扩容的复制元素均摊到每次操作, 最终复杂度还是 O(1)
 * 2. 采用动态指针表示头尾, 解决删除(头尾)元素复杂度降为 O(1), 删除中间元素复杂度降低一半还是 O(n)
 *
 * @param <T>
 */
public class ArrayBase01<T> extends AbsLine<T> {

    protected T[] data;

    protected int size;

    public ArrayBase01() {
        this(10);
        data = (T[]) new Object[10];
    }

    public ArrayBase01(int capacity) {
        super(capacity);
        data = (T[]) new Object[capacity];
    }

    @Override
    public void clean() {
        size = 0;
        shrink();
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public T get(int index) {
        checkIndexBound(index);
        return data[index];
    }

    @Override
    public void set(T v, int index) {
        checkIndexBound(index);
        data[index] = v;
    }

    @Override
    public int indexOf(T v) {
        for (int i = 0; i < data.length; i++) {
            if (Objects.equals(data[i], v)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void add(T v) {
        ensureCapacity();
        this.data[++size - 1] = v;
    }

    @Override
    public void insert(T v, int index) {
        checkIndexBound(index);
        ensureCapacity();
        // 插入元素, 后续元素后移
        rightMove(index);
        data[index] = v;
        size++;
    }

    @Override
    public T delete(int index) {
        checkIndexBound(index);
        
        if (index <= size - 1) {
            T oldVal = data[index];
            // 删除元素, 后续元素前移
            leftMove(index);
            size--;
            shrink();
            return oldVal;
        }
        return null;
    }

    @Override
    public T delete(T v) {
        if (v == null) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            if (data[i] == v) {
                T oldVal = data[i];
                leftMove(i);
                size--;
                shrink();
                return oldVal;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("size=").append(size).append("\r\n");
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i != size - 1) {
                sb.append(",").append("\r\n");
            }
        }
        sb.append("\r\n").append("]");
        return sb.toString();
    }

    /**
     * 元素左移, 从index开始
     * 
     * @param index
     */
    protected void leftMove(int index) {
        for (int j = index; j < size; j++) {
            data[j] = data[j + 1];
            if (j == size - 1) {
                data[j] = null;
            }
        }
    }

    /**
     * 元素右移, 截止到index
     *
     * @param index
     */
    protected void rightMove(int index) {
        for (int i = size - 1; i >= index; i--) {
            data[i + 1] = data[i];
        }
    }

    /**
     * 确保容量
     */
    protected void ensureCapacity() {
        if (size >= data.length - 1) {
            dilatation();
        }
    }

    /**
     * 扩容
     */
    protected void dilatation() {
        int append = data.length >> 1;
        append = append <= 0 ? 1 : append;
        T[] newData = (T[]) new Object[data.length + append];
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }

    /**
     * 收缩
     */
    protected void shrink() {
        double ratio = 0.3;
        if ((double) size / (double) data.length < ratio) {
            T[] newData = (T[]) new Object[data.length >> 1];
            if (size > 0) {
                System.arraycopy(data, 0, newData, 0, size);
            }
            data = newData;
        }
    }

    protected void checkIndexBound(int index) {
        if (index + 1 > size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
    }
}
