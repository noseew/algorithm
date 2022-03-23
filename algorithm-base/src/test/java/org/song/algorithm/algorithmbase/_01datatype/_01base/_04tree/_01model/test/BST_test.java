package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02BST01;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02BST02;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.AbsBSTTree;

import java.util.*;
import java.util.stream.Collectors;

public class BST_test {

    private final int maxValue = 50;
    private final int valueSize = 15;

    @Test
    public void test_start_add() {
        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            tree.add(random.nextInt(maxValue));
        }
        // 循环引用检测
        boolean check = BTreeUtils.cycleCheck(tree.root);
        System.out.println(check);
        if (check) {
            System.out.println(BTreeUtils.print(tree.root, false));
        }
    }

    @Test
    public void test_start_add2() {
        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));

        List<Integer> sort = new ArrayList<>(valueSize);

        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            if (tree.add(v)) {
                sort.add(v);
                System.out.println(v);
                System.out.println(BTreeUtils.print(tree.root, false));
                sort.sort(Comparator.comparing(Integer::doubleValue));
                System.out.println(Arrays.toString(sort.toArray()));
                System.out.println();
            }
        }
        // 循环引用检测
        boolean check = BTreeUtils.cycleCheck(tree.root);
        System.out.println(check);
        if (check) {
            System.out.println(BTreeUtils.print(tree.root, false));
        }
    }

    @Test
    public void test_start_remove() {
//        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Tree02BST02<Integer> tree = new Tree02BST02<>(Comparator.comparing(Integer::doubleValue));
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
        tree.remove(55);
        System.out.println(55);
        BTreeUtils.print(tree.root, true);
        tree.remove(38);
        System.out.println(38);
        BTreeUtils.print(tree.root, true);
        tree.remove(76);
        System.out.println(76);
        BTreeUtils.print(tree.root, true);
        tree.remove(25);
        System.out.println(25);
        BTreeUtils.print(tree.root, true);
        tree.remove(50);
        System.out.println(50);
        BTreeUtils.print(tree.root, true);
        tree.remove(72);
        System.out.println(72);
        BTreeUtils.print(tree.root, true);
        tree.remove(33);
        System.out.println(33);
        BTreeUtils.print(tree.root, true);
        tree.remove(17);
        System.out.println(17);
        BTreeUtils.print(tree.root, true);
        tree.remove(46);
        System.out.println(46);
        BTreeUtils.print(tree.root, true);
    }
    
    @Test
    public void test_start_traverse() {

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
        }
        BTreeUtils.print(tree.root, true);

        tree.traverse(tree.root, AbsBSTTree.Order.MidOrder, e -> {
            System.out.println(e);
            return true;
        });
        System.out.println("条件打印");
        tree.traverse(tree.root, AbsBSTTree.Order.MidOrder, e -> {
            if (e.val < 10) {
                System.out.println(e);
                return true;
            }
            return false;
        });
        
    }

    /**
     * 自动测试 test
     */

    /**
     * 删除是否正常
     */
    @Test
    public void test_start2_remove_AutoTest() {

        Set<Integer> set = new HashSet<>(valueSize);

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            if (tree.add(v)) {
                set.add(v);
            }
        }
//        assert BTreeUtils.eq(set, tree);
//        System.out.println(BTreeUtils.print(tree.root, false));

        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            tree.remove(next);
            iterator.remove();

            if (!BTreeUtils.eq(set, tree)) {
                System.out.println("error=" + next);
                System.out.println(BTreeUtils.print(tree.root, false));
                System.out.println(Arrays.toString(set.toArray()));
                assert false;
            }
        }
    }

    @Test
    public void test_start2_floor_AutoTest() {

        List<Integer> list = new ArrayList<>(valueSize);

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
            list.add(v);
        }

        for (int i = 0; i < valueSize; i++) {
            int val = i;
            Integer floor = tree.floor(val);
            if (floor == null && list.stream().filter(e -> e <= val).max(Comparator.comparing(Integer::doubleValue)).orElse(null) == null) {
                continue;
            }
            // <= v 的 最大的那个
            if (floor == list.stream().filter(e -> e <= val).max(Comparator.comparing(Integer::doubleValue)).orElse(null)) {
                continue;
            }
            System.out.println("error " + i);
            BTreeUtils.print(tree.root, true);
            assert false;
        }
    }

    @Test
    public void test_start3_ceiling_AutoTest() {

        List<Integer> list = new ArrayList<>(valueSize);

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
            list.add(v);
        }

        for (int i = 0; i < valueSize; i++) {
            int val = i;
            Integer ceiling = tree.ceiling(val);
            if (ceiling == null && list.stream().filter(e -> e >= val).min(Comparator.comparing(Integer::doubleValue)).orElse(null) == null) {
                continue;
            }
            // >= v 的 最小的那个
            if (ceiling == list.stream().filter(e -> e >= val).min(Comparator.comparing(Integer::doubleValue)).orElse(null)) {
                continue;
            }
            System.out.println("error " + i);
            BTreeUtils.print(tree.root, true);
            assert false;
        }
    }

    @Test
    public void test_start3_min_AutoTest() {

        int expectV = maxValue;

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
            expectV = Math.min(v, expectV);
        }

        if (tree.min() != expectV) {
            System.out.println("error expectV=" + expectV);
            System.out.println("error min=" + tree.min());
            assert false;
        } else {
            System.out.println("OK");
        }
    }

    @Test
    public void test_start2_removeMin_AutoTest() {

        List<Integer> sort = new ArrayList<>(valueSize);

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            if (tree.add(v)) {
                sort.add(v);
            }
        }
        sort.sort(Comparator.comparing(Integer::doubleValue));
//        System.out.println(BTreePrinter.print(tree.root, false));

        System.out.println("min=" + sort.get(0));
        System.out.println("删除=" + tree.removeMin());

        if (tree.min() !=  sort.get(1)) {
            System.out.println("sort min=" + sort.get(1));
            System.out.println("tree min=" + tree.min());
            System.out.println(Arrays.toString(sort.toArray()));
            System.out.println(BTreeUtils.print(tree.root, false));
            assert false;
        } else {
            System.out.println("OK " + tree.min());
        }

    }

    @Test
    public void test_start3_max_AutoTest() {

        int expectV = -1;

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
            expectV = Math.max(v, expectV);
        }

        if (tree.max() != expectV) {
            System.out.println("error expectV=" + expectV);
            System.out.println("error max=" + tree.max());
            assert false;
        } else {
            System.out.println("OK");
        }
    }

    @Test
    public void test_start2_removeMax_AutoTest() {

        List<Integer> sort = new ArrayList<>(valueSize);

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            if (tree.add(v)) {
                sort.add(v);
            }
        }
        sort.sort(Comparator.comparing(Integer::doubleValue).reversed());
//        System.out.println(BTreePrinter.print(tree.root, false));

        System.out.println("max=" + sort.get(0));
        System.out.println("删除=" + tree.removeMax());

        if (tree.max() !=  sort.get(1)) {
            System.out.println("sort max=" + sort.get(1));
            System.out.println("tree max=" + tree.max());
            System.out.println(Arrays.toString(sort.toArray()));
            System.out.println(BTreeUtils.print(tree.root, false));
            assert false;
        } else {
            System.out.println("OK " + tree.min());
        }

    }
    
    @Test
    public void test_start3_rank_AutoTest() {

        List<Integer> list = new ArrayList<>(valueSize);

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            if (tree.add(v)) {
                list.add(v);
            }
        }
        BTreeUtils.print(tree.root, true);

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

        List<Integer> list = new ArrayList<>(valueSize);

        Tree02BST01<Integer> tree = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            if (tree.add(v)) {
                list.add(v);
            }
        }
        BTreeUtils.print(tree.root, true);

        for (int i = 1; i <= valueSize; i++) {
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
