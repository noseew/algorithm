package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

可以通过理解234树来理解红黑树的旋转和变色

 */
public class Tree05_RB_base<V extends Comparable<V>> extends Tree05_RB_abs<V> {

    public Tree05_RB_base(Comparator<V> comparator) {
        super(comparator);
    }

    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {

        while (x != null && x != root && x.parent.red == RED) {
            if (x.parent == x.parent.parent.left) {
                if (x.parent.parent.right.red == RED) {
                    x.parent.red = BLACK;
                    x.parent.parent.red = RED;
                    x.parent.parent.right.red = BLACK;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        leftRotate(x);
                    }
                    x.parent.red = BLACK;
                    x.parent.parent.red = RED;
                    rightRotate(x.parent.parent);
                }
            } else {
                if (x.parent.parent.left.red == RED) {
                    x.parent.red = BLACK;
                    x.parent.parent.red = RED;
                    x.parent.parent.left.red = BLACK;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.parent.left) {
                        x = x.parent;
                        rightRotate(x);
                    }
                    x.parent.red = BLACK;
                    x.parent.parent.red = RED;
                    leftRotate(x.parent.parent);
                }
            }
        }
        return x;
    }

    protected TreeNode<V> balanceDeletion(TreeNode<V> x) {
        while (x != root && x.red == BLACK) {
            if (x == x.parent.left) {
                if (x.parent.right.red == RED) {
                    x.parent.right.red = BLACK;
                    x.parent.red = RED;
                    leftRotate(x.parent);
                }
                if (x.parent.right.left.red == BLACK && x.parent.right.right.red == BLACK) {
                    x.parent.right.red = RED;
                    x = x.parent;
                } else {
                    if (x.parent.right.right.red == BLACK) {
                        x.parent.right.left.red = BLACK;
                        x.parent.right.red = RED;
                        rightRotate(x.parent);
                        x = x.parent;
                    }
                    x.parent.right.red = x.parent.red;
                    x.parent.red = BLACK;
                    x.parent.right.right.red = BLACK;
                    leftRotate(x.parent);
                    x = root;
                }
            } else {
                if (x.parent.left.red == RED) {
                    x.parent.left.red = BLACK;
                    x.parent.red = RED;
                    rightRotate(x.parent);
                }
                if (x.parent.left.right.red == BLACK && x.parent.left.left.red == BLACK) {
                    x.parent.left.red = RED;
                    x = x.parent;
                } else {
                    if (x.parent.left.left.red == BLACK) {
                        x.parent.left.right.red = BLACK;
                        x.parent.left.red = RED;
                        leftRotate(x.parent.left);
                    }
                    x.parent.left.red = x.parent.red;
                    x.parent.red = BLACK;
                    x.parent.left.left.red = BLACK;
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
