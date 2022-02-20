package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_jdkhashmap;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree05_RB_jdktreemap;

import java.util.*;

public class RB_test {

    private final int maxValue = 50;
    private final int valueSize = 15;

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
//        Tree05_RB_jdkhashmap<Integer> tree = new Tree05_RB_jdkhashmap<>(Comparator.comparing(Integer::doubleValue));
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
    public void test_vs2() {

        Set<Integer> set = new HashSet<>(valueSize);

        Tree05_RB_jdktreemap<Integer> treemap = new Tree05_RB_jdktreemap<>(Comparator.comparing(Integer::doubleValue));
        Tree05_RB_base<Integer> rb = new Tree05_RB_base<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            treemap.put(v);
            if (rb.add(v)) {
                set.add(v);
            }
        }
        System.out.println("(treemap, rb)");
        assert BTreeUtils.eq(treemap, rb);

        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            iterator.remove();

            treemap.remove(next);
            rb.remove(next);
            if (!BTreeUtils.eq(rb, treemap)) {
                System.out.println(BTreeUtils.print(treemap.root, false));
                System.out.println(BTreeUtils.print(rb.root, false));
                System.out.println(Arrays.toString(set.toArray()));
                assert false;
            }

        }
    }
}
