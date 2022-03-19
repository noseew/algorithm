package org.song.algorithm.algorithmbase._01datatype._01base._04tree._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02BST;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03AVL;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

public class TreeAlg {

    private TreeNode<Integer> initAVLTreeNode(int count) {
        Tree03AVL<Integer> tree = new Tree03AVL<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < count; i++) {
            tree.add(i);
        }
        return tree.root;
    }

    private TreeNode<Integer> initBSTTreeNode(int count) {
        Tree02BST<Integer> tree = new Tree02BST<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < count; i++) {
            tree.add(i);
        }
        return tree.root;
    }

    private Tree03AVL<Integer> initAVLTree(int count) {
        Tree03AVL<Integer> tree = new Tree03AVL<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < count; i++) {
            tree.add(i);
        }
        return tree;
    }

    /**
     * 二叉树的遍历 递归
     */
    @Test
    public void test_01_traverse_01() {
        TreeNode<Integer> root = initAVLTreeNode(3);
        // 二叉树前序遍历   根-> 左-> 右
        preOrderTraversalRecursive(root);
        // 二叉树中序遍历   左-> 根-> 右
        inOrderTraversalRecursive(root);
        // 二叉树后序遍历   左-> 右-> 根
        postOrderTraversalRecursive(root);
    }

    /**
     * 二叉树的遍历 循环
     */
    @Test
    public void test_02_traverse_02() {
        TreeNode<Integer> root = initAVLTreeNode(3);
        // 二叉树前序遍历   根-> 左-> 右
        preOrderTraversalWithStack(root);
        // 二叉树中序遍历   左-> 根-> 右
        inOrderTraversalWithStack(root);
        // 二叉树后序遍历   左-> 右-> 根
        postOrderTraversalWithStack(root);
        // 层序遍历
        levelOrder(root);
    }

    /**
     * 获取二叉树的高度
     */
    @Test
    public void test_03_height() {
        TreeNode<Integer> root = initAVLTreeNode(20);
        int height = getHeightRecursive(root);
        System.out.println(height);

        BTreeUtils.print(root, true);
    }
    @Test
    public void test_03_minHeight() {
//        TreeNode<Integer> root = initAVLTreeNode(20);
        TreeNode<Integer> root = initBSTTreeNode(20);
        int height = minHeightRecursive(root);
        System.out.println(height);

//        BTreePrinter.print(root, true);
    }

    /**
     * 二叉树还原
     */
    @Test
    public void test_04_restore() {
        Tree03AVL<Integer> avlTree = initAVLTree(20);
        BTreeUtils.print(avlTree.root, true);

    }

    /**
     * 判断树是否平衡
     */
    @Test
    public void isBalanced() {
        TreeNode<Integer> root = initAVLTreeNode(20);
        AtomicBoolean balanced = new AtomicBoolean(true);
        getHeight(root, balanced);
        System.out.println("" + balanced.get());
        BTreeUtils.print(root, true);
        
        System.out.println();
        
        TreeNode<Integer> root2 = initBSTTreeNode(5);
        AtomicBoolean balanced2 = new AtomicBoolean(true);
        getHeight(root2, balanced2);
        System.out.println("" + balanced2.get());
        BTreeUtils.print(root2, true);
    }


    protected int getHeight(TreeNode root, AtomicBoolean balanced) {
        if (root == null) {
            return 0;
        }
        int leftHeight = getHeight(root.left, balanced);
        int rightHeight = getHeight(root.right, balanced);
        int diff = Math.abs(leftHeight - rightHeight);
        if (diff > 1) {
            balanced.set(false);
        }
        return Math.max(leftHeight, rightHeight) + 1;
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

    /**
     * 后续遍历 左-> 右-> 根
     *
     * @param node
     */
    private static void postOrderTraversalWithStack(TreeNode node) {
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

    /**
     * 二叉树的高度 以最大的高度为准
     * 
     * @param root
     * @return
     */
    private static int getHeightRecursive(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(getHeightRecursive(root.left), getHeightRecursive(root.right)) + 1;
    }
    
    protected int minHeightRecursive(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.min(minHeightRecursive(root.left), minHeightRecursive(root.right)) + 1;
    }

}
