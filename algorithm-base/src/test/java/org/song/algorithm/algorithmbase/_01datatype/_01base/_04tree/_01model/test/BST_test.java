package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02_BST_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.AbsBSTTree;

import java.util.*;
import java.util.stream.Collectors;

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

    @Test
    public void test_traverse() {
        int max = 20;
        int size = 10;

        Tree02_BST_base<Integer> tree = new Tree02_BST_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            int v = random.nextInt(max);
            tree.push(v);
        }
        BTreePrinter.print(tree.root, true);

        tree.traverse(tree.root, AbsBSTTree.Order.MidOrder, e -> {
            System.out.println(e);
            return true;
        });
        System.out.println("条件打印");
        tree.traverse(tree.root, AbsBSTTree.Order.MidOrder, e -> {
            if (e < 10) {
                System.out.println(e);
                return true;
            }
            return false;
        });
        
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

    @Test
    public void test_start3_range_AutoTest() {
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

        for (int i = 1; i <= size; i++) {
            int min = random.nextInt(i);
            int max2 = random.nextInt(min + 10);
            List<Integer> range = tree.range(min, max2);
            List<Integer> realRange = list.stream().filter(e -> e >= min && e < max2).collect(Collectors.toList());
            if (!realRange.equals(range)) {
                System.out.println(String.format("error min=%s, max=%s, realRange=%s, range=%s", min, max2, realRange, range));
            }
        }
    }
}
