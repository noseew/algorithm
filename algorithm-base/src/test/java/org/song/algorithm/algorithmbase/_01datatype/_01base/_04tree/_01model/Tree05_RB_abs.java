package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

可以通过理解234树来理解红黑树的旋转和变色

 */
public abstract class Tree05_RB_abs<V extends Comparable<V>> extends Tree02_BST_base<V> {

    public static final boolean RED = true;
    public static final boolean BLACK = false;
    
    public Tree05_RB_abs(Comparator<V> comparator) {
        super(comparator);
    }

    /***************************************** 平衡 *****************************************************/

    /**
     * 插入平衡
     *
     * @param x
     * @return
     */
    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        return x;
    }

    /**
     * 删除平衡
     *
     * @param x
     * @return
     */
    protected TreeNode<V> balanceDeletion(TreeNode<V> x) {
        return x;
    }

    /**
     * 处理 / LL
     *
     * @param p 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> rightRotate(TreeNode<V> p) {
        /*
        右旋:
            LL型(左左) /, M在P左, S在M左, 调整方式 P右旋, M成为新的, P父节点 (M为轴P右旋)
        注意:
            1. 需要将 M右子交给P左子
            2. 树节点的变更, 至少要操作2个指针才能确定位置安全和线程安全, 因此无法使用CAS操作来保证线程安全

          LL 型 /:
                    P
                   /
                  M
                 /
                S
            M为轴P右旋转成
               M
              /  \
             S    P
         */
        
        /*
        说明: 
            1. 没有 parent 指针
            2. 返回新的父节点, 由调用方处理和他们的关系重新和他关联
        
        TreeNode<V> pLeft = p.left;
        p.left = pLeft.right;
        pLeft.right = p;
        return pLeft;
        
         */
        
        /*
        说明: 
            1. 有 parent 指针
                至少需要处理3个节点的 parent 指针
                1)p.parent, 2)pLeft.parent, 3)pLeft.right.parent
            2. 返回新的父节点, 由调用方处理和他们的关系重新和他关联
            3. 没有处理 p.parent.left/right, 由调用方处理和他们的关系重新和他关联
            4. 没有处理变色
        
        if (p != null) {
            TreeNode<V> pLeft = p.left;
            p.left = pLeft.right;
            if (pLeft.right != null) {
                // 3. pLeft.right.parent
                pLeft.right.parent = p;
            }
            // 2. pLeft.parent
            pLeft.parent = p.parent;
            pLeft.right = p;
            // 1. p.parent
            p.parent = pLeft;
            return pLeft;
        }
        // 处理 p.parent的子节点指向, 可选操作, 也可以将新的parent节点返回, 由调用方处理
            if (p.parent == null) {
                root = l;
            } else if (p.parent.right == p) {
                p.parent.right = l;
            } else {
                p.parent.left = l;
            }
         */

        return p;
    }

    /**
     * 处理 \ RR
     *
     * @param p 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> leftRotate(TreeNode<V> p) {
        /*
        左旋:
            RR型(右右) \, M在P右, S在M右, 调整方式 P左旋, M成为新的, P父节点 (M为轴P左旋)
        注意:
            1. 需要将 M左子交给P右子
            2. 树节点的变更, 至少要操作2个指针才能确定位置安全和线程安全, 因此无法使用CAS操作来保证线程安全

            RR 型 \:
                P
                 \
                  M
                   \
                    S
            M为轴P左旋转成
                 M
               /   \
              P     S
         */
        
        /*
        说明: 
            1. 没有 parent 指针
            2. 返回新的父节点, 由调用方处理和他们的关系重新和他关联
        
        TreeNode<V> pRight = p.right;
        p.right = pRight.left;
        pRight.left = p;
        return pRight;
        
         */
        /*
        说明: 
            1. 有 parent 指针
                至少需要处理3个节点的 parent 指针
                1)p.parent, 2)pRight.parent, 3)pRight.left.parent
            2. 返回新的父节点, 由调用方处理和他们的关系重新和他关联
            3. 没有处理 p.parent.left/right, 由调用方处理和他们的关系重新和他关联
            4. 没有处理变色
        
        if (p != null) {
            TreeNode<V> pRight = p.right;
            p.right = pRight.left;
            if (pRight.left != null) {
                // 3)pRight.left.parent
                pRight.left.parent = p;
            }
            // 2)pRight.parent
            pRight.parent = p.parent;
            pRight.left = p;
            // 1)p.parent
            p.parent = pRight;
            return pRight;
        }
        // 处理 p.parent的子节点指向, 可选操作, 也可以将新的parent节点返回, 由调用方处理
            if (p.parent == null) {
                root = r;
            } else if (p.parent.left == p) {
                p.parent.left = r;
            } else {
                p.parent.right = r;
            }
         */
        return p;
    }
}
