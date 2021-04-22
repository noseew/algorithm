package org.song.algorithm.algorithmbase.datatype.tree.model;

import java.util.Comparator;

/**
 * AVL 树, 有序树
 *
 * @param <V>
 */
public class AVLTree<V> extends BTreeNode<V> {

    private Comparator<V> comparator;

    public AVLTree(BTreeNode<V> parent, BTreeNode<V> left, BTreeNode<V> right, V v) {
        super(parent, left, right, v);
    }

    public Comparator<V> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<V> comparator) {
        this.comparator = comparator;
    }
}
