package org.song.algorithm.base._01datatype._01base._02queue_stack._01model.stack;

import org.song.algorithm.base._01datatype._01base._01linear.list._01model.ArrayBase01;

public class Stack_Array_01<T> extends AbsStack<T> {

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
    public void push(T v) {
        if (array.isEmpty()) {
            array.add(v);
        } else {
            array.insert(v, 0);
        }
    }

    @Override
    public T pop() {
        if (array.isEmpty()) {
            return null;
        }
        return array.delete(0);
    }

    @Override
    public String toString() {
        return array.toString();
    }
}
