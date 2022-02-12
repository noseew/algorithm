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
     * @param node
     * @return
     */
    protected TreeNode<V> balanceInsertion(TreeNode<V> node) {
        if (node == null) {
            return node;
        }

        // 左高右低
        if (higher(node.left, node.right)) {
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                // LL型 / 右旋转
                node = rightRotate4LL(node);
            } else {
                // LR型 < 先左旋转再右旋转
//                node = leftRightRotate4LR(node);
                node.left = leftRotation4RR(node.left);
                node = rightRotate4LL(node);
            }
        }
        // 右高左低
        else if (higher(node.right, node.left)) {
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                // RR型 \ 左旋转
                node = leftRotation4RR(node);
            } else {
                // RL型 > 先右旋转再左旋转
//                node = rightLeftRotate4RL(node);
                node.right = rightRotate4LL(node.right);
                node = leftRotation4RR(node);
            }
        }

        // 更新高度
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    private boolean higher(TreeNode<V> node1, TreeNode<V> node2) {
        int node1Height = getHeight(node1);
        int node2Height = getHeight(node2);
        node2Height = node2Height == 0 ? 1 : node2Height;
        return (double) (node1Height / node2Height) > balanceRatio;
    }
}
