package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02_BST_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;

import java.util.Comparator;
import java.util.Random;

public class AVL_test {

    @Test
    public void test_01_start() {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            tree.add(random.nextInt(100));
        }
        System.out.println(tree.toString());
    }

    @Test
    public void test_remove() {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));

        for (int i = 0; i < 20; i++) {
            tree.add(i);
        }
        System.out.println(tree.toString());
        for (int i = 0; i < 20; i++) {
            tree.remove(i);
            System.out.println(tree.toString());
        }
    }
    @Test
    public void test_remove2() {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));
        tree.add(55);
        tree.add(38);
        tree.add(76);
        tree.add(25);
        tree.add(50);
        tree.add(72);
        tree.add(33);
        tree.add(17);
        tree.add(46);

        BTreePrinter.print(tree.root, true);
        tree.remove(55);
        System.out.println(55);
        BTreePrinter.print(tree.root, true);
        tree.remove(38);
        System.out.println(38);
        BTreePrinter.print(tree.root, true);
        tree.remove(76);
        System.out.println(76);
        BTreePrinter.print(tree.root, true);
        tree.remove(25);
        System.out.println(25);
        BTreePrinter.print(tree.root, true);
        tree.remove(50);
        System.out.println(50);
        BTreePrinter.print(tree.root, true);
        tree.remove(72);
        System.out.println(72);
        BTreePrinter.print(tree.root, true);
        tree.remove(33);
        System.out.println(33);
        BTreePrinter.print(tree.root, true);
        tree.remove(17);
        System.out.println(17);
        BTreePrinter.print(tree.root, true);
        tree.remove(46);
        System.out.println(46);
        BTreePrinter.print(tree.root, true);
    }

}
