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

    @Override
    public boolean add(V v) {
        TreeNode<V> vTreeNode = this.insert_traverse(root, v);
        if (vTreeNode != null) {
            root = vTreeNode;
            TreeNode<V> node = this.search_traverse(root, v);
            balanceInsertion(node);
            setBlack(root);
            return true;
        }
        setBlack(root);
        return false;
    }

    @Override
    public TreeNode<V> newNode(V v) {
        TreeNode<V> node = new TreeNode<>(v, RED);
        node.height = 1;
        size++;
        return node;
    }

    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {

        while (x != null && x != root && isRed(parent(x))) {
            if (parent(x) == left(parent(parent(x)))) {
                if (isRed(right(parent(parent(x))))) {
                    setBlack(parent(x));
                    setRed(parent(parent(x)));
                    setBlack(right(parent(parent(x))));
                    x = parent(parent(x));
                } else {
                    if (x == right(parent(x))) {
                        x = parent(x);
                        leftRotate(x);
                    }
                    setBlack(parent(x));
                    setRed(parent(parent(x)));
                    rightRotate(parent(parent(x)));
                }
            } else {
                if (isRed(left(parent(parent(x))))) {
                    setBlack(parent(x));
                    setRed(parent(parent(x)));
                    setBlack(left(parent(parent(x))));
                    x = parent(parent(x));
                } else {
                    if (x == left(parent(parent(x)))) {
                        x = parent(x);
                        rightRotate(x);
                    }
                    setBlack(parent(x));
                    setRed(parent(parent(x)));
                    leftRotate(parent(parent(x)));
                }
            }
        }
        return x;
    }

    protected TreeNode<V> balanceDeletion(TreeNode<V> x) {
        while (x != root && isBlack(x)) {
            if (x == left(parent(x))) {
                if (isRed(right(parent(x)))) {
                    setBlack(right(parent(x)));
                    setRed(parent(x));
                    leftRotate(parent(x));
                }
                if (isBlack(left(right(parent(x)))) && isBlack(right(right(parent(x))))) {
                    setRed(right(parent(x)));
                    x = parent(x);
                } else {
                    if (isBlack(right(right(parent(x))))) {
                        setBlack(left(right(parent(x))));
                        setRed(right(parent(x)));
                        rightRotate(parent(x));
                        x = parent(x);
                    }
                    right(parent(x)).red = parent(x).red;
                    setBlack(parent(x));
                    setBlack(right(right(parent(x))));
                    leftRotate(parent(x));
                    x = root;
                }
            } else {
                if (isRed(left(parent(x)))) {
                    setBlack(left(parent(x)));
                    setRed(parent(x));
                    rightRotate(parent(x));
                }
                if (isBlack(right(left(parent(x)))) && isBlack(left(left(parent(x))))) {
                    setRed(left(parent(x)));
                    x = parent(x);
                } else {
                    if (isBlack(left(left(parent(x))))) {
                        setBlack(right(left(parent(x))));
                        setRed(left(parent(x)));
                        leftRotate(left(parent(x)));
                    }
                    left(parent(x)).red = parent(x).red;
                    setBlack(parent(x));
                    setBlack(left(left(parent(x))));
                    rightRotate(parent(x));
                    x = root;
                }
            }
        }
        return x;
    }

    protected TreeNode<V> rightRotate(TreeNode<V> p) {
        if (p == null) {
            return p;
        }
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
        if (p == null) {
            return p;
        }
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
