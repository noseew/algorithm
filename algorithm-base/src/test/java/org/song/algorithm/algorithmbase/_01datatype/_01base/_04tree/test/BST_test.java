package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02_BST_base;

import java.util.Comparator;

public class BST_test {

    @Test
    public void test_start1() {
        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));
        tree.push(5);
        tree.push(3);
        tree.push(11);
        tree.push(9);
        tree.push(13);
        tree.push(7);
        tree.push(1);
        BTreePrinter.print(tree.root);
        System.out.println(tree.floor(8));
        System.out.println(tree.ceiling(8));
    }
}
