package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

public class Tree03_AVL_Ratio1<V extends Comparable<V>> extends Tree03_AVL_base<V> {
    /**
     * AVL 平衡因子, 也就是左右子树高度差的绝对值, 默认是1, 当大于这个值时, 树需要调整, 否则不需要调整
     * -    是否可以通过调整平衡因子从而达到类似于红黑树的效果呢? 比如降低调整的频率
     * 测试结果表示, 并没有稳定的降低调整次数, 结果是不稳定的, 走题来说略有减小
     * 
     * 平衡率, 较大的子树/较小的子树的最大比值
     */
    private double balanceRatio = 2;

    public Tree03_AVL_Ratio1(Comparator<V> comparator) {
        super(comparator);
    }

    /**
     * 平衡判断和处理
     *
     * @param x
     * @return
     */
    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        if (x == null) {
            return x;
        }

        // 左高右低
        if (higher(x.left, x.right)) {
            if (getHeight(x.left.left) >= getHeight(x.left.right)) {
                // LL型 / 右旋转
                x = rotateRight(x);
            } else {
                // LR型 < 先左旋转再右旋转
//                node = leftRightRotate4LR(node);
                x.left = rotateLeft(x.left);
                x = rotateRight(x);
            }
        }
        // 右高左低
        else if (higher(x.right, x.left)) {
            if (getHeight(x.right.right) >= getHeight(x.right.left)) {
                // RR型 \ 左旋转
                x = rotateLeft(x);
            } else {
                // RL型 > 先右旋转再左旋转
//                node = rightLeftRotate4RL(node);
                x.right = rotateRight(x.right);
                x = rotateLeft(x);
            }
        }

        // 更新高度
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        return x;
    }

    private boolean higher(TreeNode<V> node1, TreeNode<V> node2) {
        int node1Height = getHeight(node1);
        int node2Height = getHeight(node2);
        node2Height = node2Height == 0 ? 1 : node2Height;
        return (double) (node1Height / node2Height) > balanceRatio;
    }
}
