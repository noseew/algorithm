package org.song.algorithm.algorithmbase.datatype.tree.model;

import org.junit.Test;
import org.song.algorithm.algorithmbase.datatype.tree.BTreePrinter;

/**
 * Java 语言: AVL树
 *
 * @author skywang
 * @date 2013/11/07
 */
public class AVLTree_temp<V extends Comparable<V>> {

    @Test
    public void test_start_01() {
        AVLTree_temp<Integer> tree = new AVLTree_temp<>();
        tree.insert(1);
        tree.insert(2);
        tree.insert(3);
        BTreePrinter.printNode(mRoot);

    }

    public TreeNode<V> mRoot;    // 根结点

    private int height(TreeNode<V> tree) {
        // 获取树的高度
        if (tree != null) {
            return tree.height;
        }
        return 0;
    }

    private int max(int a, int b) {
        return Math.max(a, b);
    }

    /*
     * LL：左左对应的情况(左单旋转)。
     *
     * 返回值：旋转后的根节点
     */
    private TreeNode<V> leftLeftRotation(TreeNode<V> k2) {
        TreeNode<V> k1;

        k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;

        k2.height = max(height(k2.left), height(k2.right)) + 1;
        k1.height = max(height(k1.left), k2.height) + 1;

        return k1;
    }

    /*
     * RR：右右对应的情况(右单旋转)。
     *
     * 返回值：旋转后的根节点
     */
    private TreeNode<V> rightRightRotation(TreeNode<V> k1) {
        TreeNode<V> k2;

        k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        k1.height = max(height(k1.left), height(k1.right)) + 1;
        k2.height = max(height(k2.right), k1.height) + 1;

        return k2;
    }

    /*
     * LR：左右对应的情况(左双旋转)。
     *
     * 返回值：旋转后的根节点
     */
    private TreeNode<V> leftRightRotation(TreeNode<V> k3) {
        k3.left = rightRightRotation(k3.left);

        return leftLeftRotation(k3);
    }

    /*
     * RL：右左对应的情况(右双旋转)。
     *
     * 返回值：旋转后的根节点
     */
    private TreeNode<V> rightLeftRotation(TreeNode<V> k1) {
        k1.right = leftLeftRotation(k1.right);

        return rightRightRotation(k1);
    }

    /*
     * 将结点插入到AVL树中，并返回根节点
     *
     * 参数说明：
     *     tree AVL树的根结点
     *     key 插入的结点的键值
     * 返回值：
     *     根节点
     */
    private TreeNode<V> insert(TreeNode<V> tree, V key) {
        if (tree == null) {
            // 新建节点
            tree = new TreeNode<V>(null, null, null, key);
            if (tree == null) {
                System.out.println("ERROR: create avltree node failed!");
                return null;
            }
        } else {
            int cmp = key.compareTo(tree.v);

            if (cmp < 0) {    // 应该将key插入到"tree的左子树"的情况
                tree.left = insert(tree.left, key);
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (height(tree.left) - height(tree.right) == 2) {
                    if (key.compareTo(tree.left.v) < 0)
                        tree = leftLeftRotation(tree);
                    else
                        tree = leftRightRotation(tree);
                }
            } else if (cmp > 0) {    // 应该将key插入到"tree的右子树"的情况
                tree.right = insert(tree.right, key);
                // 插入节点后，若AVL树失去平衡，则进行相应的调节。
                if (height(tree.right) - height(tree.left) == 2) {
                    if (key.compareTo(tree.right.v) > 0)
                        tree = rightRightRotation(tree);
                    else
                        tree = rightLeftRotation(tree);
                }
            } else {    // cmp==0
                System.out.println("添加失败：不允许添加相同的节点！");
            }
        }

        tree.height = max(height(tree.left), height(tree.right)) + 1;

        return tree;
    }

    public void insert(V key) {
        mRoot = insert(mRoot, key);
    }

}
