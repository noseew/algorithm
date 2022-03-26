package org.song.algorithm.algorithmbase._01datatype._01base._04tree._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02BST01;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03AVL;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicBoolean;

public class TreeAlg {

    private TreeNode<Integer> initAVLTreeNode(int count) {
        Tree03AVL<Integer> tree = new Tree03AVL<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < count; i++) {
            tree.add(i);
        }
        return tree.root;
    }

    private TreeNode<Integer> initBSTTreeNode(int count) {
        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < count; i++) {
            tree.add(i);
        }
        return tree.root;
    }

    private Tree03AVL<Integer> initAVLTree(int count) {
        Tree03AVL<Integer> tree = new Tree03AVL<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < count; i++) {
            tree.add(i);
        }
        return tree;
    }

    /**
     * 获取二叉树的高度
     */
    @Test
    public void test_03_height() {
        TreeNode<Integer> root = initAVLTreeNode(20);
        int height = getHeightRecursive(root);
        System.out.println(height);

        BTreeUtils.print(root, true);
    }
    @Test
    public void test_03_minHeight() {
//        TreeNode<Integer> root = initAVLTreeNode(20);
        TreeNode<Integer> root = initBSTTreeNode(20);
        int height = minHeightRecursive(root);
        System.out.println(height);

//        BTreePrinter.print(root, true);
    }

    /**
     * 二叉树还原
     */
    @Test
    public void test_04_restore() {
        Tree03AVL<Integer> avlTree = initAVLTree(20);
        BTreeUtils.print(avlTree.root, true);

    }

    /**
     * 判断树是否平衡
     */
    @Test
    public void isBalanced() {
        TreeNode<Integer> root = initAVLTreeNode(20);
        AtomicBoolean balanced = new AtomicBoolean(true);
        getHeight(root, balanced);
        System.out.println("" + balanced.get());
        BTreeUtils.print(root, true);
        
        System.out.println();
        
        TreeNode<Integer> root2 = initBSTTreeNode(5);
        AtomicBoolean balanced2 = new AtomicBoolean(true);
        getHeight(root2, balanced2);
        System.out.println("" + balanced2.get());
        BTreeUtils.print(root2, true);
    }


    protected int getHeight(TreeNode root, AtomicBoolean balanced) {
        if (root == null) {
            return 0;
        }
        int leftHeight = getHeight(root.left, balanced);
        int rightHeight = getHeight(root.right, balanced);
        int diff = Math.abs(leftHeight - rightHeight);
        if (diff > 1) {
            balanced.set(false);
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }



    /**
     * 二叉树的高度 以最大的高度为准
     * 
     * @param root
     * @return
     */
    private static int getHeightRecursive(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(getHeightRecursive(root.left), getHeightRecursive(root.right)) + 1;
    }
    
    protected int minHeightRecursive(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.min(minHeightRecursive(root.left), minHeightRecursive(root.right)) + 1;
    }

}
