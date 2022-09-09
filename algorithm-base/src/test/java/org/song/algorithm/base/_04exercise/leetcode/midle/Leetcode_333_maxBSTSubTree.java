package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 333. 最大BST子树,
 * 由于是会员, 这里只写不提交
 */
public class Leetcode_333_maxBSTSubTree {

    @Test
    public void test() {
        TreeNode root = new TreeNode();
    }

    public int maxBSTSubTree(TreeNode root) {
        if (root == null) return 0;
        /*
        思路
        1. 如果root是bst, 则直接返回节点数
        2. 否则, 返回left或right最大的数量
        
        自顶向下, 效率O(n^2)
         */
        if (isBST(root)) {
            return count(root);
        }

        return Math.max(maxBSTSubTree(root.left), maxBSTSubTree(root.right));
    }

    private int count(TreeNode root) {
        AtomicInteger count = new AtomicInteger();
        count(root, count);
        return count.get();
    }

    private void count(TreeNode root, AtomicInteger count) {
        if (root == null) return;
        count(root.left, count);
        count.incrementAndGet();
        count(root.right, count);
    }

    private boolean isBST(TreeNode root) {
        if (root == null) return true;
        AtomicBoolean bst = new AtomicBoolean();
        isBST(root, bst);
        return bst.get();
    }

    private TreeNode prev;

    private void isBST(TreeNode root, AtomicBoolean bst) {
        if (root == null) return;
        isBST(root.left, bst);
        if (prev != null && prev.val >= root.val) {
            bst.set(false);
            prev = null;
            return;
        }
        prev = root;
        isBST(root.right, bst);
    }
}
