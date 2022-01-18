package org.song.algorithm.algorithmbase._01datatype._01base._04tree._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.TreeNode;

import java.util.Stack;

public class Tree_alg {

    private TreeNode<Integer> initALVBinaryTree(int count) {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>();
        for (int i = 0; i < count; i++) {
            tree.insert(i);
        }
        return tree.root;
    }

    /**
     * 二叉树的遍历 递归
     */
    @Test
    public void test_01_traverse_01() {
        TreeNode<Integer> root = initALVBinaryTree(3);
        recursive_01(root, 2);
    }

    /**
     * 0: 前序遍历 根-> 左-> 右
     * 1: 中序遍历 左-> 根-> 右
     * 2: 后序遍历 左-> 右-> 根
     *
     * @param node
     * @param order 遍历顺序 0: 前序, 1: 中序, 2: 后序
     */
    private void recursive_01(TreeNode<Integer> node, int order) {
        if (node == null) {
            return;
        }
        if (order == 0) {
            System.out.println(node.v);
        }

        recursive_01(node.left, order);

        if (order == 1) {
            System.out.println(node.v);
        }

        recursive_01(node.right, order);

        if (order == 2) {
            System.out.println(node.v);
        }
    }

    /**
     * 二叉树的遍历 循环
     */
    @Test
    public void test_02_traverse_02() {
        TreeNode<Integer> root = initALVBinaryTree(3);
        recursive_02(root, 0);
    }

    private void recursive_02(TreeNode<Integer> node, int order) {
        if (node == null) {
            return;
        }
        Stack<TreeNode<Integer>> stack = new Stack<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                if (order == 0) {
                    System.out.println(node.v);
                }

                stack.push(node);
                node = node.left;
            }
            if (!stack.isEmpty()) {
                node = stack.pop();

                if (order == 1) {
                    System.out.println(node.v);
                }

                node = node.right;
            }
        }
    }

}
