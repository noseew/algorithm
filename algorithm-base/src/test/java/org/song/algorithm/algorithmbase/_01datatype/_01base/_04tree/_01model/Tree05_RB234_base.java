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
    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        if (x == null) {
            return x;
        }
        // 右红左黑: 左旋 == 情况 2.2
        if (isRed(x.right) && !isRed(x.left)) {
            x = rotateLeft(x);
        }
        if (isRed(x.left) && !isRed(x.right)) {
            x = rotateRight(x);
        }
        // 左红左左红: 右旋 == 情况 2.1
        if (isRed(x.left) && isRed(x.left.left)) {
            x = rotateRight(x);
        }
        if (isRed(x.right) && isRed(x.right.right)) {
            x = rotateLeft(x);
        }
        // 左红右红: 变色 == 情况 1
        if (isRed(x.left) && isRed(x.right)) {
            flipColors(x);
        }

        return x;
    }

}
