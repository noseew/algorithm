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
            tree.push(v);
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
            tree1.push(v);
            tree2.push(v);
        }
        BTreePrinter.print(tree1.root, true);
        BTreePrinter.print(tree2.root, true);
    }

    @Test
    public void test_start2() {

        Tree05_RB_llrb<Integer> tree = new Tree05_RB_llrb<>(Comparator.comparing(Integer::doubleValue));
        tree.push(55);
        tree.push(38);
        tree.push(76);
        tree.push(25);
        tree.push(50);
        tree.push(72);
        tree.push(33);
        tree.push(17);
        tree.push(46);
        BTreePrinter.print(tree.root, true);
        tree.remove(17);
        BTreePrinter.print(tree.root, true);
    }
}
