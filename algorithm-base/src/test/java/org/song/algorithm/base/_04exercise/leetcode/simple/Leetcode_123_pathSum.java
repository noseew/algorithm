package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 113. 路径总和 II
 * 给你二叉树的根节点 root 和一个整数目标和 targetSum ，找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
 *
 * 叶子节点 是指没有子节点的节点。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/path-sum-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_123_pathSum {

    @Test
    public void test() {
        TreeNode treeNode = new TreeNode();
        treeNode.val = 10;
        TreeNode right = new TreeNode();
        treeNode.right = right;
        right.val = 5;
        System.out.println(pathSum(treeNode, 15));
    }

    /**
     * 典型的 DFS思路
     * 支持负值
     */
    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        List<Integer> select = new ArrayList<>();

        dfs(root, targetSum, select, result);
        return result;
    }


    private void dfs(TreeNode root, int targetSum, List<Integer> select, List<List<Integer>> result) {
        // 如果是叶子结点, 且值相等, 则返回true
        if (targetSum == root.val && root.left == null && root.right == null) {
            select.add(root.val);
            result.add(new ArrayList<>(select));
            select.remove(select.size() - 1);
            return;
        }

        // 向左右探索
        if (root.left != null) {
            select.add(root.val);
            dfs(root.left, targetSum - root.val, select, result);
            select.remove(select.size() - 1);
        }
        if (root.right != null) {
            select.add(root.val);
            dfs(root.right, targetSum - root.val, select, result);
            select.remove(select.size() - 1);
        }
    }

}
