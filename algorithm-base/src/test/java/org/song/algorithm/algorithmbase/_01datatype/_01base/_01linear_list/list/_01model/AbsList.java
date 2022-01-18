package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

public abstract class AbsList<T> {

    protected AbsList() {
    }

    protected AbsList(int capacity) {
    }

    protected abstract void clean();

    protected abstract boolean isEmpty();

    protected abstract int length();

    protected abstract T get(int index);

    protected abstract int indexOf(T v);

    protected abstract void insert(T v, int index);

    protected abstract T delete(int index);
    
    protected abstract T delete(T v);

}
