package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

可以通过理解234树来理解红黑树的旋转和变色

 */
public class Tree05_RB_base<V extends Comparable<V>> extends Tree05_RB_abs<V> {

    public Tree05_RB_base(Comparator<V> comparator) {
        super(comparator);
    }

}
