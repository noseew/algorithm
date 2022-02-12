package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

可以通过理解234树来理解红黑树的旋转和变色

 */
public class Tree05_RB234_base<V extends Comparable<V>> extends Tree05_RB23_base<V> {

    public Tree05_RB234_base(Comparator<V> comparator) {
        super(comparator);
    }

    /**
     * 平衡判断和处理
     */
    @Override
    protected TreeNode<V> balanceInsertion(TreeNode<V> node) {
        if (node == null) {
            return node;
        }
        // 右红左黑: 左旋 == 情况 2.2
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }
        if (isRed(node.left) && !isRed(node.right)) {
            node = rotateRight(node);
        }
        // 左红左左红: 右旋 == 情况 2.1
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }
        if (isRed(node.right) && isRed(node.right.right)) {
            node = rotateLeft(node);
        }
        // 左红右红: 变色 == 情况 1
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
    }

}
