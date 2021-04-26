package org.song.algorithm.algorithmbase.datatype.tree.model;

import java.util.Comparator;

/**
 * 平衡二叉树
 *
 * @param <V>
 */
public class AVLTree_base<V> {

    private int size;

    private Comparator<V> comparator;

    public TreeNode<V> root;

    public void push(V v) {
        root = insert(root, v);
    }

    private TreeNode<V> insert(TreeNode<V> parent, V v) {
        if (parent == null) {
            parent = new TreeNode<>(parent, null, null, v);
            // 叶子节点的高度都是0
            parent.height = 0;
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
            parent.left = insert(parent.left, v);
            if (getHeight(parent.left) - getHeight(parent.right) > 1) {
                if (((Comparable) v).compareTo(parent.left.v) < 0) {
                    parent = leftLeftRotate(parent);
                } else {
                    parent = leftRightRotate(parent);
                }
            }
        } else if (com > 0) {
            parent.right = insert(parent.right, v);
            if (getHeight(parent.right) - getHeight(parent.right) > 1) {
                if (((Comparable) v).compareTo(parent.right.v) > 0) {
                    parent = rightRightRotation(parent);
                } else {
                    parent = rightLeftRotate(parent);
                }
            }

        }
        parent.height = Math.max(getHeight(parent.left), getHeight(parent.right)) + 1;
        return parent;
    }

    private int compare(TreeNode<V> node1, TreeNode<V> node2) {
        if (comparator != null) {
            return comparator.compare(node1.v, node2.v);
        } else {
            return ((Comparable) node1.v).compareTo(node2.v);
        }
    }

    private void put(TreeNode<V> parent, V v) {
        TreeNode<V> newNode = new TreeNode<>(parent, null, null, v);
        if (comparator != null) {
            if (comparator.compare(v, parent.v) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        } else {
            if (((Comparable) v).compareTo(((Comparable) parent.v)) < 0) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }
        }
    }

    private void balance(TreeNode<V> node) {
        /*
         平衡二叉树的平衡分为
         RR: 单向右型 \ : rightRightRotation(旋转方向: 左旋转, 右上左下)
         LR: 左右型   < : 先旋转成 RR
         LL: 单向左型 / : leftLeftRotate(旋转方向: 右旋转, 左上右下)
         RL: 右左型   > : 先旋转成 LL
         */
    }

    /**
     * 处理 <
     *
     * @param node
     * @return
     */
    private TreeNode<V> leftRightRotate(TreeNode<V> node) {
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
         */
        TreeNode<V> newParent = node.left;
        node.left = newParent.right;
        newParent.right = node;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newParent.height = Math.max(getHeight(newParent.left), getHeight(newParent)) + 1;

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
         */
        TreeNode<V> newParent = node.right;
        node.right = newParent.left;
        newParent.left = node;

        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        newParent.height = Math.max(getHeight(newParent.left), getHeight(newParent)) + 1;

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

}
