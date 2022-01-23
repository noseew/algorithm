package org.song.algorithm.algorithmbase._01datatype._01base._04tree.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02_BST_base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BST_test {

    @Test
    public void test_start1() {
        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            tree.push(random.nextInt(50));
        }
        BTreePrinter.print(tree.root, true);
    }

    @Test
    public void test_start2_floor() {
        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            tree.push(random.nextInt(20));
        }
        BTreePrinter.print(tree.root, true);
        
        System.out.println("floor(5) = " + tree.floor(5));
        System.out.println("floor(8) = " + tree.floor(8));
        System.out.println("floor(15) = " + tree.floor(15));
        System.out.println("floor(20) = " + tree.floor(20));
    }

    /**
     * 自动测试 test
     */
    @Test
    public void test_start2_floor_AutoTest() {

        int max = 1000;
        int size = 100;
        
        List<Integer> list = new ArrayList<>(size);
        
        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.push(v);
            list.add(v);
        }
        BTreePrinter.print(tree.root, true);

        for (int i = 0; i < size; i++) {
            int val = i;
            Integer floor = tree.floor(val);
            if (floor == null && list.stream().filter(e-> e <= val).max(Comparator.comparing(Integer::doubleValue)).orElse(null) == null) {
                continue;
            }
            // <= v 的 最大的那个
            if (floor == list.stream().filter(e-> e <= val).max(Comparator.comparing(Integer::doubleValue)).orElse(null)) {
                continue;
            }
            System.out.println("error " + i);
        }
    }

    @Test
    public void test_start3_ceiling_AutoTest() {
        int max = 1000;
        int size = 100;

        List<Integer> list = new ArrayList<>(size);

        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.push(v);
            list.add(v);
        }
        BTreePrinter.print(tree.root, true);

        for (int i = 0; i < size; i++) {
            int val = i;
            Integer ceiling = tree.ceiling(val);
            if (ceiling == null && list.stream().filter(e-> e >= val).min(Comparator.comparing(Integer::doubleValue)).orElse(null) == null) {
                continue;
            }
            // >= v 的 最小的那个
            if (ceiling == list.stream().filter(e-> e >= val).min(Comparator.comparing(Integer::doubleValue)).orElse(null)) {
                continue;
            }
            System.out.println("error " + i);
        }
    }

    @Test
    public void test_start3_rank_AutoTest() {
        int max = 20;
        int size = 10;

        List<Integer> list = new ArrayList<>(size);

        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            if (tree.push(v)) {
                list.add(v);
            }
        }
        BTreePrinter.print(tree.root, true);

        for (int val : list) {
            int rank = tree.rank(val);
            long realRank = list.stream().filter(e -> e < val).count() + 1;
            if (rank == realRank) {
                continue;
            }
            System.out.println(String.format("error realRank=%s, rank=%s", realRank, rank));
            
        }
    }
}
