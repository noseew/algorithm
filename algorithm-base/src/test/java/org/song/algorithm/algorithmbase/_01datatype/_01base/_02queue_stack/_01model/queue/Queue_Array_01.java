package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;

/**
 * 基于动态数组实现队列
 * 如果队列的大小有限制, 则需要使用固定大小, 而不是动态扩容或者缩容, 
 * 如果固定大小还使用数组实现的话, 动态的位置调整导致效率低下, 
 * 如果不进行动态调整又会出现假溢出, 
 * 改进方法可以参考循环数组
 * 
 * @param <T>
 */
public class Queue_Array_01<T> extends AbsQueue<T> {

    private ArrayBase01<T> array = new ArrayBase01<>();
    
    @Override
    public void clean() {
        array.clean();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public int length() {
        return array.length();
    }

    @Override
    public T getTop() {
        if (array.isEmpty()) {
            return null;
        }
        return array.get(0);
    }

    @Override
    public T getBottom() {
        if (array.isEmpty()) {
            return null;
        }
        return array.get(array.length() - 1);
    }

    @Override
    public void rpush(T v) {
        array.add(v);
    }

    @Override
    public void lpush(T v) {
        if (array.isEmpty()) {
            array.add(v);
        } else {
            array.insert(v, 0);
        } 
    }

    @Override
    public T rpop() {
        if (array.isEmpty()) {
            return null;
        } else {
            return array.delete(array.length() - 1);
        } 
    }

    @Override
    public T lpop() {
        if (array.isEmpty()) {
            return null;
        } else {
            return array.delete(0);
        }
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
