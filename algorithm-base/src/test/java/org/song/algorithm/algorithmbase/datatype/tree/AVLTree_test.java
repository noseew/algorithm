package org.song.algorithm.algorithmbase.datatype.tree;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.datatype.tree.model.AVLTree_base;

public class AVLTree_test {

    @Test
    public void test_01_start() {
        AVLTree_base<Integer> tree = new AVLTree_base<>();
        tree.push(1);
        tree.push(2);
        tree.push(3);
        BTreePrinter.printNode(tree.root);
    }
}
