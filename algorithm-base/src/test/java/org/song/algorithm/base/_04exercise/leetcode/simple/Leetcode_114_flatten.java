package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

/**
 * 114. 二叉树展开为链表
 * 给你二叉树的根结点 root ，请你将它展开为一个单链表：
 * <p>
 * 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
 * 展开后的单链表应该与二叉树 先序遍历 顺序相同。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/flatten-binary-tree-to-linked-list
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_114_flatten {

    @Test
    public void test() {
        TreeNode treeNode = new TreeNode();
        treeNode.val = 10;
        TreeNode right = new TreeNode();
        treeNode.left = right;
        right.val = 5;
        flatten(treeNode);
    }

    public void flatten(TreeNode root) {
        if (root == null) return;

        // 类似 先序遍历
        if (root.left != null) {
            insertRight(root, root.left);
        }
        // 左子树已经没了, 所以直接右子树
        flatten(root.right);
    }

    /**
     * 将左子树插入到右子树中间
     */
    private void insertRight(TreeNode parent, TreeNode ih) {
        /*
        示例:
        1
      /  \
     2    5
    /  \   \
   3   4    6 
        一次 插入之后:
        1
         \
          2
         / \
        3   4
             \
              5
               \
                6
         */
        
        // parent 原本的右子节点
        TreeNode pr = parent.right;
        // parent 左子节点的右子节点
        TreeNode ir = ih.right;
        if (ir != null) {
            while (ir.right != null) {
                ir = ir.right;
            }
        } else {
            ir = ih;
        }
        ir.right = pr;
        parent.right = ih;
        parent.left = null;
    }
}
