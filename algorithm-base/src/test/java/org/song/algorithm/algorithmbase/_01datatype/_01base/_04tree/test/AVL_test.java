package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;

import java.util.Comparator;

public class AVL_test {

    @Test
    public void test_01_start() {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));
        for (int i = 0; i < 10; i++) {
            tree.push(i);
        }
//        BTreePrinter.printNode(tree.root);
        BTreePrinter.print(tree.root);
        System.out.println(tree.toString());

        System.out.println(tree.get(9));

        tree.remove(3);
//        BTreePrinter.printNode(tree.root);
        BTreePrinter.print(tree.root);
    }
}
