package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

import java.util.Arrays;
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
public class Array_base_01<T> extends AbsLine<T> {

    public Array_base_01() {
        this(10);
    }

    public Array_base_01(int capacity) {
        super(capacity);
    }

    @Override
    public void clean() {
        for (int i = 0; i < size; i++) {
            datas[i] = null;
        }
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
        if (datas.length - 1 < index || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return datas[index];
    }

    @Override
    public int indexOf(T v) {
        for (int i = 0; i < datas.length; i++) {
            if (Objects.equals(datas[i], v)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void add(T data) {
        ensureCapacity();
        datas[++size - 1] = data;
    }

    @Override
    public void insert(T v, int index) {
        if (datas.length - 1 < index || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        ensureCapacity();
        // 插入元素, 后续元素后移
        for (int i = size - 1; i >= index; i--) {
            datas[i + 1] = datas[i];
            if (i == index) {
                datas[i] = v;
            }
        }
        size++;
    }

    @Override
    public T delete(int index) {
        if (index <= size - 1) {
            T oldVal = datas[index];
            // 删除元素, 后续元素前移
            for (int j = index; j < size; j++) {
                datas[j] = datas[j + 1];
                if (j == size - 1) {
                    datas[j] = null;
                }
            }
            size--;
            shrink();
            return oldVal;
        }
        return null;
    }

    @Override
    public T delete(T value) {
        if (value == null) {
            for (int i = 0; i < size; i++) {
                if (datas[i] == null) {
                    for (int j = i; j < size; j++) {
                        datas[j] = datas[j + 1];
                        if (j == size - 1) {
                            datas[j] = null;
                        }
                    }
                    size--;
                    shrink();
                    return null;
                }
            }
            return null;
        } else {
            for (int i = 0; i < size; i++) {
                if (datas[i] == value) {
                    T oldVal = datas[i];
                    for (int j = i; j < size; j++) {
                        datas[j] = datas[j + 1];
                        if (j == size - 1) {
                            datas[j] = null;
                        }
                    }
                    size--;
                    shrink();
                    return oldVal;
                }
            }
            return null;

        }
    }

    /**
     * 确保容量
     */
    private void ensureCapacity() {
        if (size >= datas.length - 1) {
            dilatation();
        }
    }

    /**
     * 扩容
     */
    private void dilatation() {
        T[] newData = (T[]) new Object[datas.length + (datas.length >> 1)];
        System.arraycopy(datas, 0, newData, 0, datas.length);
        datas = newData;
    }

    /**
     * 收缩
     */
    private void shrink() {
        double ratio = 0.3;
        if ((double) size / (double) datas.length < ratio) {
            T[] newData = (T[]) new Object[datas.length >> 1];
            if (size > 0) {
                System.arraycopy(datas, 0, newData, 0, size);
            }
            datas = newData;
        }
    }

    @Override
    public String toString() {
        return "size=" + size + ", datas=" + Arrays.toString(datas);
    }
}
