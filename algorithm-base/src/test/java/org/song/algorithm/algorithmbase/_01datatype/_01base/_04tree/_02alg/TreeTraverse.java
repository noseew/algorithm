package org.song.algorithm.algorithmbase._01datatype._01base._04tree._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03AVL01;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

public class TreeTraverse {

    private TreeNode<Integer> initAVLTreeNode(int count) {
        Tree03AVL01<Integer> tree = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < count; i++) {
            tree.add(i);
        }
        return tree.root;
    }
    
    /*
    二叉树的遍历
    1. 前序遍历: 遍历顺序 根左右
    2. 中序遍历: 遍历顺序 左根右
    3. 后序遍历: 遍历顺序 左右根
    
    
     */

    /**
     * 二叉树的遍历 递归
     */
    @Test
    public void test_01_traverse_01() {
        TreeNode<Integer> root = initAVLTreeNode(7);
        BTreeUtils.print(root, true);

        // 二叉树前序遍历   根-> 左-> 右
        preOrderTraversalRecursive(root);
        System.out.println();
        // 二叉树中序遍历   左-> 根-> 右
        inOrderTraversalRecursive(root);
        System.out.println();
        // 二叉树后序遍历   左-> 右-> 根
        postOrderTraversalRecursive(root);
    }

    /**
     * 二叉树的遍历 循环
     */
    @Test
    public void test_02_traverse_02() {
        TreeNode<Integer> root = initAVLTreeNode(3);
        BTreeUtils.print(root, true);
        // 二叉树前序遍历   根-> 左-> 右
        preOrderTraversalWithStack(root);
        System.out.println();
        // 二叉树中序遍历   左-> 根-> 右
        inOrderTraversalWithStack(root);
        System.out.println();
        // 二叉树后序遍历   左-> 右-> 根
        postOrderTraversalWithStack(root);
        // 层序遍历
        levelOrder(root);
    }

    @Test
    public void test_02_traverse_03() {
        TreeNode<Integer> root = initAVLTreeNode(7);
        BTreeUtils.print(root, true);
        // 二叉树前序遍历   根-> 左-> 右
        preOrderTraversalWithStack2(root);
        System.out.println();
        preOrderTraversalWithStack3(root);
        System.out.println();
        // 二叉树中序遍历   左-> 根-> 右
        inOrderTraversalWithStack2(root);
        System.out.println();
        // 二叉树后序遍历   左-> 右-> 根
        postOrderTraversalWithStack2(root);
        System.out.println();
        // 层序遍历
        levelOrder(root);
    }

    /****************************二叉树的遍历*****************************/

    /**
     * 二叉树前序遍历   根-> 左-> 右
     *
     * @param node 二叉树节点
     */
    private static void preOrderTraversalRecursive(TreeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.val + " ");
        preOrderTraversalRecursive(node.left);
        preOrderTraversalRecursive(node.right);
    }

    /**
     * 二叉树中序遍历   左-> 根-> 右
     *
     * @param node 二叉树节点
     */
    private static void inOrderTraversalRecursive(TreeNode node) {
        if (node == null) {
            return;
        }
        inOrderTraversalRecursive(node.left);
        System.out.print(node.val + " ");
        inOrderTraversalRecursive(node.right);
    }

    /**
     * 二叉树后序遍历   左-> 右-> 根
     *
     * @param node 二叉树节点
     */
    private static void postOrderTraversalRecursive(TreeNode node) {
        if (node == null) {
            return;
        }
        postOrderTraversalRecursive(node.left);
        postOrderTraversalRecursive(node.right);
        System.out.print(node.val + " ");
    }

    /**************************** 二叉树的遍历-循环方式 *****************************/
    /**
     * 前序遍历 根-> 左-> 右
     *
     * @param node
     */
    private static void preOrderTraversalWithStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = node;
        while (treeNode != null || !stack.isEmpty()) {
            // 迭代访问节点的左孩子, 并入栈
            while (treeNode != null) {
                System.out.print(treeNode.val + " ");
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            // 如果节点没有左孩子, 则弹出栈顶节点, 访问节点右孩子
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                treeNode = treeNode.right;
            }
        }
    }

    private static void preOrderTraversalWithStack2(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        while (true) {
            if (node != null) {
                System.out.print(node.val + " ");
                if (node.right != null) {
                    stack.push(node.right); // 遍历的同时, 右子节点入栈
                }
                node = node.left; // 一致遍历到左子节点
            } else if (!stack.isEmpty()) {
                node = stack.pop(); // 左子节点遍历完, 开始反向遍历右子节点
            } else {
                break; // 都遍历完, 结束遍历
            }
        }
    }

    private static void preOrderTraversalWithStack3(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(node);
        while (!stack.isEmpty()) {
            TreeNode pop = stack.pop();
            System.out.print(pop.val + " ");
            if (pop.right != null) {
                stack.push(pop.right);
            }
            if (pop.left != null) {
                stack.push(pop.left);
            }
        }
    }

    /**
     * 中序遍历 左-> 根-> 右
     *
     * @param node
     */
    private static void inOrderTraversalWithStack(TreeNode node) {
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

    private static void inOrderTraversalWithStack2(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        while (true) {
            if (node != null) {
                stack.push(node);
                node = node.left;
            } else if (stack.isEmpty()) {
                break;
            } else {
                node = stack.pop();
                System.out.print(node.val + " ");
                node = node.right;
            }
        }
    }

    /**
     * 后续遍历 左-> 右-> 根
     *
     * @param node
     */
    private static void postOrderTraversalWithStack(TreeNode node) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = node;
        TreeNode lastVisit = null;   //标记每次遍历最后一次访问的节点
        while (treeNode != null || !stack.isEmpty()) {//节点不为空, 结点入栈, 并且指向下一个左孩子
            while (treeNode != null) {
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            //栈不为空
            if (!stack.isEmpty()) {
                //出栈
                treeNode = stack.pop();
                /**
                 * 这块就是判断treeNode是否有右孩子, 
                 * 如果没有输出treeNode.data, 让lastVisit指向treeNode, 并让treeNode为空
                 * 如果有右孩子, 将当前节点继续入栈, treeNode指向它的右孩子,继续重复循环
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

    private static void postOrderTraversalWithStack2(TreeNode node) {
        Stack<TreeNode> stack = new Stack<>();
        stack.push(node);
        TreeNode lastVisit = null;
        while (!stack.isEmpty()) {
            TreeNode top = stack.peek();
            if ((top.left == null && top.right == null)
                    || (Objects.equals(lastVisit, top.left) || Objects.equals(lastVisit, top.right))) {
                lastVisit = stack.pop();
                System.out.print(lastVisit.val + " ");
            } else {
                if (top.right != null) {
                    stack.push(top.right);
                }
                if (top.left != null) {
                    stack.push(top.left);
                }
            }
        }
    }

    /**
     * 层序遍历 逐层遍历
     *
     * @param root
     */
    private static void levelOrder(TreeNode root) {
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
