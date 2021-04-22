package org.song.algorithm.algorithmbase.datatype.tree.model;

/**
 * 红黑树
 * 特点: 介于 AVL 和 BST 之间的一种树, 比AVL平衡度低, 比BST平衡度高
 *
 * @param <V>
 */
public class RBTreeNode<V> extends AVLTree<V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;

    public boolean color = BLACK;

    public RBTreeNode(BTreeNode<V> parent, BTreeNode<V> left, BTreeNode<V> right, V v) {
        super(parent, left, right, v);
    }
}
