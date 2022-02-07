package org.song.algorithm.algorithmbase._02alg._03app.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._03app.leetcode.TreeNode;

/**
 * 108. 将有序数组转换为二叉搜索树
 */
public class Leetcode_108_sortedArrayToBST {

    @Test
    public void test() {
        TreeNode treeNode = sortedArrayToBST(new int[]{-10, -3, 0, 5, 9});
        System.out.println(treeNode);
    }

    public TreeNode sortedArrayToBST(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        Tree03_AVL_base avl = new Tree03_AVL_base();
        for (int num : nums) {
            avl.push(num);
        }
        return avl.root;
    }

    public class Tree03_AVL_base {

        public TreeNode root;

        public boolean push(int v) {
            root = insert_recursive(root, v);
            return true;
        }

        protected TreeNode insert_recursive(TreeNode parent, int v) {
            if (parent == null) {
                // 新建节点, 高度默认1
                parent = new TreeNode(v);
                return parent;
            }

            if (v < parent.val) {
                // 向左插入
                parent.left = insert_recursive(parent.left, v);
            } else if (v > parent.val) {
                // 向右插入
                parent.right = insert_recursive(parent.right, v);
            } else {
                parent.val = v; // 重复元素不处理 直接替换值
                return parent;
            }
            parent = balance(parent);
            return parent;
        }

        protected TreeNode balance(TreeNode node) {
            if (node == null) {
                return node;
            }

            // 左高右低
            if (getHeight(node.left) - getHeight(node.right) > 1) {
                if (getHeight(node.left.left) >= getHeight(node.left.right)) {
                    // LL型 / 右旋转
                    node = rightRotate4LL(node);
                } else {
                    // LR型 < 先左旋转再右旋转
                    node = leftRightRotate4LR(node);
                }
            }
            // 右高左低
            else if (getHeight(node.right) - getHeight(node.left) > 1) {
                if (getHeight(node.right.right) >= getHeight(node.right.left)) {
                    // RR型 \ 左旋转
                    node = leftRotation4RR(node);
                } else {
                    // RL型 > 先右旋转再左旋转
                    node = rightLeftRotate4RL(node);
                }
            }

            return node;
        }

        protected TreeNode leftRightRotate4LR(TreeNode p) {
            p.left = leftRotation4RR(p.left);
            return rightRotate4LL(p);
        }

        protected TreeNode rightLeftRotate4RL(TreeNode p) {
            p.right = rightRotate4LL(p.right);
            return leftRotation4RR(p);
        }

        protected TreeNode rightRotate4LL(TreeNode p) {
            TreeNode newParent = p.left;
            p.left = newParent.right;
            newParent.right = p;

            return newParent;
        }

        protected TreeNode leftRotation4RR(TreeNode p) {
            TreeNode newParent = p.right;
            p.right = newParent.left;
            newParent.left = p;

            return newParent;
        }

        protected int getHeight(TreeNode root) {
            if (root == null) {
                return 0;
            }
            return Math.max(getHeight(root.left), getHeight(root.right)) + 1;
        }
    }
}
