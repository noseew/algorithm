package org.song.algorithm.algorithmbase._02alg._02case.leetcode.simple;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 100. 相同的树
 *
 * 给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。
 *
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 * 输入：p = [1,2,3], q = [1,2,3]
 * 输出：true
 *
 * 输入：p = [1,2], q = [1,null,2]
 * 输出：false
 *
 * 输入：p = [1,2,1], q = [1,1,2]
 * 输出：false
 *
 * 提示：
 *
 * 两棵树上的节点数目都在范围 [0, 100] 内
 * -104 <= Node.val <= 104
 */
/*
 二叉树的遍历
 四种遍历方式分别为：先序遍历、中序遍历、后序遍历、层序遍历。

 */
public class Leetcode_100_SameTree {

    @Test
    public void test() {
        System.out.println(isSameTree3(new TreeNode(1, new TreeNode(1), null), new TreeNode(1, null, new TreeNode(1))));
        System.out.println();
    }

    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null && q != null) {
            return false;
        }
        if (q == null && p != null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    public boolean isSameTree2(TreeNode p, TreeNode q) {

        List<TreeNode> pList = new ArrayList<>();
        preOrderTraveral(p, pList);

        List<TreeNode> qList = new ArrayList<>();
        preOrderTraveral(q, qList);

        if (pList.size() != qList.size()) {
            return false;
        }
        for (int i = 0; i < pList.size(); i++) {
            if (!isSameTreeUtil(pList.get(i), qList.get(i), true)) {
                return false;
            }
        }
        return true;
    }

    public boolean isSameTree3(TreeNode p, TreeNode q) {

        List<TreeNode> pList = new ArrayList<>();
        preOrderTraveralWithStack(p, pList);

        List<TreeNode> qList = new ArrayList<>();
        preOrderTraveralWithStack(q, qList);

        if (pList.size() != qList.size()) {
            return false;
        }
        for (int i = 0; i < pList.size(); i++) {
            if (!isSameTreeUtil(pList.get(i), qList.get(i), true)) {
                return false;
            }
        }
        return true;
    }

    public boolean isSameTreeUtil(TreeNode p, TreeNode q, boolean recursion) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null && q != null) {
            return false;
        }
        if (q == null && p != null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        if (!recursion) {
            return true;
        }
        return isSameTreeUtil(p.left, q.left, false) && isSameTreeUtil(p.right, q.right, false);
    }


    public static void preOrderTraveral(TreeNode node, List<TreeNode> treeNodes) {
        if (node == null) {
            return;
        }
        treeNodes.add(node);
        preOrderTraveral(node.left, treeNodes);
        preOrderTraveral(node.right, treeNodes);
    }

    public static void preOrderTraveralWithStack(TreeNode node, List<TreeNode> treeNodes) {
        Stack<TreeNode> stack = new Stack<TreeNode>();
        TreeNode treeNode = node;
        while (treeNode != null || !stack.isEmpty()) {
            while (treeNode != null) {
                treeNodes.add(treeNode);
                stack.push(treeNode);
                treeNode = treeNode.left;
            }
            if (!stack.isEmpty()) {
                treeNode = stack.pop();
                treeNode = treeNode.right;
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

    /**
     * Definition for a binary tree node.
     */
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

}
