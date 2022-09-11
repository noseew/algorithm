package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 98. 验证二叉搜索树
 * 给你一个二叉树的根节点 root ，判断其是否是一个有效的二叉搜索树。
 *
 * 有效 二叉搜索树定义如下：
 *
 * 节点的左子树只包含 小于 当前节点的数。
 * 节点的右子树只包含 大于 当前节点的数。
 * 所有左子树和右子树自身必须也是二叉搜索树。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/validate-binary-search-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_98_isValidBST {

    @Test
    public void test() {
        TreeNode root = new TreeNode();
        TreeNode p = new TreeNode();
        TreeNode q = new TreeNode();
        root.left = p;
        root.right = q;
        System.out.println(isValidBST(root));
    }

    /**
     * 中序遍历, 判断顺序即可
     */
    public boolean isValidBST(TreeNode root) {
        middleOrder(root);
        return isBst;
    }

    private TreeNode prev;
    private boolean isBst = true;

    private void middleOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        middleOrder(root.left);
        if (prev != null && prev.val >= root.val) {
            isBst = false;
            return;
        }
        prev = root;
        middleOrder(root.right);
    }
}
