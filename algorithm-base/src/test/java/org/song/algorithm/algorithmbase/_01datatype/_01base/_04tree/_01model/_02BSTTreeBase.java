package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public abstract class _02BSTTreeBase<V> extends _01TreeBase<V> {

    public Comparator<V> comparator;

    public _02BSTTreeBase(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    public abstract V max();
    public abstract V min();
    public abstract V floor(V v);
    public abstract V ceiling(V v);
    public abstract int rank(V v);
    public abstract List<V> range(V min, V max);

    public boolean less(V v1, V v2) {
        return comparator.compare(v1, v2) < 0;
    }

    public boolean greater(V v1, V v2) {
        return comparator.compare(v1, v2) > 0;
    }

    /**
     * 二叉树遍历
     * 
     * @param node 节点
     * @param order 顺序 0-前序, 1-中序, 2-后序
     * @param stop 执行操作, true-继续遍历, false-终止遍历
     */
    public void traverse(TreeNode<V> node, Order order, Predicate<V> stop) {
        if (node == null) return;
        if (order == Order.PreOrder && !stop.test(node.val)) return;
        traverse(node.left, order, stop);
        if (order == Order.MidOrder && !stop.test(node.val)) return;
        traverse(node.right, order, stop);
        if (order == Order.PostOrder && !stop.test(node.val)) return;
    }
    
    protected enum Order {
        PreOrder, MidOrder, PostOrder,;
    }
}
