package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.stack;

public abstract class AbsStack<T> {

    public AbsStack() {
    }

    public AbsStack(int capacity) {
    }

    public abstract void clean();

    public abstract boolean isEmpty();

    public abstract int length();

    public abstract T getTop();

    public abstract void push(T v);

    public abstract T pop();

}
