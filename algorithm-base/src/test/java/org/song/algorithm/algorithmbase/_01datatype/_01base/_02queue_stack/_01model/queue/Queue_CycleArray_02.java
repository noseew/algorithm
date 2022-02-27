package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.AbsLine;

/**
 * 循环数组实现队列
 * 优化效率
 *
 * @param <T>
 */
public class Queue_CycleArray_02<T> extends AbsQueue<T> {

    /*
    循环数组, 
    数组大小必须是2次幂数
     */
    private T[] data;
    private int start, end;
    private int size;
    private int capacity;

    public Queue_CycleArray_02(int capacity) {
        capacity = upPower(capacity);
        data = (T[]) new Object[capacity];
    }

    @Override
    public void clean() {
        AbsLine.fill(data, null);
        start = 0;
        end = 0;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // 采用size大小来判断
    }

    @Override
    public boolean isFull() {
        return size == data.length; // 采用size大小来判断
    }

    @Override
    public int length() {
        return size; // 采用size大小来判断
    }

    @Override
    public T getTop() {
        if (isEmpty()) {
            return null;
        }
        return data[start];
    }

    @Override
    public T getBottom() {
        if (isEmpty()) {
            return null;
        }
        return data[end - 1];
    }

    @Override
    public void rpush(T v) {
        if (isFull()) {
            throw new RuntimeException("队列已满");
        }
        // 直接将元素放入end下标, 防止下标越界需要取余, 然后end后移
        data[end & (capacity - 1)] = v;
        end++;
        size++;
    }

    @Override
    public void lpush(T v) {
        throw new RuntimeException("暂不支持双端队列");
    }

    @Override
    public T rpop() {
        throw new RuntimeException("暂不支持双端队列");
    }

    @Override
    public T lpop() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空");
        }
        // 直接将start元素取出, 防止下标越界需要取余, 然后start后移
        T v = data[start & (capacity - 1)];
        start++;
        size--;
        return v;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = start; i < end; i++) {
            sb.append(data[i & (capacity - 1)]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static int upPower(int n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 16 : n + 1;
    }
}
