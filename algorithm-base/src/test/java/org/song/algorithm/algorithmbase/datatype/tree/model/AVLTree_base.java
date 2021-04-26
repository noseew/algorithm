package org.song.algorithm.algorithmbase.datatype.tree.model;

import java.util.Comparator;

/**
 * 平衡二叉树
 * 优点: 平衡, 查询效率最差 O(logn)
 * 缺点: 增删会涉及旋转, 效率较低
 *
 * @param <V>
 */
public class AVLTree_base<V> {

    private int size;
    /**
     * 旋转次数
     */
    private int rotateTimes;

    private Comparator<V> comparator;

    public TreeNode<V> root;

    public void push_recursive(V v) {
        root = insert(root, v);
    }

    public V search_recursive(V v) {
        TreeNode<V> treeNode = search(root, v);
        if (treeNode != null) {
            return treeNode.v;
        }
        return null;
    }

    /**
     * 采用递归的方式, 插入节点
     *
     * @param parent
     * @param v
     * @return
     */
    private TreeNode<V> insert(TreeNode<V> parent, V v) {
        if (parent == null) {
            // 新建节点, 高度默认1
            parent = new TreeNode<>(parent, null, null, v);
            parent.height = 1;
            size++;
            return parent;
        }

        int com;
        if (comparator != null) {
            com = comparator.compare(v, parent.v);
        } else {
            com = ((Comparable) v).compareTo(parent.v);
        }

        if (com < 0) {
            // 向左插入
            parent.left = insert(parent.left, v);
            // 新节点如果不平衡(左右子树高度差 > 1)
            if (getHeight(parent.left) - getHeight(parent.right) > 1) {
                /*
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
                if (((Comparable) v).compareTo(parent.left.v) < 0) {
                    // LL型
                    parent = leftLeftRotate(parent);
                } else {
                    // LR型
                    parent = leftRightRotate(parent);
                }
            }
        } else if (com > 0) {
            // 向右插入
            parent.right = insert(parent.right, v);
            // 新节点如果不平衡(左右子树高度差 > 1)
            if (getHeight(parent.right) - getHeight(parent.left) > 1) {
                /*
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
                if (((Comparable) v).compareTo(parent.right.v) > 0) {
                    // RR型
                    parent = rightRightRotation(parent);
                } else {
                    // RL型
                    parent = rightLeftRotate(parent);
                }
            }
        } else {
            // 重复元素不处理
            return parent;
        }
        parent.height = Math.max(getHeight(parent.left), getHeight(parent.right)) + 1;
        return parent;
    }

    /**
     * 采用递归方式, 查找节点
     *
     * @param parent
     * @param v
     * @return
     */
    private TreeNode<V> search(TreeNode<V> parent, V v) {
        if (parent == null) {
            return null;
        }
        int com;
        if (comparator != null) {
            com = comparator.compare(v, parent.v);
        } else {
            com = ((Comparable) v).compareTo(parent.v);
        }
        if (com < 0) {
            return search(parent.left, v);
        } else if (com > 0) {
            return search(parent.right, v);
        } else {
            return parent;
        }
    }

    /*
         平衡二叉树的平衡分为
         RR: 单向右型 \ : rightRightRotation(旋转方向: 左旋转, 右上左下)
         LR: 左右型   < : 先旋转成 RR
         LL: 单向左型 / : leftLeftRotate(旋转方向: 右旋转, 左上右下)
         RL: 右左型   > : 先旋转成 LL
     */

    /**
     * 处理 <
     *
     * @param node
     * @return
     */
    private TreeNode<V> leftRightRotate(TreeNode<V> node) {
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
    private TreeNode<V> rightLeftRotate(TreeNode<V> node) {
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
    private TreeNode<V> leftLeftRotate(TreeNode<V> node) {
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
    private TreeNode<V> rightRightRotation(TreeNode<V> node) {
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

    private boolean isBalanced() {
        return isBalanced(root);
    }

    private boolean isBalanced(TreeNode<V> node) {
        if (node == null) {
            return true;
        }
        int balanceFactory = Math.abs(getBalanceFactor(node));
        if (balanceFactory > 1) {
            return false;
        }
        return isBalanced(node.left) && isBalanced(node.right);
    }

    private int getBalanceFactor(TreeNode<V> node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    private int getHeight(TreeNode<V> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    @Override
    public String toString() {
        return "AVLTree_base{" +
                "size=" + size +
                ", rotateTimes=" + rotateTimes +
                '}';
    }
}
