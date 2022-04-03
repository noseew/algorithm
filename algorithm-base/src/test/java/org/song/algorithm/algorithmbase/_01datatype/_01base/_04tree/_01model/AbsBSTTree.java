package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbsBSTTree<V extends Comparable<V>> {

    public static final Comparator<Integer> INT_C = Comparator.comparing(Integer::intValue);
    public static final Comparator<Double> DOUBLE_C = Comparator.comparing(Double::doubleValue);

    public Comparator<V> comparator;

    public AbsBSTTree(Comparator<V> comparator) {
        this.comparator = comparator;
    }

    public abstract boolean add(V v);
    public abstract V get(V v);
    public abstract V remove(V v);
    public abstract void clear();
    public abstract int size();

    public abstract V max();
    public abstract V min();
    public abstract V removeMax();
    public abstract V removeMin();
    public abstract V floor(V v);
    public abstract V ceiling(V v);
    public abstract int rank(V v);
    public abstract List<V> range(V min, V max);

    public TreeNode<V> newNode(V v) {
        return new TreeNode<>(v);
    }

    public boolean less(V v1, V v2) {
        return comparator.compare(v1, v2) < 0;
    }

    public boolean greater(V v1, V v2) {
        return comparator.compare(v1, v2) > 0;
    }

    public boolean eq(V v1, V v2) {
        return comparator.compare(v1, v2) == 0;
    }

    public int compare(V v1, V v2) {
        return comparator.compare(v1, v2);
    }

    /**
     * 二叉树遍历
     * 
     * @param node 节点
     * @param order 顺序 0-前序, 1-中序, 2-后序
     * @param goon 执行操作, true-继续遍历, false-终止遍历
     */
    public static <V extends Comparable<V>> void traverse(TreeNode<V> node, Order order, Predicate<TreeNode<V>> goon) {
        if (node == null) return;
        if (order == Order.PreOrder && !goon.test(node)) return;
        traverse(node.left, order, goon);
        if (order == Order.MidOrder && !goon.test(node)) return;
        traverse(node.right, order, goon);
        if (order == Order.PostOrder && !goon.test(node)) return;
    }
    
    public enum Order {
        PreOrder, MidOrder, PostOrder,;
    }
}
