package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

/**
 * 队列, 先进先出 FIFO
 * 
 * @param <T>
 */
public abstract class AbsQueue<T> {

    public AbsQueue() {
    }

    public AbsQueue(int capacity) {
    }

    public abstract void clean();

    public abstract boolean isEmpty();

    public abstract int length();

    public abstract T getTop();
    
    public abstract T getBottom();

    public abstract void rpush(T v);

    public abstract void lpush(T v);

    public abstract T rpop();

    public abstract T lpop();

}
