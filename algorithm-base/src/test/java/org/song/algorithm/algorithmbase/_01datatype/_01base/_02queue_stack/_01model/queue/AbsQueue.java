package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue;

public abstract class AbsQueue<T> {

    protected AbsQueue() {
    }

    protected AbsQueue(int capacity) {
    }

    protected abstract void clean();

    protected abstract boolean isEmpty();

    protected abstract void length();

    protected abstract T getTop();
    
    protected abstract T getBottom();

    protected abstract void rpush(T v);

    protected abstract void lpush(T v);

    protected abstract T rpop();

    protected abstract T lpop();

}
