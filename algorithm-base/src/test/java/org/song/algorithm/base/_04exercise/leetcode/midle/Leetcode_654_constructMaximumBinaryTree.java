package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 654. 最大二叉树
 * 给定一个不重复的整数数组 nums 。 最大二叉树 可以用下面的算法从 nums 递归地构建:
 *
 * 创建一个根节点，其值为 nums 中的最大值。
 * 递归地在最大值 左边 的 子数组前缀上 构建左子树。
 * 递归地在最大值 右边 的 子数组后缀上 构建右子树。
 * 返回 nums 构建的 最大二叉树 。
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 * nums 中的所有整数 互不相同
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/maximum-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_654_constructMaximumBinaryTree {

    @Test
    public void test() {
        TreeNode treeNode = constructMaximumBinaryTree(new int[]{3, 2, 1, 6, 0, 5});
        System.out.println(treeNode);

    }

    /**
     * 由于题目给出的值范围不大, 所以可以使用递归解决
     */
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        if (nums.length == 1) return new TreeNode(nums[0]);
        return constructMaximumBinaryTree1(nums, 0, nums.length);
    }
    
    private TreeNode constructMaximumBinaryTree1(int[] nums, int l, int r) {
        if (l >= r) return null;
        int maxIndex = l;
        for (int i = l; i < r; i++) {
            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }

        TreeNode parent = new TreeNode(nums[maxIndex]);
        parent.left = constructMaximumBinaryTree1(nums, l, maxIndex);
        parent.right = constructMaximumBinaryTree1(nums, maxIndex + 1, r);
        return parent;
    }
}
