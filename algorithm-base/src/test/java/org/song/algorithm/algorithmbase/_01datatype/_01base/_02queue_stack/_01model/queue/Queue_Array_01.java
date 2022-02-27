package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.Array_base_01;

/**
 * 数组实现队列
 * 
 * @param <T>
 */
public class Queue_Array_01<T> extends AbsQueue<T> {

    private Array_base_01<T> array = new Array_base_01<>();
    
    @Override
    public void clean() {
        array.clean();
    }

    @Override
    public boolean isEmpty() {
        return array.isEmpty();
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
        return array.get(array.size - 1);
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
            return array.delete(array.size - 1);
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
