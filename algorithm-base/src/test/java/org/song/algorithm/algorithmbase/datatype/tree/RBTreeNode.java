package org.song.algorithm.algorithmbase.datatype.tree;

/**
 * 红黑树
 *
 * @param <V>
 */
public class RBTreeNode<V> extends BTreeNode<V> {

    private static final boolean RED   = false;
    private static final boolean BLACK = true;

    boolean color = BLACK;

    public RBTreeNode(BTreeNode<V> parent, BTreeNode<V> left, BTreeNode<V> right, V v) {
        super(parent, left, right, v);
    }
}
