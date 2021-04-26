package org.song.algorithm.algorithmbase.datatype.tree;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.datatype.tree.model.AVLTree_base;

public class AVLTree_test {

    @Test
    public void test_01_start() {
        AVLTree_base<Integer> tree = new AVLTree_base<>();
        for (int i = 0; i < 10; i++) {
            tree.push_recursive(i);
        }
        BTreePrinter.printNode(tree.root);
        System.out.println(tree.toString());

        System.out.println(tree.search_recursive(9));

        tree.remove_recursive(3);
        BTreePrinter.printNode(tree.root);
    }
}
