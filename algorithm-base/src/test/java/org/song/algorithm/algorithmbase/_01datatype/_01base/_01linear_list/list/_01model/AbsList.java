package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

public abstract class AbsList<T> {

    public T[] datas;

    public int size;

    protected AbsList() {
    }

    protected AbsList(int capacity) {
        this.datas = (T[]) new Object[capacity];
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
