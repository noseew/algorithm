package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

public abstract class AbsLine<T> {

    public AbsLine() {
    }

    public AbsLine(int capacity) {
    }

    public abstract void clean();

    public abstract boolean isEmpty();

    public abstract int length();

    public abstract T get(int index);

    public abstract void set(T v, int index);

    public abstract int indexOf(T v);

    public abstract void add(T v);
    
    public abstract void insert(T v, int index);

    public abstract T delete(int index);
    
    public abstract T delete(T v);

    public static void fill(Object[] a, Object val) {
        for (int i = 0, len = a.length; i < len; i++)
            a[i] = val;
    }

}
