package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

TODO 未完成

 */
public class Tree05_RB234_base<V extends Comparable<V>> extends Tree05_RB23_base<V> {

    public Tree05_RB234_base(Comparator<V> comparator) {
        super(comparator);
    }


    @Override
    public boolean push(V v) {
        int size = this.size;
        root = insert_recursive(root, v);
        // 根节点总为黑色
        root.color = BLACK;
        return size > this.size;
    }

    /**
     * 采用递归的方式, 插入节点
     *
     * @param parent
     * @param v
     * @return
     */
    protected TreeNode<V> insert_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            // 新建节点
            parent = new TreeNode<>(null, null, v, RED);
            size++;
            return parent;
        }

        //
        if (isRed((TreeNode<V>) parent.left) && isRed((TreeNode<V>) parent.right)) {
            flipColors(parent);
        }

        if (less(v, parent.val)) {
            // 向左插入
            parent.left = insert_recursive((TreeNode<V>) parent.left, v);
        } else if (greater(v, parent.val)) {
            // 向右插入
            parent.right = insert_recursive((TreeNode<V>) parent.right, v);
        } else {
            parent.val = v; // 重复元素不处理 直接替换值
            return parent;
        }
        // 递归从叶子结点向上, 逐个判断
        parent = (TreeNode<V>) balance(parent);
        return parent;
    }

}
