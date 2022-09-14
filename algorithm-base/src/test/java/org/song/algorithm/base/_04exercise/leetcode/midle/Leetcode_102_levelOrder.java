package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 102. 二叉树的层序遍历
 */
public class Leetcode_102_levelOrder {

    @Test
    public void test() {
    }

    static TreeNode nullNode = new TreeNode();

    /**
     * 未完成 TODO
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) return new ArrayList<>();

        List<List<Integer>> result = new ArrayList<>();
        
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        queue.add(nullNode);
        while (!queue.isEmpty()) {
            root = queue.pollFirst();
            if (root != nullNode) {
                System.out.print(root.val + " ");
            } else {
                System.out.print("|");
                continue;
            }
            if (root.left != null) {
                queue.addLast(root.left);
            }
            if (root.right != null) {
                queue.addLast(root.right);
            }
            queue.add(nullNode);
        }
        return result;
    }
    
}
