package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.Comparator;
import java.util.List;

public abstract class _02BSTTreeBase<V> extends _01TreeBase<V> {

    public Comparator<V> comparator;

    public _02BSTTreeBase(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    public abstract V max();
    public abstract V min();
    public abstract V floor(V v);
    public abstract V ceiling(V v);
    public abstract int rank(V v);
    public abstract List<V> range(V min, V max);
    
}
