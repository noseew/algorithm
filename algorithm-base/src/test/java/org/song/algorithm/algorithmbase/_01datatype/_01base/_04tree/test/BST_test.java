package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02_BST_base;

public class BST_test {

    @Test
    public void test_start() {
        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>();
        tree.push(5);
        tree.push(3);
        tree.push(6);
        tree.push(4);
        tree.push(8);
        tree.push(7);
        System.out.println();
        BTreePrinter.printNode(tree.root);
        BTreePrinter.show(tree.root);
    }
}
