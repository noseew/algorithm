package org.song.algorithm.algorithmbase.datatype.tree.model;

/**
 * 二叉树
 *
 * @param <V>
 */
public class BTreeNode<V> extends TreeNode<V> {

    public BTreeNode<V> parent;
    public BTreeNode<V> left;
    public BTreeNode<V> right;

    public BTreeNode(BTreeNode<V> parent, BTreeNode<V> left, BTreeNode<V> right, V v) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.v = v;
    }
}
