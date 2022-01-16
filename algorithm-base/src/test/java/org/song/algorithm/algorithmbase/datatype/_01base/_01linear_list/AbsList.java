package org.song.algorithm.algorithmbase.datatype._01base._01linear_list;

public abstract class AbsList<T> {
    
    protected abstract void setNull();
    protected abstract void length();
    protected abstract void get(int index);
    protected abstract int indexOf(T v);
    protected abstract void insert(T v, int index);
    protected abstract T delete(int index);
    
}
