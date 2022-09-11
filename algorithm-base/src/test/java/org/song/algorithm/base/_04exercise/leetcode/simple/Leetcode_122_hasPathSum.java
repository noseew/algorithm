package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 112. 路径总和
 * 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。如果存在，返回 true ；否则，返回 false 。
 * <p>
 * 叶子节点 是指没有子节点的节点。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/path-sum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_122_hasPathSum {

    @Test
    public void test() {
        TreeNode treeNode = new TreeNode();
        treeNode.val = 10;
        TreeNode right = new TreeNode();
        treeNode.right = right;
        right.val = 5;
        System.out.println(hasPathSum(treeNode, 11));
    }

    /**
     * 典型的 DFS思路
     */
    public boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;
        return dfs(root, targetSum);
    }


    private boolean dfs(TreeNode root, int targetSum) {
        // 如果是叶子结点, 且值相等, 则返回true
        if (targetSum == root.val && root.left == null && root.right == null) {
            return true;
        }

        // 向左右探索
        boolean left = false, right = false;
        if (root.left != null) {
            left = dfs(root.left, targetSum - root.val);
        }
        if (root.right != null) {
            right = dfs(root.right, targetSum - root.val);
        }
        return left || right;
    }

}
