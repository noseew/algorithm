package org.song.algorithm.algorithmbase.datatype.tree.model;

import java.util.Comparator;

/**
 * 平衡二叉树
 *
 * @param <V>
 */
public class AVLTree_base<V> {

    private int size;

    private Comparator<V> comparator;

    private AVLTreeNode<V> root;

    public void push(V v) {
        if (root == null) {
            root = new AVLTreeNode<>(null, null, null, v, 0);
            size++;
            return;
        }

        AVLTreeNode<V> parent = root;
        while (true) {
            AVLTreeNode<V> next;
            if (comparator != null) {
                if (comparator.compare(v, parent.v) < 0) {
                    next = parent.left;
                } else {
                    next = parent.right;
                }
            } else {
                if (((Comparable) v).compareTo(((Comparable) parent.v)) < 0) {
                    next = parent.left;
                } else {
                    next = parent.right;
                }
            }
            if (next == null) {
                break;
            }
            parent = next;
        }

        put(parent, v);
        size++;
    }

    private void put(AVLTreeNode<V> parent, V v) {
        AVLTreeNode<V> newNode = new AVLTreeNode<>(parent, null, null, v, parent.height + 1);
        if (comparator != null) {
            if (comparator.compare(v, parent.v) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        } else {
            if (((Comparable) v).compareTo(((Comparable) parent.v)) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
    }

    private void balance(AVLTreeNode<V> node) {
        /*
         平衡二叉树的平衡分为
         RR: 单向右型: 需要左旋操作
         LR: 左右型: 先左旋再右旋
         LL: 单向左型: 需要右旋操作
         RL: 右左型: 先右旋再左旋
         */
    }

    /**
     * 右旋
     * 针对 LL
     *
     * @param node 不平衡的节点, isBalanced(node) = false
     */
    private void rightRotate(AVLTreeNode<V> node) {
        /*
         右旋: 需要操作两个节点
             node: 不平衡的节点
             newParent: 不平衡节点的左子节点
         右旋过程:
            1. 将 node 设置为 newParent 右子节点
            2. 将 node 的左子节点设置为 newParent 原右子节点
            3. 更新高度
         树节点的变更, 至少要操作2个指针才能确定位置安全和线程安全, 因此无法使用CAS操作来保证线程安全
         */
        AVLTreeNode<V> newParent = node.left;
        AVLTreeNode<V> v = newParent.right;
        newParent.right = node;
        node.left = v;

    }

    private boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(AVLTreeNode<V> node) {
        if (node == null) {
            return true;
        }
        int balanceFactory = Math.abs(getBalanceFactor(node));
        if (balanceFactory > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    private int getBalanceFactor(AVLTreeNode<V> node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    private int getHeight(AVLTreeNode<V> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    static class AVLTreeNode<V> {

        AVLTreeNode<V> parent;
        AVLTreeNode<V> left;
        AVLTreeNode<V> right;
        V v;
        int height;

        AVLTreeNode(AVLTreeNode<V> parent, AVLTreeNode<V> left, AVLTreeNode<V> right, V v, int height) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.v = v;
            this.height = height;
        }

    }
}
