package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_jdkhashmap;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_jdkhotspot;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_jdktreemap;

import java.util.Comparator;
import java.util.Random;

public class RB_JDK_test {

    @Test
    public void test_start1() {
        int max = 100;
        int size = 30;

        Tree05_RB_jdkhashmap<Integer> tree = new Tree05_RB_jdkhashmap<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.add(v);
        }
        BTreeUtils.print(tree.root, true);
        int lastSize = tree.size;
        for (int i = 0; i < size; i++) {
            tree.remove(i);
            if (tree.size < lastSize) {
                BTreeUtils.print(tree.root, true);
            }
        }
    }

    @Test
    public void test_start2() {
        int max = 100;
        int size = 30;

        Tree05_RB_jdktreemap<Integer> tree = new Tree05_RB_jdktreemap<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.put(v);
        }
        BTreeUtils.print(tree.root, true);
//        int lastSize = tree.size;
//        for (int i = 0; i < size; i++) {
//            tree.remove(i);
//            if (tree.size < lastSize) {
//                BTreePrinter.print(tree.root, true);
//            }
//        }
    }

    @Test
    public void test_start3() {
        int max = 100;
        int size = 30;

        Tree05_RB_jdkhotspot<Integer> tree = new Tree05_RB_jdkhotspot<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.put(v);
        }
        BTreeUtils.print(tree.root, true);
        BTreeUtils.printJDK9(tree.root);
//        int lastSize = tree.size;
//        for (int i = 0; i < size; i++) {
//            tree.remove(i);
//            if (tree.size < lastSize) {
//                BTreePrinter.print(tree.root, true);
//            }
//        }
    }

    @Test
    public void test_vs() {
        int max = 100;
        int size = 20;

        Tree05_RB_jdktreemap<Integer> treemap = new Tree05_RB_jdktreemap<>(Comparator.comparing(Integer::doubleValue));
        Tree05_RB_jdkhashmap<Integer> hashmap = new Tree05_RB_jdkhashmap<>(Comparator.comparing(Integer::doubleValue));
        Tree05_RB_jdkhotspot<Integer> hotspot = new Tree05_RB_jdkhotspot<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            treemap.put(v);
            hashmap.add(v);
            hotspot.put(v);
        }
        BTreeUtils.print(treemap.root, true);
        BTreeUtils.print(hashmap.root, true);
        BTreeUtils.print(hotspot.root, true);
//        int lastSize = tree.size;
//        for (int i = 0; i < size; i++) {
//            tree.remove(i);
//            if (tree.size < lastSize) {
//                BTreePrinter.print(tree.root, true);
//            }
//        }
    }

    @Test
    public void test_vs2() {
        int max = 100;
        int size = 10;

        Tree05_RB_jdktreemap<Integer> treemap = new Tree05_RB_jdktreemap<>(Comparator.comparing(Integer::doubleValue));
        Tree05_RB_base<Integer> rb = new Tree05_RB_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            treemap.put(v);
            rb.add(v);
        }
        BTreeUtils.print(treemap.root, true);
        BTreeUtils.print(rb.root, true);
//        int lastSize = tree.size;
//        for (int i = 0; i < size; i++) {
//            tree.remove(i);
//            if (tree.size < lastSize) {
//                BTreePrinter.print(tree.root, true);
//            }
//        }
    }
}
