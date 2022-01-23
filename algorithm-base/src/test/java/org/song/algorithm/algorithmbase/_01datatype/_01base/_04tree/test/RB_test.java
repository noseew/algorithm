package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02_BST_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model._02BSTTreeBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RB_test {

    @Test
    public void test_start1() {
        int max = 20;
        int size = 10;

        Tree05_RB_base<Integer> tree = new Tree05_RB_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.push(v);
        }
        BTreePrinter.print(tree.root, true);
    }
}
