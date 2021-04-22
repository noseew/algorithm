package org.song.algorithm.algorithmbase.datatype.tree;

import org.junit.Test;
import org.song.algorithm.algorithmbase.datatype.tree.model.BTreeNode;

import java.util.LinkedList;
import java.util.Stack;

public class BTree_test {

    public BTreeNode<Integer> buildTree() {
        BTreeNode<Integer> root = new BTreeNode<>(null, null, null, 0);

        BTreeNode<Integer> l1 = new BTreeNode<>(root, null, null, 1);
        BTreeNode<Integer> r1 = new BTreeNode<>(root, null, null, 2);
        root.left = l1;
        root.right = r1;

        BTreeNode<Integer> l11 = new BTreeNode<>(l1, null, null, 3);
        l1.left = l11;
        BTreeNode<Integer> l12 = new BTreeNode<>(l1, null, null, 4);
        l1.right = l12;
        BTreeNode<Integer> r11 = new BTreeNode<>(r1, null, null, 5);
        r1.left = r11;
        BTreeNode<Integer> r12 = new BTreeNode<>(r1, null, null, 6);
        r1.right = r12;
        return root;
    }

    /**
     * 树的遍历
     */
    @Test
    public void traverse() {
        BTreeNode<Integer> root = buildTree();

//        preOrder(root);
        inOrder(root);
//        postOrder(root);

    }

    /********************************** 递归方式 ************************************/

    /**
     * 先序遍历 根 -> 左 -> 右
     *
     * @param node
     */
    public static <V> void preOrder(BTreeNode<V> node) {
        if (node == null) {
            return;
        }
        System.out.println(node.v);
        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * 中序遍历 左 -> 根 -> 右
     *
     * @param node
     */
    public static <V> void inOrder(BTreeNode<V> node) {
        if (node == null) {
            return;
        }
        inOrder(node.left);
        System.out.println(node.v);
        inOrder(node.right);
    }

    /**
     * 后序遍历 左 -> 右 -> 根
     *
     * @param node
     */
    public static <V> void postOrder(BTreeNode<V> node) {
        if (node == null) {
            return;
        }
        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.v);
    }


    /********************************** 循环方式 ************************************/

    /**
     * 先序遍历 根 -> 左 -> 右
     *
     * @param node
     */
    public static <V> void preOrder2(BTreeNode<V> node) {
        Stack<BTreeNode<V>> stack = new Stack<>();
        while (node != null || !stack.isEmpty()) {
            while (node != null) {
                System.out.println(node.v);
                stack.push(node);
                node = node.left;
            }
            if (!stack.isEmpty()) {
                node = stack.pop();
                node = node.right;
            }

        }
    }

    /**
     * 中序遍历 左 -> 根 -> 右
     *
     * @param node
     */
    public static <V> void inOrder2(BTreeNode<V> node) {
    }

    /**
     * 后序遍历 左 -> 右 -> 根
     *
     * @param node
     */
    public static <V> void postOrder2(BTreeNode<V> node) {
    }


    /**
     * 层序遍历
     *
     * @param root
     */
    public static <V> void levelOrder(BTreeNode<V> root) {
        LinkedList<BTreeNode<V>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            root = queue.pop();
            System.out.print(root.v);
            if (root.left != null) {
                queue.add(root.left);
            }
            if (root.right != null) {
                queue.add(root.right);
            }
        }
    }

}
