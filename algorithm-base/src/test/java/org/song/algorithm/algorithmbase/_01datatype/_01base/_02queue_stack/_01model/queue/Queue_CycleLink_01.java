package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.Linked_CycleDouble_01;

/**
 * 链表实现循环队列
 * 
 * @param <T>
 */
public class Queue_CycleLink_01<T> extends AbsQueue<T> {

    private Linked_CycleDouble_01<T> linked = new Linked_CycleDouble_01<>();
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

    @Deprecated
    @Override
    public void lpush(T v) {
        throw new RuntimeException("暂不支持双端队列");
    }

    @Deprecated
    @Override
    public T rpop() {
        throw new RuntimeException("暂不支持双端队列");
    }

    @Override
    public T lpop() {
        if (isEmpty()) {
            throw new RuntimeException("队列已满");
        }
        return linked.lpop();
    }

    @Override
    public String toString() {
        return linked.toString();
    }
}
