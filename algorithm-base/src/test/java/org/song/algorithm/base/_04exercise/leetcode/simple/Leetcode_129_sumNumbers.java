package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 113. 路径总和 II
 * 给你二叉树的根节点 root 和一个整数目标和 targetSum ，找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
 * <p>
 * 叶子节点 是指没有子节点的节点。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/path-sum-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_129_sumNumbers {

    @Test
    public void test() {
        TreeNode treeNode = new TreeNode();
        treeNode.val = 10;
        TreeNode left = new TreeNode();
        left.val = 5;
        TreeNode right = new TreeNode();
        right.val = 5;


        treeNode.left = left;
        treeNode.right = right;
        System.out.println(sumNumbers(treeNode));
    }

    /**
     * 典型的 DFS思路
     * 参见 112. 路径总和
     */
    public int sumNumbers(TreeNode root) {
        int[] result = {0};
        if (root == null) return 0;

        List<Integer> select = new ArrayList<>();

        dfs(root, select, result);
        return result[0];
    }


    private void dfs(TreeNode root, List<Integer> select, int[] result) {
        // 如果是叶子结点, 且值相等, 则返回true
        if (root.left == null && root.right == null) {
            select.add(root.val);
            result[0] += list2Int(select);
            select.remove(select.size() - 1);
            return;
        }

        // 向左右探索
        if (root.left != null) {
            select.add(root.val);
            dfs(root.left, select, result);
            select.remove(select.size() - 1);
        }
        if (root.right != null) {
            select.add(root.val);
            dfs(root.right, select, result);
            select.remove(select.size() - 1);
        }
    }

    private int list2Int(List<Integer> select) {
        int sum = 0;
        for (int i = 0; i < select.size(); i++) {
            sum += select.get(i) * Math.pow(10, (select.size() - i - 1));
        }
//        System.out.println(select + "=" + sum);
        return sum;
    }

    @Test
    public void test2() {
        TreeNode treeNode = new TreeNode();
        treeNode.val = 10;
        TreeNode left = new TreeNode();
        left.val = 5;
        TreeNode right = new TreeNode();
        right.val = 5;


        treeNode.left = left;
        treeNode.right = right;
        System.out.println(sumNumbers2(treeNode));
    }

    /**
     * 典型的 DFS思路
     * 节省空间, 提高效率
     */
    public int sumNumbers2(TreeNode root) {
        int[] result = {0};
        if (root == null) return 0;

        dfs2(root, 0, result);
        return result[0];
    }


    private void dfs2(TreeNode root, int select, int[] result) {
        // 如果是叶子结点, 且值相等, 则返回true
        if (root.left == null && root.right == null) {
            result[0] += select * 10 + root.val;
            return;
        }

        // 向左右探索
        if (root.left != null) {
            dfs2(root.left, select * 10 + root.val, result);
        }
        if (root.right != null) {
            dfs2(root.right, select * 10 + root.val, result);
        }
    }

}
