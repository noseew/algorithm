package org.song.algorithm.algorithmbase._02case.leetcode.simple._03bintree;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 101. 对称二叉树
 * 给定一个二叉树，检查它是否是镜像对称的。
 * <p>
 * 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
 * <p>
 * 但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
 * 进阶：
 * <p>
 * 你可以运用递归和迭代两种方法解决这个问题吗？
 */
public class Leetcode_101_isSymmetric {

    @Test
    public void test() {
        System.out.println(isSymmetric(
                new TreeNode(1,
                        new TreeNode(2, new TreeNode(3), null),
                        new TreeNode(2, null, new TreeNode(3)))));
        System.out.println();
    }

    public boolean isSymmetric(TreeNode root) {

        List<TreeNode> pList = new ArrayList<>();
        preOrderTraveral(root.left, pList);

        List<TreeNode> qList = new ArrayList<>();
        postOrderTraveral(root.right, qList);

        if (pList.size() != qList.size()) {
            return false;
        }
        for (int i = 0; i < pList.size(); i++) {
            if (!isSymmetricUtil(pList.get(i), qList.get(pList.size() - i - 1), true)) {
                return false;
            }
        }
        return true;
    }


    public boolean isSymmetricUtil(TreeNode p, TreeNode q, boolean recursion) {
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
        return isSymmetricUtil(p.left, q.right, false) && isSymmetricUtil(p.right, q.left, false);
    }

    public static void preOrderTraveral(TreeNode node, List<TreeNode> nodes) {
        if (node == null) {
            return;
        }
        nodes.add(node);
        preOrderTraveral(node.left, nodes);
        preOrderTraveral(node.right, nodes);
    }

    public static void postOrderTraveral(TreeNode node, List<TreeNode> nodes) {
        if (node == null) {
            return;
        }
        postOrderTraveral(node.left, nodes);
        postOrderTraveral(node.right, nodes);
        nodes.add(node);
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
