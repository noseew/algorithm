package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
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
            tree.push(v);
        }
        BTreePrinter.print(tree.root, true);
        int lastSize = tree.size;
        for (int i = 0; i < size; i++) {
            tree.remove(i);
            if (tree.size < lastSize) {
                BTreePrinter.print(tree.root, true);
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
        BTreePrinter.print(tree.root, true);
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
        BTreePrinter.print(tree.root, true);
        BTreePrinter.printJDK9(tree.root);
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
            hashmap.push(v);
            hotspot.put(v);
        }
        BTreePrinter.print(treemap.root, true);
        BTreePrinter.print(hashmap.root, true);
        BTreePrinter.print(hotspot.root, true);
//        int lastSize = tree.size;
//        for (int i = 0; i < size; i++) {
//            tree.remove(i);
//            if (tree.size < lastSize) {
//                BTreePrinter.print(tree.root, true);
//            }
//        }
    }
}
