package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/**
 * 平衡二叉树
 * 
 * 采用循环的方式实现 新增/删除 调整
 * node 节点有parent指针, 
 * 旋转的时候不需要将新的父节点返回
 */
public class Tree03AVL02<V extends Comparable<V>> extends Tree03AVL01<V> {


    public Tree03AVL02(Comparator<V> comparator) {
        super(comparator);
    }

    /**
     * AVL 树新增最多只需要调整1次
     * 
     * @param v
     * @return
     */
    @Override
    public boolean add(V v) {
        int size = this.size;
        insert_traverse(root, v);
        return size > this.size;
    }

    @Override
    public V remove(V v) {
        TreeNode<V> node = search_traverse(root, v);
        if (node != null) {
            remove(node);
        }
        return null;
    }

    /**
     * 采用循环的方式, 插入节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return 返回该节点
     */
    protected TreeNode<V> insert_traverse(TreeNode<V> parent, V v) {
        if (parent == null) {
            TreeNode<V> node = newNode(v);
            root = node;
            return node;
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
        x.parent = xp;
        if (less(v, xp.val)) {
            xp.left = x;
        } else if (greater(v, xp.val)) {
            xp.right = x;
        }

        return balanceInsertion(xp);
    }

    /**
     * 采用循环遍历方式, 查找节点
     *
     * @param parent 以 parent 为root
     * @param v
     * @return
     */
    protected TreeNode<V> search_traverse(TreeNode<V> parent, V v) {
        while (parent != null) {
            if (eq(v, parent.val)) return parent;
            parent = less(v, parent.val) ? parent.left : parent.right;
        }
        return parent;
    }

    protected void remove(TreeNode<V> x) {
        // 待删除节点
        if (x == null) return;

        // 删除 度为2的节点
        if (x.right != null && x.left != null) {
            TreeNode<V> successor = getMinNode(x.right);
            x.val = successor.val; // 采用值替换的方式删除
            x = successor;
        }
        TreeNode<V> replacement = x.right != null ? x.right : x.left;

        // 删除 度为1和0的节点
        if (replacement != null) { // 度为1
            replacement.parent = x.parent;
        }

        // 度为1和0 共用
        if (x.parent == null) {
            root = replacement; // 删除根节点, 则替换root
        } else if (isLeft(x.parent, x)) {
            x.parent.left = replacement; // 删除的是左子节点
        } else {
            x.parent.right = replacement; // 删除的是右子节点
        }
        size--;
        balanceInsertion(x);
    }

    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        if (x == null) {
            return x;
        }
        while (x != null) {
            balance(x);
            x = x.parent;
        }
        return x;
    }

    protected TreeNode<V> balance(TreeNode<V> x) {
        if (x == null) {
            return x;
        }

        // 左高右低
        if (getHeight(x.left) - getHeight(x.right) > 1) {
            if (getHeight(x.left.left) >= getHeight(x.left.right)) {
                // LL型 / 右旋转
                rightRotate(x);
            } else {
                // LR型 < 先左旋转再右旋转
                leftRotate(x.left);
                rightRotate(x);
            }
        }
        // 右高左低
        else if (getHeight(x.right) - getHeight(x.left) > 1) {
            if (getHeight(x.right.right) >= getHeight(x.right.left)) {
                // RR型 \ 左旋转
                leftRotate(x);
            } else {
                // RL型 > 先右旋转再左旋转
                rightRotate(x.right);
                leftRotate(x);
            }
        }

        // 更新高度
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
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


        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        pLeft.height = Math.max(getHeight(pLeft.left), getHeight(pLeft)) + 1;

        rotateTimes++;
        
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

        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        pRight.height = Math.max(getHeight(pRight.left), getHeight(pRight)) + 1;
        rotateTimes++;
        
        return pRight;
    }
}
