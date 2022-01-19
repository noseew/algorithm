package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.Comparator;
import java.util.List;

/*
一棵二叉查找树（BST）是一棵二叉树，其中每个结点都含有一个Comparable的键（以及相关联的值）且每个结点的键都大于其左子树中的任意结点的键而小于右子树的任意结点的键。
 */
public class Tree02_BST_base<V extends Comparable<V>> extends _02BSTTreeBase<V> {

    private int size;

    public TreeNode<V> root;

    public Tree02_BST_base(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public void push(V v) {
        if (root == null) {
            root = new TreeNode<>( null, null, v);
            size++;
            return;
        }

        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
            if (less(v, parent.val)) {
                next = parent.left;
            } else {
                next = parent.right;
            }
            if (next == null) {
                break;
            }
            parent = next;
        }

        put(parent, v);
        size++;
    }

    @Override
    public V get(V v) {
        if (root == null || v == null) {
            return null;
        }

        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
            if (less(v, parent.val)) {
                next = parent.left;
            } else if (greater(v, parent.val)) {
                next = parent.right;
            } else {
                return parent.val;
            }
            if (next == null) {
                return null;
            }
            parent = next;
        }
    }

    @Override
    public V remove(V v) {
        // TODO
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V max() {
        if (root == null) {
            return null;
        }

        TreeNode<V> max = root;
        while (max.right != null) {
            max = max.right;
        }
        return max.val;
    }

    @Override
    public V min() {
        if (root == null) {
            return null;
        }

        TreeNode<V> max = root;
        while (max.left != null) {
            max = max.left;
        }
        return max.val;
    }

    @Override
    public V floor(V v) {
        // TODO
        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
            if (less(v, parent.val)) {
                next = parent.left;
            } else if (greater(v, parent.val) && parent.right != null && greater(v, parent.right.val)) {
                next = parent.right;
            } else {
                return parent.val;
            }
            if (next == null) {
                return null;
            }
            parent = next;
        }
    }

    @Override
    public V ceiling(V v) {
        // TODO
        TreeNode<V> parent = root;
        while (true) {
            TreeNode<V> next;
            if (greater(v, parent.val)) {
                next = parent.right;
            } else if (less(v, parent.val) && parent.left != null && less(v, parent.left.val)) {
                next = parent.left;
            } else {
                return parent.val;
            }
            if (next == null) {
                return null;
            }
            parent = next;
        }
    }

    @Override
    public int rank(V v) {
        return 0;
    }

    @Override
    public List<V> range(V min, V max) {
        return null;
    }

    private void put(TreeNode<V> parent, V v) {
        TreeNode<V> newNode = new TreeNode<>( null, null, v);
        if (comparator != null) {
            if (comparator.compare(v, parent.val) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        } else {
            if (((Comparable) v).compareTo(((Comparable) parent.val)) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
    }
}
