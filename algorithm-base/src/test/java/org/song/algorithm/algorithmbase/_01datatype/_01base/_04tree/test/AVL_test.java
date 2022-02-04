package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_Ratio2;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_Ratio1;

import java.util.Comparator;
import java.util.Random;

public class AVL_test {

    @Test
    public void test_01_start() {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            tree.push(random.nextInt(100));
        }
        System.out.println(tree.toString());
    }

    @Test
    public void test_remove() {
        Tree03_AVL_base<Integer> tree = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));

        for (int i = 0; i < 20; i++) {
            tree.push(i);
        }
        System.out.println(tree.toString());
        for (int i = 0; i < 20; i++) {
            tree.remove(i);
            System.out.println(tree.toString());
        }
    }

    @Test
    public void test_02_start() {
        Tree03_AVL_base<Integer> tree1 = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));
        Tree03_AVL_Ratio1<Integer> tree2 = new Tree03_AVL_Ratio1<>(Comparator.comparing(Integer::doubleValue));
        Tree03_AVL_Ratio2<Integer> tree3 = new Tree03_AVL_Ratio2<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int val = random.nextInt(100);
            tree1.push(val);
            tree2.push(val);
            tree3.push(val);
        }
        System.out.println(tree1.toString());
        System.out.println();
        System.out.println(tree2.toString());
        System.out.println();
        System.out.println(tree3.toString());
    }
}
