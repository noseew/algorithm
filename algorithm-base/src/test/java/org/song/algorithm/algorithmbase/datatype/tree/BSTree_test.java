package org.song.algorithm.algorithmbase.datatype.tree;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.datatype.tree.model.BSTree_base;

public class BSTree_test {

    @Test
    public void test_start() {
        BSTree_base<Integer> tree = new BSTree_base<>();
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
