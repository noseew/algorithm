package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
重构了 Tree02BST02 中的remove方法
 */
public class Tree02BST03<V extends Comparable<V>> extends Tree02BST02<V> {

    public Tree02BST03(Comparator<V> comparator) {
        super(comparator);
    }

    /**
     * Tree02BST02 中的remove方法已经非常精简, 这里重写将其更加贴近红黑树中删除, 
     *
     * @param x
     */
    protected void remove(TreeNode<V> x) {
        // 待删除节点
        if (x == null) return;

        // 删除 度为2的节点
        if (x.right != null && x.left != null) {
            TreeNode<V> successor = successor(x);
            x.val = successor.val;
            x = successor;
        }
        TreeNode<V> replacement = x.right != null ? x.right : x.left;

        if (replacement != null) {
            // 度为1
            if (x.parent == null) {
                root = replacement;
            }else if (isLeft(x.parent, x)) {
                x.parent.left = replacement;
            } else {
                x.parent.right = replacement;
            }
            replacement.parent = x.parent;
        } else if (x.parent == null) {
            root = null; // 删除根节点, 则替换root
        } else {
            // 度为0
            if (isLeft(x.parent, x)) {
                x.parent.left = null;
            } else {
                x.parent.right = null;
            }
        }
        size--;
    }
}
