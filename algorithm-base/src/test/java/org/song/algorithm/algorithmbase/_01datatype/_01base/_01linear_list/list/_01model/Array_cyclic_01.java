package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

/**
 * 循环数组
 * 
 * @param <T>
 */
public class Array_cyclic_01<T> extends AbsList<T> {

    private T[] datas;

    private int size;
    
    @Override
    protected void clean() {
        
    }

    @Override
    protected boolean isEmpty() {
        return false;
    }

    @Override
    protected int length() {
        return 0;
    }

    @Override
    protected T get(int index) {
        return null;
    }

    @Override
    protected int indexOf(T v) {
        return 0;
    }

    @Override
    protected void insert(T v, int index) {

    }

    @Override
    protected T delete(int index) {
        return null;
    }

    @Override
    protected T delete(T v) {
        return null;
    }
}
