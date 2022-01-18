package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;

public class AVLTree_test {

    @Test
    public void test_01_start() {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>();
        for (int i = 0; i < 10; i++) {
            tree.insert(i);
        }
        BTreePrinter.printNode(tree.root);
        System.out.println(tree.toString());

        System.out.println(tree.search(9));

        tree.remove(3);
        BTreePrinter.printNode(tree.root);
    }
}
