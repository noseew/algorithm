package org.song.algorithm.algorithmbase.datatype.tree.model;

/**
 * AVL 平衡二叉查找树
 * 特点: 平衡, 增加旋转等操作
 *
 * @param <V>
 */
public class AVLTree<V> extends BSTreeNode<V> {

    public AVLTree(BTreeNode<V> parent, BTreeNode<V> left, BTreeNode<V> right, V v) {
        super(parent, left, right, v);
    }

}
