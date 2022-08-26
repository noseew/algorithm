package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 572. 另一棵树的子树
 *
 * 给你两棵二叉树 root 和 subRoot 。检验 root 中是否包含和 subRoot 具有相同结构和节点值的子树。如果存在，返回 true ；否则，返回 false 。
 *
 * 二叉树 tree 的一棵子树包括 tree 的某个节点和这个节点的所有后代节点。tree 也可以看做它自身的一棵子树。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/subtree-of-another-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_572_isSubtree {

    @Test
    public void test() {
        TreeNode root = new TreeNode(1,
                new TreeNode(2, new TreeNode(3), null),
                new TreeNode(2, null, new TreeNode(3)));
        TreeNode subRoot = new TreeNode(1,
                new TreeNode(2, new TreeNode(3), null),
                new TreeNode(2, null, new TreeNode(3)));
        System.out.println(isSubtree(root, subRoot));
    }

    /**
     * 思路
     * 将两棵树序列化成String, 判断一个树的是否是另一颗树的子串
     */
    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        return serial(root).contains(serial(subRoot));
    }

    /**
     * 树的序列化
     * !表示值
     * #!表示空节点
     * 
     * 只要(根左右)三个节点能序列化在一起, 就可以, 所以不能采用层序遍历
     * 注意: 
     * 采用先序遍历的时候, 需要在最前面做个分隔符, 
     * 采用后续遍历的时候, 需要在最后面做个分隔符, 
     */
    private String serial(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serial(root, sb);
        return sb.toString();
    }
    
    private void serial(TreeNode root, StringBuilder sb) {
        if (root == null) {
            // 表示空节点
            sb.append("#!");
            return;
        }
        serial(root.left, sb);
        serial(root.right, sb);
        // 使用后续遍历, 分隔符在后面
        sb.append(root.val).append("!");
    }
}
