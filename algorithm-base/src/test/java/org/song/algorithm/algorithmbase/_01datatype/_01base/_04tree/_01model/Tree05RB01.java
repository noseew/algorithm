package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

模板实现, 尤其是旋转和平衡

主要参考了, TreeMap中的红黑树实现

 */
public class Tree05RB01<V extends Comparable<V>> extends Tree05RBAbs<V> {

    public Tree05RB01(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public boolean add(V v) {
        int oldSize = this.size;
        TreeNode<V> node = this.insert_traverse(root, v);
        if (root == null) {
            root = node;
        }
        balanceInsertion(node);
        setBlack(root); // 根总为黑
        return oldSize < size;
    }

    @Override
    public V remove(V v) {
        TreeNode<V> node = search_traverse(root, v);
        remove(node);
        setBlack(root); // 根总为黑
        return null;
    }

    @Override
    public TreeNode<V> newNode(V v) {
        TreeNode<V> node = new TreeNode<>(v, RED);
        node.height = 1;
        size++;
        return node;
    }

    /**
     * 删除提供的节点
     * 节点删除逻辑的演化参见 => Tree02BST02 => Tree02BST03
     *
     * @param x
     */
    protected void remove(TreeNode<V> x) {
        // 待删除节点
        if (x == null) return;

        // 删除 度为2的节点
        if (x.right != null && x.left != null) {
            TreeNode<V> successor = successor(x);
            x.val = successor.val;
            x = successor;
        }
        TreeNode<V> replacement = x.right != null ? x.right : x.left;

        if (replacement != null) {
            // 度为1
            if (x.parent == null) {
                root = replacement;
            }else if (isLeft(x.parent, x)) {
                x.parent.left = replacement;
            } else {
                x.parent.right = replacement;
            }
            replacement.parent = x.parent;

            if (!x.red) {
                balanceDeletion(replacement);
            }
            
        } else if (x.parent == null) {
            root = null; // 删除根节点, 则替换root
        } else {
            
            if (!x.red) {
                balanceDeletion(x);
            }
            
            // 度为0
            if (isLeft(x.parent, x)) {
                x.parent.left = null;
            } else {
                x.parent.right = null;
            }
        }
        size--;
    }
    
    @Override
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
                    if (x == left(parent(x))) {
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

    @Override
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
                    setColor(right(parent(x)), color(parent(x)));
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
                    setColor(left(parent(x)), color(parent(x)));
                    setBlack(parent(x));
                    setBlack(left(left(parent(x))));
                    rightRotate(parent(x));
                    x = root;
                }
            }
        }
        x.red = BLACK;
        return x;
    }

    @Override
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

    @Override
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
