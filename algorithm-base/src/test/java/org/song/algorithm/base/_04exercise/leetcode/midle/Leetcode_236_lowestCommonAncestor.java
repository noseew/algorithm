package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 236. 二叉树的最近公共祖先
 * <p>
 * 给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。
 * <p>
 * 百度百科中最近公共祖先的定义为：“对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（一个节点也可以是它自己的祖先）。”
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_236_lowestCommonAncestor {

    @Test
    public void test() {
        TreeNode root = new TreeNode();
        TreeNode p = new TreeNode();
        TreeNode q = new TreeNode();
        root.left = p;
        root.right = q;
        System.out.println(lowestCommonAncestor(root, p, q));
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            // 如果有一个为root, 则公共组件一定是root
            return root;
        }
        /*
        否则, 从左右子树分别查找
        找的结果一定是
        1. p,q在left子树中, 则 left不为null
        2. p,q在right子树中, 则 right不为null
        3. p,q一个在left中, 另一个在right中, left和right都不为null
        5. p,q两个都不在: 根据题意这个选项不存在
         */
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null) {
            // 情况1
            return root;
        }
        // 包含情况2/3/4
        return left != null ? left : right;

    }
}
