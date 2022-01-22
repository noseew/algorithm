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
            // 重复元素不处理
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
         平衡二叉树的平衡分为
         RR: 单向右型 \ : 处理方式需要 左旋转
         LR: 左右型   < : 先旋转成 RR, 先左旋转再右旋转
         LL: 单向左型 / : 处理方式需要 右旋转
         RL: 右左型   > : 先旋转成 LL, 先右旋转再左旋转
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

        // 新节点如果不平衡 左高右低
        if (getHeight(node.left) - getHeight(node.right) > 1) {
                /*
                  判断不平衡类型
                  这里是向左插入节点, 不平衡有两种
                 */
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                /*
                LL 需要 右旋转
                  1. LL型 /:
                            p
                           /
                        p.left
                         /
                       v
                    旋转成
                      p.left
                      /  \
                     v    p
                 */
                node = rightRotate(node);
            } else {
                /*
                LR 需要 先左旋转再右旋转
                  2. LR型 <:
                            p
                           /
                        p.left
                            \
                             v
                    旋转成
                      p.left
                      /  \
                     v    p
                 */
                node = leftRightRotate(node);
            }
        }
        // 新节点如果不平衡 右高左低
        else if (getHeight(node.right) - getHeight(node.left) > 1) {
                /*
                  判断不平衡类型
                 */
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                /*
                RR 需要 左旋转
                  1. RR型 \:
                        p
                         \
                        p.left
                           \
                            v
                    旋转成
                      p.left
                      /  \
                     v    p
                
                 */
                node = leftRotation(node);
            } else {
                /*
                RL 需要 先右旋转再左旋转
                  2. RL型 >:
                        p
                         \
                        p.left
                          /
                        v
                    旋转成
                      p.left
                      /  \
                     p    v
                 */
                node = rightLeftRotate(node);
            }
        }

        // 更新高度
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    /**
     * 处理 < LR
     *
     * @param node
     * @return
     */
    protected TreeNode<V> leftRightRotate(TreeNode<V> node) {
        /*
         LR型 <:
                p
                 \
                p.left
                  /
                v

             先旋转成
                p
                 \
                p.left
                   \
                    v

            再旋转成
              p.left
              /  \
             p    v
         */
        node.left = leftRotation(node);
        return rightRotate(node);
    }

    /**
     * 处理 > RL
     *
     * @param node
     * @return
     */
    protected TreeNode<V> rightLeftRotate(TreeNode<V> node) {
        /*
         RL型 >:
                p
               /
            p.left
                \
                 v

             先旋转成

                    p
                   /
                p.left
                 /
               v

            再旋转成

              p.left
              /  \
             v    p

         */
        node.left = rightRotate(node);
        return leftRotation(node);
    }

    /**
     * 处理 / LL
     *
     * @param node 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> rightRotate(TreeNode<V> node) {
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
                   node
                   /
               newParent
                 /
               v
            旋转成
             newParent
              /    \
             v     node
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
     * @param node 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> leftRotation(TreeNode<V> node) {
        /*
         左旋: 需要操作两个节点
             node: 不平衡的节点
             newParent: 不平衡节点的右子节点
         右旋过程:
            1. 将 node 设置为 newParent 左子节点
            2. 将 node 的右子节点设置为 newParent 原左子节点
            3. 更新高度
         树节点的变更, 至少要操作2个指针才能确定位置安全和线程安全, 因此无法使用CAS操作来保证线程安全

               node
                 \
                newParent
                   \
                    v
            旋转成
              newParent
               /    \
             node    v
         */
        TreeNode<V> newParent = node.right;
        node.right = newParent.left;
        newParent.left = node;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newParent.height = Math.max(getHeight(newParent.left), getHeight(newParent)) + 1;

        rotateTimes++;

        return newParent;
    }
}
