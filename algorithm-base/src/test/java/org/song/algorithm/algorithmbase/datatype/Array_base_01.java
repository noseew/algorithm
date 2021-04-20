package org.song.algorithm.algorithmbase.datatype;

import java.util.Arrays;

public class Array_base_01<T> {

    private T[] datas;

    private int size;

    public Array_base_01(int capacity) {
        datas = (T[]) new Object[capacity];
    }

    public Array_base_01() {
        this(10);
    }

    public void add(T data) {
        ensureCapacity();
        datas[++size - 1] = data;
    }

    public T remove(int index) {
        if (index <= size - 1) {
            T oldVal = datas[index];
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

    public T remove(T value) {
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

    public void clear() {
        for (int i = 0; i < size; i++) {
            datas[i] = null;
        }
        size = 0;
        shrink();

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
        return "size=" + size +
                "\r\ndatas=" + Arrays.toString(datas);
    }
}
