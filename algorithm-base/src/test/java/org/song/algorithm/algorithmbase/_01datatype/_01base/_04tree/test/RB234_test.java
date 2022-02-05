package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB234_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB23_base;

import java.util.Comparator;
import java.util.Random;

public class RB234_test {

    @Test
    public void test_start1() {
        int max = 100;
        int size = 30;

        Tree05_RB234_base<Integer> tree = new Tree05_RB234_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.push(v);
        }
        BTreePrinter.print(tree.root, true);
    }
    @Test
    public void test_start2() {

        Tree05_RB234_base<Integer> tree = new Tree05_RB234_base<>(Comparator.comparing(Integer::doubleValue));
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
    }
}
