package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_base;

import java.util.Comparator;
import java.util.Random;

public class RB_test {

    @Test
    public void test_start1() {
        int max = 100;
        int size = 30;

        Tree05_RB_base<Integer> tree = new Tree05_RB_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.add(v);
        }
        BTreeUtils.print(tree.root, true);
    }
    @Test
    public void test_start2() {

        Tree05_RB_base<Integer> tree = new Tree05_RB_base<>(Comparator.comparing(Integer::doubleValue));
        tree.add(55);
        tree.add(38);
        tree.add(76);
        tree.add(25);
        tree.add(50);
        tree.add(72);
        tree.add(33);
        tree.add(17);
        tree.add(46);
        BTreeUtils.print(tree.root, true);
    }
}
