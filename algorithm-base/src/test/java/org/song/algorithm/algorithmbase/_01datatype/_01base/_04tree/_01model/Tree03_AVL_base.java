package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;

/**
 * 平衡二叉树
 * 优点: 平衡, 查询效率最差 O(logn)
 * 缺点: 增删会涉及旋转, 效率较低
 * 
 * 待验证
 * AVL 树新增最多只需要调整O(n)次最多2, 删除最多调整(logn)次
 * 新增的时候, 高度最多增加1, 在调整一次之后, 新增节点所在的树高度-1并且平衡, 从而达到平衡, 而不影响祖先或者其他子树的高度
 * 删除的时候, 高度可能降低, 在调整的过程中, 高度可能再次降低, 所以删除可能会有多次调整
 * 
 *
 * @param <V>
 */
public class Tree03_AVL_base<V extends Comparable<V>> extends Tree02_BST_base<V> {

    /**
     * 旋转次数
     */
    public int rotateTimes;

    public Tree03_AVL_base(Comparator<V> comparator) {
        super(comparator);
    }

    /**
     * AVL 树新增最多只需要调整1次
     * 
     * @param v
     * @return
     */
    @Override
    public boolean push(V v) {
        int size = this.size;
        root = insert_recursive(root, v);
        return size > this.size;
    }

    @Override
    public V remove(V v) {
        root = remove_recursive(root, v);
        return null;
    }

    @Override
    protected TreeNode<V> insert_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            // 新建节点, 高度默认1
            parent = new TreeNode<>(v);
            parent.height = 1;
            size++;
            return parent;
        }

        if (less(v, parent.val)) {
            // 向左插入
            parent.left = insert_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            // 向右插入
            parent.right = insert_recursive(parent.right, v);
        } else {
            parent.val = v; // 重复元素不处理 直接替换值
            return parent;
        }
        /*
        平衡处理, 每个节点都要判断并处理
        由于插入是递归操作, 所以每插入一个元素, 都会进行一次平衡调整
        平衡调整由插入的叶子结点的父节点开始, 递归向上逐个判断, 一直判断到根节点
         */
        parent = balanceInsertion(parent);
        return parent;
    }

    @Override
    protected TreeNode<V> remove_recursive(TreeNode<V> parent, V v) {

        if (null == parent) {
            return parent;
        }
        /*
        1. 递归找到指定的节点s
        2. 找到s的直接前驱结点或者直接后继节点, 替代s即可
            1. 直接前驱结点: 就是s的左子树的右右..右子节点
            2. 直接后继节点: 就是s的右子树的左左..右子节点
         */

        if (less(v, parent.val)) {
            // 小于当前根节点
            parent.left = remove_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            // 大于当前根节点
            parent.right = remove_recursive(parent.left, v);
        } else if (parent.left != null && parent.right != null) {
            // 找到右边最小的节点
            parent.val = getMinNode(parent.right).val;
            // 当前节点的右边等于原节点右边删除已经被选为的替代节点
            parent.right = remove_recursive(parent.right, parent.val);
        } else {
            parent = (parent.left != null) ? parent.left : parent.right;
        }
        if (parent != null) {
            size--;
        }
        parent = balanceInsertion(parent);
        return parent;
    }

    /***************************************** 平衡处理-旋转 *****************************************************/

    /*
    节点的关系
    有3个节点, 分别为: 
        P 父节点
        M 子节点
        S 孙节点
    3个节点形成的关系
        LL型(左左) /, M在P左, S在M左, 调整方式 P右旋, M成为新的, P父节点 (M为轴P右旋)
        RR型(右右) \, M在P右, S在M右, 调整方式 P左旋, M成为新的, P父节点 (M为轴P左旋)
        LR型(左右) <, M在P左, S在M右, 调整方式 M左旋, S成为新的M节点, M成为新的S节点, 此时完全变为LL, 接着旋转P (S为轴M左旋, M(新)为轴P右旋)
        RL型(右左) >, M在P右, S在M左, 调整方式 M右旋, S成为新的M节点, M成为新的S节点, 此时完全成为RR, 接着旋转P (S为轴M右旋, M(新)为轴P左旋)
        
    新增节点和删除节点时才需要修正平衡
        AVL树中, 新增和删除的修正逻辑类似, 无特殊处理
     */

    /**
     * 平衡判断和处理
     *
     * @param x
     * @return
     */
    protected TreeNode<V> balanceInsertion(TreeNode<V> x) {
        if (x == null) {
            return x;
        }

        // 左高右低
        if (getHeight(x.left) - getHeight(x.right) > 1) {
            if (getHeight(x.left.left) >= getHeight(x.left.right)) {
                // LL型 / 右旋转
                x = rightRotate(x);
            } else {
                // LR型 < 先左旋转再右旋转
                /*
        左旋+右旋
            LR型(左右) <, M在P左, S在M右, 调整方式 M左旋, S成为新的M节点, M成为新的S节点, 此时完全变为LL, 接着旋转P (S为轴M左旋, M(新)为轴P右旋)
        LR型 <: (M左旋, P右旋)
                P
               /
               M
                \
                 S
             S为轴M左旋成
                    P
                   /
                  S
                 /
                M
            再P右旋转成
                S
              /  \
             M    P
         */
                x.left = leftRotate(x.left);
                x = rightRotate(x);
            }
        }
        // 右高左低
        else if (getHeight(x.right) - getHeight(x.left) > 1) {
            if (getHeight(x.right.right) >= getHeight(x.right.left)) {
                // RR型 \ 左旋转
                x = leftRotate(x);
            } else {
                // RL型 > 先右旋转再左旋转
                /*
        右旋+左旋
            RL型(右左) >, M在P右, S在M左, 调整方式 M右旋, S成为新的M节点, M成为新的S节点, 此时完全成为RR, 接着旋转P (S为轴M右旋, M(新)为轴P左旋)
        RL型 >: (M右旋, P左旋)
                P
                 \
                  M
                  /
                 S
             S为轴M右旋成
                P
                 \
                  S
                   \
                    M
            再P左旋转成
                S
              /  \
             P    M
         */
                x.right = rightRotate(x.right);
                x = leftRotate(x);
            }
        }

        // 更新高度
        x.height = Math.max(getHeight(x.left), getHeight(x.right)) + 1;
        return x;
    }

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
        TreeNode<V> newParent = p.left;
        p.left = newParent.right;
        newParent.right = p;

        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        newParent.height = Math.max(getHeight(newParent.left), getHeight(newParent)) + 1;

        rotateTimes++;

        return newParent;
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
        TreeNode<V> newParent = p.right;
        p.right = newParent.left;
        newParent.left = p;

        p.height = Math.max(getHeight(p.left), getHeight(p.right)) + 1;
        newParent.height = Math.max(getHeight(newParent.left), getHeight(newParent)) + 1;

        rotateTimes++;

        return newParent;
    }

    @Override
    public String toString() {
        return super.toString() + "\r\n" + "调整次数: " + rotateTimes;
    }
}
