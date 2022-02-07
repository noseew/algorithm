package org.song.algorithm.algorithmbase._02alg._03app.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._03app.leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 94. 二叉树的中序遍历
 */
public class Leetcode_94_inorderTraversal {

    @Test
    public void test() {
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inorderTraversal(root, list);
        return list;
    }

    public void inorderTraversal(TreeNode root, List<Integer> list) {
        if (root == null) {
            return;
        }
        inorderTraversal(root.left, list);
        list.add(root.val);
        inorderTraversal(root.right, list);
    }
}
