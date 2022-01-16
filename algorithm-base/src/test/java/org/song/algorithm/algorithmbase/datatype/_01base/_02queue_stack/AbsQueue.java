package org.song.algorithm.algorithmbase.datatype._01base._02queue_stack;

public abstract class AbsQueue<T> {

    protected AbsQueue() {
    }

    protected AbsQueue(int capacity) {
    }

    protected abstract void clean();

    protected abstract boolean isEmpty();

    protected abstract void length();

    protected abstract T getTop();

    protected abstract void push(T v);

    protected abstract T pop();

    protected abstract T delete(int index);

}
