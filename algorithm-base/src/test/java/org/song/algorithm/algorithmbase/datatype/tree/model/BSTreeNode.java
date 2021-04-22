package org.song.algorithm.algorithmbase.datatype.tree.model;

import java.util.Comparator;

/**
 * BST 二叉查找树,
 * 特点: 有序
 *
 * @param <V>
 */
public class BSTreeNode<V> extends BTreeNode<V> {

    private Comparator<V> comparator;

    public BSTreeNode(BTreeNode<V> parent, BTreeNode<V> left, BTreeNode<V> right, V v) {
        super(parent, left, right, v);
    }
}
