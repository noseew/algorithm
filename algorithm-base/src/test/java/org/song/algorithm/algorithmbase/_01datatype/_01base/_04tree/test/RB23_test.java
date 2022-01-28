package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB23_base;

import java.util.Comparator;
import java.util.Random;

public class RB23_test {

    @Test
    public void test_start1() {
        int max = 20;
        int size = 10;

        Tree05_RB23_base<Integer> tree = new Tree05_RB23_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.push(v);
        }
        BTreePrinter.print(tree.root, true);
    }
}
