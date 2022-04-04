package org.song.algorithm.base._01datatype._01base._02queue_stack._01model.queue;

import org.song.algorithm.base._01datatype._01base._01linear.list._01model.LinkedDoubleCycle01;

/**
 * 链表实现循环队列
 * 
 * @param <T>
 */
public class Queue_CycleLink_01<T> extends AbsQueue<T> {

    private final LinkedDoubleCycle01<T> linked = new LinkedDoubleCycle01<>();
    private int capacity;
    
    public Queue_CycleLink_01(int capacity) {
        this.capacity = capacity;
    }
    
    @Override
    public void clean() {
        linked.clean();
    }

    @Override
    public boolean isEmpty() {
        return linked.isEmpty();
    }

    @Override
    public boolean isFull() {
        return linked.length() == capacity;
    }

    @Override
    public int length() {
        return linked.length();
    }

    @Override
    public T getTop() {
        if (linked.isEmpty()) {
            return null;
        }
        return linked.getHead().value;
    }

    @Override
    public T getBottom() {
        if (linked.isEmpty()) {
            return null;
        }
        return linked.getTail().value;
    }

    @Override
    public void rpush(T v) {
        if (isFull()) {
            throw new RuntimeException("队列已满");
        }
        linked.rpush(v);
    }

    @Override
    public void lpush(T v) {
        if (isFull()) {
            throw new RuntimeException("队列已满");
        }
        linked.lpush(v);
    }

    @Override
    public T rpop() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空");
        }
        return linked.rpop();
    }

    @Override
    public T lpop() {
        if (isEmpty()) {
            throw new RuntimeException("队列为空");
        }
        return linked.lpop();
    }

    @Override
    public String toString() {
        return linked.toString();
    }
}
