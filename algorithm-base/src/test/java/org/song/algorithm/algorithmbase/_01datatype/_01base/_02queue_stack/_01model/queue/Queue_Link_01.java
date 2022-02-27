package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.LinkedDouble01;

/**
 * 链表实现队列
 * 
 * @param <T>
 */
public class Queue_Link_01<T> extends AbsQueue<T> {

    private LinkedDouble01<T> linked = new LinkedDouble01<>();
    
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
        return false;
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
        linked.rpush(v);
    }

    @Override
    public void lpush(T v) {
        linked.lpush(v);
    }

    @Override
    public T rpop() {
        return linked.rpop();
    }

    @Override
    public T lpop() {
        return linked.lpop();
    }

    @Override
    public String toString() {
        return linked.toString();
    }
}
