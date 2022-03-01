package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.AbsLine;

/**
 * 循环数组实现队列
 *
 * @param <T>
 */
public class Queue_CycleArray_01<T> extends AbsQueue<T> {

    /*
    循环数组, 
    数组初始化的时候给以固定的带下, 
    元素的存放位置通过起止下标来控制, 
        start: 队首
        end: 队尾的下一个位置(也就是下一个插入元素的位置)
        size = (end - start + max) % max
        队列是否满: start == end
    循环数组实现细节有几种情况
    1. 数组大小 == 最大限制
        存在问题: 判空和判满, 方式相同了, 容易产生歧义
            判空: start == end
            判满: start == end
    2. 数组大小 = 最大限制 + 1
        解决1的判空判满方式相同的问题
        存在问题: 
            1. 浪费一个空间
            2. +1后数组大小就可能不是2的次幂数, (为什么大小要设置为2次幂数? 一些场景为了提高效率)
    3. 可以通过增加一个容量字段来解决上述问题
     */
    protected T[] data;
    protected int start, end;
    protected int size;

    public Queue_CycleArray_01(int capacity) {
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
//        return start == end; // 采用起止下标来判断
        return size == 0; // 采用size大小来判断
    }

    @Override
    public boolean isFull() {
//        return start == end; // 采用起止下标来判断
        return size == data.length; // 采用size大小来判断
    }

    @Override
    public int length() {
//        return (end - start + data.length) % data.length; // 采用起止下标来判断
        return size; // 采用size大小来判断
    }

    @Override
    public T getTop() {
        if (isEmpty()) return null;
        return data[start];
    }

    @Override
    public T getBottom() {
        if (isEmpty()) return null;
        return data[end - 1];
    }

    @Override
    public void rpush(T v) {
        if (isFull()) throw new RuntimeException("队列已满");
        // 直接将元素放入end下标, 防止下标越界需要取余, 然后end后移
        data[end % data.length] = v;
        end++;
        size++;
    }

    @Override
    public void lpush(T v) {
        if (isFull()) throw new RuntimeException("队列已满");
        // start 前移, 防止出现负数 所以加上 length
        data[start = (start - 1 + data.length) % data.length] = v;
        size++;
    }

    @Override
    public T rpop() {
        if (isEmpty()) throw new RuntimeException("队列为空");
        // 直接将end元素取出, 防止出现负数 所以加上 length, 并恢复到可控范围内
        T v = data[end = (end - 1 + data.length) % data.length];
        size--;
        return v;
    }

    @Override
    public T lpop() {
        if (isEmpty()) throw new RuntimeException("队列为空");
        // 直接将start元素取出, 防止下标越界需要取余, 然后start后移
        T v = data[start % data.length];
        start++;
        size--;
        return v;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = start; i < end; i++) {
            sb.append(data[i % data.length]).append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
