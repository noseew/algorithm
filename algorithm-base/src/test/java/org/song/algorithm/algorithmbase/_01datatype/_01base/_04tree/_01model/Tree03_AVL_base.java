package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.Comparator;

/**
 * 平衡二叉树
 * 优点: 平衡, 查询效率最差 O(logn)
 * 缺点: 增删会涉及旋转, 效率较低
 *
 * @param <V>
 */
public class Tree03_AVL_base<V extends Comparable<V>> extends Tree02_BST_base<V> {

    /**
     * 旋转次数
     */
    private int rotateTimes;

    public Tree03_AVL_base(Comparator<V> comparator) {
        super(comparator);
    }

    @Override
    public boolean push(V v) {
        int size = this.size;
        root = insert_recursive(root, v);
        return size > this.size;
    }

//    @Override
//    public V get(V v) {
//        TreeNode<V> treeNode = search_recursive(root, v);
//        if (treeNode != null) {
//            return treeNode.val;
//        }
//        return null;
//    }

    public V remove(V v) {
        root = remove_recursive(root, v);
        return null;
    }

    /***************************************** 增删查-递归 *****************************************************/

    /**
     * 采用递归的方式, 插入节点
     *
     * @param parent
     * @param v
     * @return
     */
    private TreeNode<V> insert_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            // 新建节点, 高度默认1
            parent = new TreeNode<>(null, null, v);
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
        parent = balance(parent);
        return parent;
    }

    /**
     * 采用递归方式, 查找节点
     *
     * @param parent
     * @param v
     * @return
     */
    private TreeNode<V> search_recursive(TreeNode<V> parent, V v) {
        if (parent == null) {
            return null;
        }
        if (less(v, parent.val)) {
            return search_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            return search_recursive(parent.right, v);
        } else {
            return parent;
        }
    }

    /**
     * 采用递归方式, 删除节点
     *
     * @param parent
     * @param v
     * @return
     */
    private TreeNode<V> remove_recursive(TreeNode<V> parent, V v) {

        if (null == parent) {
            return parent;
        }
        /*
        1. 递归找到指定的节点
        2.
         */

        if (less(v, parent.val)) {
            // 小于当前根节点
            parent.left = remove_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            // 大于当前根节点
            parent.right = remove_recursive(parent.left, v);
        } else if (parent.left != null && parent.right != null) {
            // 找到右边最小的节点
            parent.val = min(parent.right).val;
            // 当前节点的右边等于原节点右边删除已经被选为的替代节点
            parent.right = remove_recursive(parent.right, parent.val);
        } else {
            parent = (parent.left != null) ? parent.left : parent.right;
        }
        return balance(parent);
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
     */

    /**
     * 平衡判断和处理
     *
     * @param node
     * @return
     */
    protected TreeNode<V> balance(TreeNode<V> node) {
        if (node == null) {
            return node;
        }

        // 左高右低
        if (getHeight(node.left) - getHeight(node.right) > 1) {
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                // LL型 / 右旋转
                node = rightRotate4LL(node);
            } else {
                // LR型 < 先左旋转再右旋转
                node = leftRightRotate4LR(node);
            }
        }
        // 右高左低
        else if (getHeight(node.right) - getHeight(node.left) > 1) {
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                // RR型 \ 左旋转
                node = leftRotation4RR(node);
            } else {
                // RL型 > 先右旋转再左旋转
                node = rightLeftRotate4RL(node);
            }
        }

        // 更新高度
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    /**
     * 处理 < LR
     *
     * @param p
     * @return
     */
    protected TreeNode<V> leftRightRotate4LR(TreeNode<V> p) {
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
        p.left = leftRotation4RR(p.left);
        return rightRotate4LL(p);
    }

    /**
     * 处理 > RL
     *
     * @param p
     * @return
     */
    protected TreeNode<V> rightLeftRotate4RL(TreeNode<V> p) {
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
        p.right = rightRotate4LL(p.right);
        return leftRotation4RR(p);
    }

    /**
     * 处理 / LL
     *
     * @param p 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> rightRotate4LL(TreeNode<V> p) {
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
    protected TreeNode<V> leftRotation4RR(TreeNode<V> p) {
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
