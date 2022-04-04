package org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

可以通过理解234树来理解红黑树的旋转和变色

 */
public abstract class Tree05RBAbs<V extends Comparable<V>> extends Tree02BST03<V> {

    public static final boolean RED = true;
    public static final boolean BLACK = false;
    
    public Tree05RBAbs(Comparator<V> comparator) {
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
        /*
        条件: 新 != 根 && 新.父 == 红 (双红)
    
        伪代码
        新.父 == 新.父.父.左      (父节点是左子节点)
            新.父.父.右 == 红     (右叔为红) 仅变色
                新.父 = 黑
                新.父.父 = 红
                新.父.父.右 = 黑
                新 = 新.父.父    (为下一次循环判断跳转)
            否则                (右叔为黑)
                新.父.右 == 新   (当前新是右子节点) LR
                    新 = 新.父   (为下一次循环判断跳转)
                    左旋 新      () 这里没有处理 新.子节点指针, 可以交给旋转里处理
                新.父 = 黑       LL
                新.父.父 = 红
                右旋 新.父.父    () 这里没有处理 新.父.父.子节点指针, 可以交给旋转里处理
        否则                   (父节点是右子节点)
            新.父.父.左 == 红    (左叔为红) 仅变色
                新.父 = 黑
                新.父.父 = 红
                新.父.父.左 = 黑
                新 = 新.父.父   (为下一次循环判断跳转)
            否则               (左叔为黑)
                新.父.左 == 新   (当前新是左子节点) RL
                    新 = 新.父  (为下一次循环判断跳转)
                    右旋 新     () 这里没有处理 新.子节点指针, 可以交给旋转里处理
                新.父 = 黑      RR
                新.父.父 = 红
                左旋 新.父.父   () 这里没有处理 新.父.父.子节点指针, 可以交给旋转里处理
         */
        return x;
    }

    /**
     * 删除平衡
     *
     * @param x
     * @return
     */
    protected TreeNode<V> balanceDeletion(TreeNode<V> x) {
        
        /*
        二叉树中删除节点最终都是导致删除叶子结点, 因为会使用叶子结点补充被删除的节点
        
        不需要旋转处理
            1. 删除红色子节点: 不需要任何处理
            2. 删除拥有红色子节点的黑色节点: 红色前驱/后继节点直接替代并变色
                如何判断是这种情况?
                    用以取代的子节点是红色节点(因为删除黑色节点后需要用子节点(红)来补充其位置)
        需要旋转处理的情况
            1. 删除没有红色子节点的黑色节点
                如何判断是这种情况?
                    用以取代的子节点是null/黑色(红黑树中, 建议一切处理都是用红黑来处理, 而不是二叉树思路来处理)
         */
        /*
        条件: 新 != 根 && 新 == 黑
        
        伪代码
        新 == 新.父.左
            新.父.右 == 红
                新.父.右 = 黑
                新.父 = 红
                左旋 新.父
            新.父.右.左 == 黑 && 新.父.右.右 == 黑
                新.父.右 = 红
                新 = 新.父
            否则
                新.父.右.右 == 黑
                    新.父.右.左 = 黑
                    新.父.右 = 红
                    右旋 新.父.右
                新.父.右.color = 新.父
                新.父 = 黑
                新.父.右.右 = 黑
                左旋 新.父
                新 = root
        否则
            新.父.左 == 红
                新.父.左 = 黑
                新.父 = 红
                右旋 新.父
            新.父.左.右 == 黑 && 新.父.左.左 == 黑
                新.父.左 = 红
                新 = 新.父
            否则
                新.父.左.左 == 黑
                    新.父.左.右 = 黑
                    新.父.左 = 红
                    左旋 新.父.左
                新.父.左.color = 新.父
                新.父 = 黑
                新.父.左.左 = 黑
                右旋 新.父
                新 = root
         */
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
        // 处理 p.parent.left/right 节点指向, 可选操作, 也可以将新的parent节点返回, 由调用方处理
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
        // 处理 p.parent.left/right 子节点指向, 可选操作, 也可以将新的parent节点返回, 由调用方处理
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

    /***************************************** 工具 *****************************************************/

    final protected boolean isRed(TreeNode<V> p) {
        return p != null && p.red;
    }

    final protected void setRed(TreeNode<V> p) {
        setColor(p, RED);
    }

    final protected boolean isBlack(TreeNode<V> p) {
        return p == null || !p.red;
    }

    final protected boolean color(TreeNode<V> p) {
        return p != null ? p.red : BLACK;
    }

    final protected void setBlack(TreeNode<V> p) {
        setColor(p, BLACK);
    }
    
    final protected void setColor(TreeNode<V> p, boolean color) {
        if (p != null) p.red = color;
    }

    final protected TreeNode<V> parent(TreeNode<V> p) {
        return (p == null ? null : p.parent);
    }

    final protected TreeNode<V> left(TreeNode<V> p) {
        return (p == null) ? null : p.left;
    }

    final protected TreeNode<V> right(TreeNode<V> p) {
        return (p == null) ? null : p.right;
    }
}
