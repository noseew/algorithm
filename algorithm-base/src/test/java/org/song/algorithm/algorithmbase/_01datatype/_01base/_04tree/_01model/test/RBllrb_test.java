package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_llrb;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_llrb02;

import java.util.Comparator;
import java.util.Random;

public class RBllrb_test {

    @Test
    public void test_start1() {
        int max = 30;
        int size = 20;

        Tree05_RB_llrb<Integer> tree = new Tree05_RB_llrb<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.add(v);
        }
        BTreePrinter.print(tree.root, true);
    }

    @Test
    public void test_start3() {
        int max = 30;
        int size = 20;

        Tree05_RB_llrb<Integer> tree1 = new Tree05_RB_llrb<>(Comparator.comparing(Integer::doubleValue));
        Tree05_RB_llrb02<Integer> tree2 = new Tree05_RB_llrb02<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree1.add(v);
            tree2.add(v);
        }
        BTreePrinter.print(tree1.root, true);
        BTreePrinter.print(tree2.root, true);
    }

    @Test
    public void test_start2() {

        Tree05_RB_llrb<Integer> tree = new Tree05_RB_llrb<>(Comparator.comparing(Integer::doubleValue));
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
        tree.remove(17);
        BTreePrinter.print(tree.root, true);
    }
}
