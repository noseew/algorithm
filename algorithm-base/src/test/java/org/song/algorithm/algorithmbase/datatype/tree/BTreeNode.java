package org.song.algorithm.algorithmbase.datatype.tree;

public class BTreeNode<V> {

    BTreeNode<V> parent;
    BTreeNode<V> left;
    BTreeNode<V> right;
    V v;

    public BTreeNode(BTreeNode<V> parent, BTreeNode<V> left, BTreeNode<V> right, V v) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.v = v;
    }
}
