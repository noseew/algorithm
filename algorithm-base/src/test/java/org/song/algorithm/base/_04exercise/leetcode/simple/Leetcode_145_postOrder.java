package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 145. 二叉树的后序遍历
 */
/*
 二叉树的遍历
 四种遍历方式分别为：先序遍历、中序遍历、后序遍历、层序遍历。

 */
public class Leetcode_145_postOrder {

    @Test
    public void test() {
        
    }

    public List<Integer> postorderTraversal(TreeNode node) {
        List<Integer> list = new ArrayList<>();
        if (node == null) {
            return list;
        }
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
                    list.add(treeNode.val);
                    lastVisit = treeNode;
                    treeNode = null;
                } else {
                    stack.push(treeNode);
                    treeNode = treeNode.right;
                }
            }
        }
        return list;
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
