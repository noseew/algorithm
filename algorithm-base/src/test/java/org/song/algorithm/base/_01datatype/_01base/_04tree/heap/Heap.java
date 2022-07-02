package org.song.algorithm.base._01datatype._01base._04tree.heap;

/*
二叉堆（Binary Heap，完全二叉堆）
多叉堆（D-heap、D-ary Heap）
索引堆（Index Heap）
二项堆（Binomial Heap）
斐波那契堆（Fibonacci Heap）
左倾堆（Leftist Heap，左式堆）
斜堆（Skew Heap）
 */
public interface Heap<T> {
    
    void push(T v);

    T pop();

    T replace(T v);

    T getTop();

    int getSize();

    boolean isEmpty();

    boolean isFull();
}
