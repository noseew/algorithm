package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model_lab;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;

import java.util.Comparator;

public class Tree03_AVL_RatioTimes<V extends Comparable<V>> extends Tree03_AVL_base<V> {

    public Tree03_AVL_RatioTimes(Comparator<V> comparator) {
        super(comparator);
    }

    /**
     * AVL 树新增最多只需要调整1次
     *
     * @param v
     * @return
     */
    @Override
    public boolean push(V v) {
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
