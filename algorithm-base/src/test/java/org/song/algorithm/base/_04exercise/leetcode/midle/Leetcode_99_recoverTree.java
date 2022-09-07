package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 99. 恢复二叉搜索树
 * <p>
 * 给你二叉搜索树的根节点 root ，该树中的 恰好 两个节点的值被错误地交换。请在不改变其结构的情况下，恢复这棵树 。
 */
public class Leetcode_99_recoverTree {

    @Test
    public void test() {
        TreeNode root = new TreeNode();
        TreeNode p = new TreeNode();
        TreeNode q = new TreeNode();
        root.left = p;
        root.right = q;
        recoverTree(root);
    }

    /**
     * 思路, BST 中序遍历是有序的(升序), 这里进行中序遍历, 然后找到逆序(错误的)节点, 记录并交换
     * 逆序分两种, 假设逆序节点是, a,b, 变成了b,a
     * 1. 两个在一起形成一个逆序, b-a,交换他们之后, 顺序自动变成有序
     * 2. 两个不在一起形成两个逆序, b-n1,n2-a, 思路也是一样,交换他们之后, 顺序自动变成有序
     */
    public void recoverTree(TreeNode root) {
        inOrder(root);
        // 交换逆序对
        int val = last.val;
        last.val = first.val;
        first.val = val;
    }

    // 分别代表两个逆序后的, first = b, last = a
    private TreeNode first, last;
    // 上一个节点, 用于比较大小
    private TreeNode prev;

    private void inOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        inOrder(root.left);
        if (prev != null && prev.val > root.val) {
            // 出现逆序对
            if (first == null) {
                // 第一个逆序对的头
                first = prev;
            }
            // 第二个逆序对的尾, 这里也表示第一个逆序对的尾
            last = root;
        }
        prev = root;
        inOrder(root.right);
    }

    private void inOrder2(TreeNode root) {
        if (root == null) {
            return;
        }
        inOrder2(root.left);
        // 出现逆序对
        if (prev != null && prev.val > root.val) {
            // 第二个逆序对的尾, 这里也表示第一个逆序对的尾
            last = root;
            
            if (first != null) {
                // 此时已经是第二个逆序对, 可以return
                return;
            }
            // 第一个逆序对的头
            first = prev;
        }
        prev = root;
        inOrder2(root.right);
    }
}
