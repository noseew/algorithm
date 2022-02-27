package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model;

public abstract class AbsStack<T> {

    protected AbsStack() {
    }

    protected AbsStack(int capacity) {
    }

    protected abstract void clean();

    protected abstract boolean isEmpty();

    protected abstract void length();

    protected abstract T getTop();

    protected abstract void push(T v);

    protected abstract T pop();

}
