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
        root = remove_traverse(root, v);
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

    @Override
    protected TreeNode<V> remove_traverse(TreeNode<V> parent, V v) {
        if (parent == null) {
            return null;
        }
        // 复用 BST
        // 待删除的节点x, x的父节点xp
        TreeNode<V> x = parent, xp = null;
        do {
            if (eq(v, x.val)) break;
            xp = x;
            x = less(v, x.val) ? x.left : x.right;
        } while (x != null);

        if (x == null) return parent; // 无需删除, 原样返回
        
        // 红黑树平衡, 在节点删除之前进行平衡操作
        if (!x.red) balanceDeletion(x);

        // 待删除x是叶子结点
        if (x.right == null && x.left == null) {
            if (xp == null) {
                size--;
                return null;
            }
            if (xp.left == x) {
                xp.left = null;
            } else {
                xp.right = null;
            }
            size--;
            return root;
        }

        // 待删除x是非叶子结点, 要找到其前驱或后继节点代替它
        if (x.right != null) {
            TreeNode<V> minNode = getMinNode(x.right);
            x.val = minNode.val;
            x.right = removeMinReturnNewParent(x.right);
            x.red = minNode.red;
        } else if (x.left != null) {
            TreeNode<V> maxNode = getMaxNode(x.left);
            x.val = maxNode.val;
            x.left = removeMaxReturnNewParent(x.left);
            x.red = maxNode.red;
        }
        size--;
        
//        if (!x.red) balanceDeletion(x);
        
        return root;
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
//        while (true) {
//            if (x == null || x == root) {
//                return root;
//            } else if (parent(x) == null) {
//                setBlack(x);
//                return x;
//            } else if (isRed(x)) {
//                setBlack(x);
//                return root;
//            }
            
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
                    setColor(right(parent(x)), parent(x).red);
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
                    setColor(left(parent(x)), parent(x).red);
                    setBlack(parent(x));
                    setBlack(left(left(parent(x))));
                    rightRotate(parent(x));
                    x = root;
                }
            }
        }
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
