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

        int com;
        if (comparator != null) {
            com = comparator.compare(v, parent.val);
        } else {
            com = ((Comparable) v).compareTo(parent.val);
        }

        if (com < 0) {
            // 向左插入
            parent.left = insert_recursive(parent.left, v);
        } else if (com > 0) {
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
        int com;
        if (comparator != null) {
            com = comparator.compare(v, parent.val);
        } else {
            com = ((Comparable) v).compareTo(parent.val);
        }
        if (com < 0) {
            return search_recursive(parent.left, v);
        } else if (com > 0) {
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

        int com;
        if (comparator != null) {
            com = comparator.compare(v, parent.val);
        } else {
            com = ((Comparable) v).compareTo(parent.val);
        }

        //小于当前根节点
        if (com < 0) {
            parent.left = remove_recursive(parent.left, v);
        } else if (com > 0) {
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
         RR: 单向右型 \ : rightRightRotation(旋转方向: 左旋转, 右上左下)
         LR: 左右型   < : 先旋转成 RR
         LL: 单向左型 / : leftLeftRotate(旋转方向: 右旋转, 左上右下)
         RL: 右左型   > : 先旋转成 LL
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
                /* LL 或者 LR 旋转
                  判断不平衡类型
                  这里是向左插入节点, 不平衡有两种
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
            if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                node = leftLeftRotate(node);
            } else {
                node = leftRightRotate(node);
            }
        }

        // 新节点如果不平衡 右高左低
        else if (getHeight(node.right) - getHeight(node.left) > 1) {
                /* RR 或者 RL 旋转
                  判断不平衡类型
                  这里是向左插入节点, 不平衡有两种
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

                  2. LR型 <:
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
            if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                node = rightRightRotation(node);
            } else {
                node = rightLeftRotate(node);
            }
        }

        // 更新高度
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        return node;
    }

    /**
     * 处理 <
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
        node.left = rightRightRotation(node);
        return leftLeftRotate(node);
    }

    /**
     * 处理 >
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
        node.left = leftLeftRotate(node);
        return rightRightRotation(node);
    }

    /**
     * 处理 /
     *
     * @param node 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> leftLeftRotate(TreeNode<V> node) {
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
     * 处理 \
     *
     * @param node 不平衡的节点, isBalanced(node) = false
     * @return 新的 parent 节点
     */
    protected TreeNode<V> rightRightRotation(TreeNode<V> node) {
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

    @Override
    public String toString() {
        return "AVLTree_base{" +
                "size=" + size +
                ", rotateTimes=" + rotateTimes +
                '}';
    }
}
