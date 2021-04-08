package algorithmbase.leetcode.simple;

/**
 * 101. 对称二叉树
 * 给定一个二叉树，检查它是否是镜像对称的。
 *
 * 例如，二叉树 [1,2,2,3,4,4,3] 是对称的。
 *
 * 但是下面这个 [1,2,2,null,3,null,3] 则不是镜像对称的:
 * 进阶：
 *
 * 你可以运用递归和迭代两种方法解决这个问题吗？
 */
public class Leetcode_101_isSymmetric {

    public boolean isSymmetric(TreeNode root) {
        TreeNode p = root.left;
        TreeNode q = root.right;

        if (p == null && q == null) {
            return true;
        }
        if (p == null && q != null) {
            return false;
        }
        if (q == null && p != null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSymmetric(p) && isSymmetric(q);
    }


    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null && q != null) {
            return false;
        }
        if (q == null && p != null) {
            return false;
        }
        if (p.val != q.val) {
            return false;
        }
        return isSameTree(p.left, q.right) && isSameTree(p.right, q.left);
    }

    public static void preOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.val + " ");
        preOrderTraveral(node.left);
        preOrderTraveral(node.right);
    }

    public static void postOrderTraveral(TreeNode node) {
        if (node == null) {
            return;
        }
        postOrderTraveral(node.left);
        postOrderTraveral(node.right);
        System.out.print(node.val + " ");
    }

    /**
     * Definition for a binary tree node.
     */
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
}
