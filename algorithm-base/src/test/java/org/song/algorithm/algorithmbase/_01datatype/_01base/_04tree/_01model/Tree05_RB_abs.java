package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

可以通过理解234树来理解红黑树的旋转和变色

 */
public abstract class Tree05_RB_abs<V extends Comparable<V>> extends Tree02_BST_base<V> {
    
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
        没有 parent 指针
        
        TreeNode<V> newParent = p.left;
        p.left = newParent.right;
        newParent.right = p;
        return newParent;
        
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
        没有 parent 指针
        
        TreeNode<V> newParent = p.right;
        p.right = newParent.left;
        newParent.left = p;
        return newParent;
        
         */
        return p;
    }
}
