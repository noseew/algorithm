package org.song.algorithm.algorithmbase.datatype._01base._04tree.model;

public class TreeNode<V> {

    public TreeNode<V> parent;
    public TreeNode<V> left;
    public TreeNode<V> right;
    public V v;
    /*
     树的高度: 从最低的叶子节点开始, 由0开始, 如果左右子树长度不一样, 则以最长的为准
     区别于树的深度: 根节点的深度为0, 每个叶子节点的深度不一样
     有的定义是从1开始
     */
    public int height;
    public int balanceFactor;

    TreeNode(TreeNode<V> parent, TreeNode<V> left, TreeNode<V> right, V v) {
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.v = v;
    }
}
