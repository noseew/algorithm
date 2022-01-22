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
    public void push(V v) {
        root = insert_recursive(root, v);
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
        // 平衡处理, 每个节点都要判断并处理
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

        //小于当前根节点
        if (less(v, parent.val)) {
            parent.left = remove_recursive(parent.left, v);
        } else if (greater(v, parent.val)) {
            //大于当前根节点
            parent.right = remove_recursive(parent.left, v);
        } else if (parent.left != null && parent.right != null) {
            //找到右边最小的节点
            parent.val = min(parent.right).val;
            //当前节点的右边等于原节点右边删除已经被选为的替代节点
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
        LL型(左左) /, M在P左, S在M左, 调整方式 P右旋, M成为新的, P父节点 (P右旋)
        RR型(右右) \, M在P右, S在M右, 调整方式 P左旋, M成为新的, P父节点 (P左旋)
        LR型(左右) <, M在P左, S在M右, 调整方式 M左旋, S成为新的M节点, M成为新的S节点, 此时完全变为LL, 接着旋转P (M左旋, P右旋)
        RL型(右左) >, M在P右, S在M左, 调整方式 M右旋, S成为新的M节点, M成为新的S节点, 此时完全成为RR, 接着旋转P (M右旋, P左旋)
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
         LR型 <: (M左旋, P右旋)
                P
               /
               M
                \
                 S
             先M左旋转成
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
         RL型 >: (M右旋, P左旋)
                P
                 \
                  M
                  /
                 S
             先M右旋转成
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
     * @param node 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> rightRotate4LL(TreeNode<V> node) {
        /*
         右旋: 需要操作两个节点
             node: 不平衡的节点
             newParent: 不平衡节点的左子节点
         右旋过程:
            1. 将 node 设置为 newParent 右子节点
            2. 将 node 的左子节点设置为 newParent 原右子节点
            3. 更新高度
         树节点的变更, 至少要操作2个指针才能确定位置安全和线程安全, 因此无法使用CAS操作来保证线程安全

          1. LL 型 /:
                    P
                   /
                  M
                 /
                S
            P右旋转成
               M
              /  \
             S    P
         */
        TreeNode<V> newParent = node.left;
        node.left = newParent.right;
        newParent.right = node;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
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
         左旋: 需要操作两个节点
             node: 不平衡的节点
             newParent: 不平衡节点的右子节点
         右旋过程:
            1. 将 node 设置为 newParent 左子节点
            2. 将 node 的右子节点设置为 newParent 原左子节点
            3. 更新高度
         树节点的变更, 至少要操作2个指针才能确定位置安全和线程安全, 因此无法使用CAS操作来保证线程安全

            RR 型 \:
                P
                 \
                  M
                   \
                    S
            P左旋转成
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
}
