package org.song.algorithm.algorithmbase._01datatype._01base._04tree._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.TreeNode;

import java.util.LinkedList;
import java.util.Stack;

public class Tree_alg {

    private TreeNode<Integer> initALVBinaryTree(int count) {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>();
        for (int i = 0; i < count; i++) {
            tree.insert(i);
        }
        return tree.root;
    }

    /**
     * 二叉树的遍历 递归
     */
    @Test
    public void test_01_traverse_01() {
        TreeNode<Integer> root = initALVBinaryTree(3);
        recursive_01(root, 2);
    }

    /**
     * 0: 前序遍历 根-> 左-> 右
     * 1: 中序遍历 左-> 根-> 右
     * 2: 后序遍历 左-> 右-> 根
     *
     * @param node
     * @param order 遍历顺序 0: 前序, 1: 中序, 2: 后序
     */
    private void recursive_01(TreeNode<Integer> node, int order) {
        if (node == null) {
            return;
        }
        if (order == 0) {
            System.out.println(node.val);
        }

        recursive_01(node.left, order);

        if (order == 1) {
            System.out.println(node.val);
        }

        recursive_01(node.right, order);

        if (order == 2) {
            System.out.println(node.val);
        }
    }

    /**
     * 二叉树的遍历 循环
     */
    @Test
    public void test_02_traverse_02() {
        TreeNode<Integer> root = initALVBinaryTree(3);
        recursive_02(root, 0);
    }

    private void recursive_02(TreeNode<Integer> node, int order) {
        if (node == null) {
            return;
        }
        Stack<TreeNode<Integer>> stack = new Stack<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                if (order == 0) {
                    System.out.println(node.val);
                }

                stack.push(node);
                node = node.left;
            }
            if (!stack.isEmpty()) {
                node = stack.pop();

                if (order == 1) {
                    System.out.println(node.val);
                }

                node = node.right;
            }
        }
    }


    /****************************二叉树的遍历*****************************/

    /**
     * 二叉树前序遍历   根-> 左-> 右
     *
     * @param node 二叉树节点
     */
    public static void preOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.val + " ");
        preOrderTraveral(node.left);
        preOrderTraveral(node.right);
    }

    /**
     * 二叉树中序遍历   左-> 根-> 右
     *
     * @param node 二叉树节点
     */
    public static void inOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        inOrderTraveral(node.left);
        System.out.print(node.val + " ");
        inOrderTraveral(node.right);
    }

    /**
     * 二叉树后序遍历   左-> 右-> 根
     *
     * @param node 二叉树节点
     */
    public static void postOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        postOrderTraveral(node.left);
        postOrderTraveral(node.right);
        System.out.print(node.val + " ");
    }

    /**************************** 二叉树的遍历-循环方式 *****************************/
    /**
     * 前序遍历
     *
     * @param node
     */
    public static void preOrderTraveralWithStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = node;
        while (treeNode != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (treeNode != null) {
                System.out.print(treeNode.val + " ");
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            //如果节点没有左孩子，则弹出栈顶节点，访问节点右孩子
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                treeNode = treeNode.right;
            }
        }
    }

    /**
     * 中序遍历
     *
     * @param node
     */
    public static void inOrderTraveralWithStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = node;
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                System.out.print(treeNode.val + " ");
                treeNode = treeNode.right;
            }

        }
    }

    /**
     * 后续遍历
     *
     * @param node
     */
    public static void postOrderTraveralWithStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = node;
        TreeNode lastVisit = null;   //标记每次遍历最后一次访问的节点
        while (treeNode != null || !stack.isEmpty()) {//节点不为空，结点入栈，并且指向下一个左孩子
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            //栈不为空
            if (!stack.isEmpty()) {
                //出栈
                treeNode = stack.pop();
                /**
                 * 这块就是判断treeNode是否有右孩子，
                 * 如果没有输出treeNode.data，让lastVisit指向treeNode，并让treeNode为空
                 * 如果有右孩子，将当前节点继续入栈，treeNode指向它的右孩子,继续重复循环
                 */
                if (treeNode.right == null || treeNode.right == lastVisit) {
                    System.out.print(treeNode.val + " ");
                    lastVisit = treeNode;
                    treeNode = null;
                } else {
                    stack.push(treeNode);
                    treeNode = treeNode.right;
                }

            }

        }
    }

    /**
     * 层序遍历
     *
     * @param root
     */
    public static void levelOrder(TreeNode root) {
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            root = queue.pop();
            System.out.print(root.val + " ");
            if (root.left != null) {
                queue.add(root.left);
            }
            if (root.right != null) {
                queue.add(root.right);
            }
        }
    }

}
