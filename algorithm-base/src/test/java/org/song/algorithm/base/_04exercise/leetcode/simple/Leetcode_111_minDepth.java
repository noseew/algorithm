package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 111. 二叉树的最小深度
 */
public class Leetcode_111_minDepth {

    @Test
    public void test() {
    }

    /**
     * 未通过 
     * 输入：
     * [2,null,3,null,4,null,5,null,6]
     * 输出：
     * 1
     * 预期结果：
     * 5
     * 
     * @param root
     * @return
     */
    public int minDepth(TreeNode root) {
        return minHeight(root);
    }

    protected int minHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.min(minHeight(root.left), minHeight(root.right)) + 1;
    }
}
