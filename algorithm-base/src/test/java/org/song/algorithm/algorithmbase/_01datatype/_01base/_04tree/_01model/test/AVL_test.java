package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
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

}
