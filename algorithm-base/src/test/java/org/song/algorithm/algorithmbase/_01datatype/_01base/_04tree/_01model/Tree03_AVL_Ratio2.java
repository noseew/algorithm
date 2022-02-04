package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

public class Tree03_AVL_Ratio2<V extends Comparable<V>> extends Tree03_AVL_Ratio1<V> {

    /**
     * AVL 平衡因子, 也就是左右子树高度差的绝对值, 默认是1, 当大于这个值时, 树需要调整, 否则不需要调整
     * -    是否可以通过调整平衡因子从而达到类似于红黑树的效果呢? 比如降低调整的频率
     * 测试结果表示, 并没有稳定的降低调整次数, 结果是不稳定的, 走题来说略有减小
     * 
     * 平衡率, 较大的子树/较小的子树的差值
     */
    private double balanceRatio = 3;

    public Tree03_AVL_Ratio2(Comparator<V> comparator) {
        super(comparator);
    }

    private boolean higher(TreeNode<V> node1, TreeNode<V> node2) {
        int node1Height = getHeight(node1);
        int node2Height = getHeight(node2);
        return node1Height - node2Height > balanceRatio;
    }
}
