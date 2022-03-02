package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.algorithmbase._04alg_exercise.leetcode.TreeNode;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 110. 平衡二叉树
 * 判断树是否平衡
 */
public class Leetcode_110_isBalanced {

    @Test
    public void test() {
    }

    public boolean isBalanced(TreeNode root) {
        AtomicBoolean balanced2 = new AtomicBoolean(true);
        getHeight(root, balanced2);
        return balanced2.get();
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
}
