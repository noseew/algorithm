package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
一棵二叉查找树(BST)是一棵二叉树, 

node 节点使用parent指针
 */
public class Tree02BST02<V extends Comparable<V>> extends Tree02BST01<V> {

    public Tree02BST02(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public V remove(V v) {
        TreeNode<V> node = search_traverse(root, v);
        remove(node);
        return null;
    }

    /***************************************** 通用方法 可重写 *****************************************************/

    /**
     * 采用循环的方式, 插入节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return 返回该节点
     */
    protected TreeNode<V> insert_traverse(TreeNode<V> parent, V v) {
        if (parent == null) {
            return newNode(v);
        }

        // 获取v的父节点, 如果v存在就是v的父节点, 如果v不存在就是v应该插入的父节点
        TreeNode<V> xp = getParentNode(parent, v);
        if (xp == null) {
            // v == parent
            return parent;
        }

        if ((xp.left != null && eq(xp.left.val, v))
                || (xp.right != null && eq(xp.right.val, v))) {
            // 等值不处理
            return parent;
        }

        TreeNode<V> x = newNode(v);
        x.parent = xp; // 记录parent指针
        if (less(v, xp.val)) {
            xp.left = x;
        } else if (greater(v, xp.val)) {
            xp.right = x;
        }
        return x;
    }

    /**
     * 删除提供的节点
     * 
     * @param x
     */
    protected void remove(TreeNode<V> x) {
        // 待删除节点
        if (x == null) {
            return;
        }

        // 度为0的节点, 待删除x是叶子结点, 直接将叶子节点删除即可
        if (x.right == null && x.left == null) {
            if (x.parent == null) {
                // 待删除节点是根节点, 直接返回新的根节点
                size--;
                root = null;
                return;
            }
            // 但删除节点不是根节点, 返回原来根节点
            if (x.parent.left == x) {
                x.parent.left = null;
            } else {
                x.parent.right = null;
            }
            size--;
            return;
        }

        if (x.right == null || x.left == null) {
            // 度为1的节点
            TreeNode<V> replacement = x.right != null ? x.right : x.left;
            if (x.parent == null) {
                size--;
                root = replacement;
                return;
            }
            if (x.parent.left == x) {
                x.parent.left = replacement; // 删除的是左子节点
            } else {
                x.parent.right = replacement; // 删除的是右子节点
            }
            replacement.parent = x.parent;
        } else {
            // 度为2的节点, 要找到其后继节点代替它
            TreeNode<V> successor = getMinNode(x.right);
            x.val = successor.val; // 采用值替换的方式删除
            // 删除 successor
            if (successor.right != null) {
                successor.parent.left = successor.right;
                successor.right.parent = successor.parent;
            }
        }
        size--;
    }

    protected TreeNode<V> removeAndReturnMax(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.right != null) {
            parent = parent.right;
        }
        // max = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.right = parent.left;
        }
        if (parent.left != null) {
            parent.left.parent = parent.parent;
        }
        return parent;
    }
    
    protected TreeNode<V> removeAndReturnMin(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.left != null) {
            parent = parent.left;
        }
        // min = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.left = parent.right;
        }
        if (parent.right != null) {
            parent.right.parent = parent.parent;
        }
        return parent;
    }
    
    protected TreeNode<V> removeMaxReturnNewParent(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.right != null) {
            parent = parent.right;
        }
        TreeNode<V> newParent = parent.left;
        // max = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.right = parent.left;
        }
        if (parent.left != null) {
            parent.left.parent = parent.parent;
        }
        return newParent;
    }
    
    protected TreeNode<V> removeMinReturnNewParent(TreeNode<V> parent) {
        if (parent == null) {
            return null;
        }
        while (parent.left != null) {
            parent = parent.left;
        }
        TreeNode<V> newParent = parent.right;;
        // min = parent 待删除的节点
        if (parent.parent != null) {
            parent.parent.left = parent.right;
        }
        if (parent.right != null) {
            parent.right.parent = parent.parent;
        }
        return newParent;
    }
}
