package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

public abstract class AbsLine<T> {

    public T[] datas;

    public int size;

    public AbsLine() {
    }

    public AbsLine(int capacity) {
        this.datas = (T[]) new Object[capacity];
    }

    public abstract void clean();

    public abstract boolean isEmpty();

    public abstract int length();

    public abstract T get(int index);

    public abstract int indexOf(T v);

    public abstract void add(T v);
    
    public abstract void insert(T v, int index);

    public abstract T delete(int index);
    
    public abstract T delete(T v);

}
