package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02_BST_base;

import java.util.Comparator;
import java.util.Random;

public class BST_test {

    @Test
    public void test_start1() {
        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            tree.push(random.nextInt(50));
        }
        BTreePrinter.print(tree.root);
    }

    @Test
    public void test_start2() {
        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            tree.push(random.nextInt(20));
        }
        BTreePrinter.print(tree.root);
        
        System.out.println("floor(5) = " + tree.floor(5));
        System.out.println("floor(8) = " + tree.floor(8));
        System.out.println("ceiling(5 = " + tree.ceiling(5));
        System.out.println("ceiling(8) = " + tree.ceiling(8));
    }
}
