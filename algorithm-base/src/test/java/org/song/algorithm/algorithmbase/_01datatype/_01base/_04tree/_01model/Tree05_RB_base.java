package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

模板实现, 尤其是旋转和平衡

 */
public class Tree05_RB_base<V extends Comparable<V>> extends Tree05_RB_abs<V> {

    public Tree05_RB_base(Comparator<V> comparator) {
        super(comparator);
    }

    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {

        while (x != null && x != root && isRed(x.parent)) {
            if (x.parent == x.parent.parent.left) {
                if (isRed(x.parent.parent.right)) {
                    setBlack(x.parent);
                    setRed(x.parent.parent);
                    setBlack(x.parent.parent.right);
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        leftRotate(x);
                    }
                    setBlack(x.parent);
                    setRed(x.parent.parent);
                    rightRotate(x.parent.parent);
                }
            } else {
                if (isRed(x.parent.parent.left)) {
                    setBlack(x.parent);
                    setRed(x.parent.parent);
                    setBlack(x.parent.parent.left);
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.parent.left) {
                        x = x.parent;
                        rightRotate(x);
                    }
                    setBlack(x.parent);
                    setRed(x.parent.parent);
                    leftRotate(x.parent.parent);
                }
            }
        }
        return x;
    }

    protected TreeNode<V> balanceDeletion(TreeNode<V> x) {
        while (x != root && isBlack(x)) {
            if (x == x.parent.left) {
                if (isRed(x.parent.right)) {
                    setBlack(x.parent.right);
                    setRed(x.parent);
                    leftRotate(x.parent);
                }
                if (isBlack(x.parent.right.left) && isBlack(x.parent.right.right)) {
                    setRed(x.parent.right);
                    x = x.parent;
                } else {
                    if (isBlack(x.parent.right.right)) {
                        setBlack(x.parent.right.left);
                        setRed(x.parent.right);
                        rightRotate(x.parent);
                        x = x.parent;
                    }
                    x.parent.right.red = x.parent.red;
                    setBlack(x.parent);
                    setBlack(x.parent.right.right);
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                if (isRed(x.parent.left)) {
                    setBlack(x.parent.left);
                    setRed(x.parent);
                    rightRotate(x.parent);
                }
                if (isBlack(x.parent.left.right) && isBlack(x.parent.left.left)) {
                    setRed(x.parent.left);
                    x = x.parent;
                } else {
                    if (isBlack(x.parent.left.left)) {
                        setBlack(x.parent.left.right);
                        setRed(x.parent.left);
                        leftRotate(x.parent.left);
                    }
                    x.parent.left.red = x.parent.red;
                    setBlack(x.parent);
                    setBlack(x.parent.left.left);
                    rightRotate(x.parent);
                    x = root;
                }
            }
        }
        return x;
    }

    protected TreeNode<V> rightRotate(TreeNode<V> p) {
        TreeNode<V> pLeft = p.left;
        p.left = pLeft.right;
        if (pLeft.right != null) {
            pLeft.right.parent = p;
        }
        pLeft.parent = p.parent;
        if (p.parent == null) {
            root = pLeft;
        } else {
            if (p == p.parent.left) {
                p.parent.left = pLeft;
            } else {
                p.parent.right = pLeft;
            }
        }
        pLeft.right = p;
        p.parent = pLeft;
        return p;
    }

    protected TreeNode<V> leftRotate(TreeNode<V> p) {
        TreeNode<V> pRight = p.right;
        p.right = pRight.left;
        if (pRight.left != null) {
            pRight.left.parent = p;
        }
        pRight.parent = p.parent;
        if (p.parent == null) {
            root = pRight;
        } else {
            if (p == p.parent.left) {
                p.parent.left = pRight;
            } else {
                p.parent.right = pRight;
            }
        }
        pRight.left = p;
        p.parent = pRight;
        return pRight;
    }
}
