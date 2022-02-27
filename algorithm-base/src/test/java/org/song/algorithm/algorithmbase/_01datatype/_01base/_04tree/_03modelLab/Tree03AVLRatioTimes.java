package org.song.algorithm.algorithmbase._01datatype._01base._04tree._03modelLab;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03AVL;

import java.util.Comparator;

public class Tree03AVLRatioTimes<V extends Comparable<V>> extends Tree03AVL<V> {

    public Tree03AVLRatioTimes(Comparator<V> comparator) {
        super(comparator);
    }

    /**
     * AVL 树新增最多只需要调整1次
     *
     * @param v
     * @return
     */
    @Override
    public boolean add(V v) {
        int size = this.size;
        root = insert_recursive(root, v);
        return size < this.size;
    }

    @Override
    public V remove(V v) {
        int size = this.size;
        root = remove_recursive(root, v);
        return size > this.size ? v : null;
    }
    
    public void resetRotateTimes() {
        rotateTimes = 0;
    }

}
