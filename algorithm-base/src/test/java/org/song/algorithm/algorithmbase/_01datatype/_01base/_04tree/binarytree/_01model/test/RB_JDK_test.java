package org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._01model.Tree05RBJDKHotspot;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._01model.Tree05RBJDKTreemap;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree.printer.BTreeUtils;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._01model.Tree05RBJDKHashmap;

import java.util.*;

public class RB_JDK_test {

    private final int maxValue = 50;
    private final int valueSize = 15;

    @Test
    public void test_hashmap() {

        Tree05RBJDKHashmap<Integer> tree = new Tree05RBJDKHashmap<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
        }
        BTreeUtils.print(tree.root, true);
    }

    @Test
    public void test_treemap() {

        Tree05RBJDKTreemap<Integer> tree = new Tree05RBJDKTreemap<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
        }
        BTreeUtils.print(tree.root, true);
    }

    @Test
    public void test_hotspot() {

        Tree05RBJDKHotspot<Integer> tree = new Tree05RBJDKHotspot<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            tree.add(v);
        }
        BTreeUtils.print(tree.root, true);
        BTreeUtils.printJDK9(tree.root);
    }

    @Test
    public void test_vs_add() {

        Tree05RBJDKTreemap<Integer> treemap = new Tree05RBJDKTreemap<>(Comparator.comparing(Integer::doubleValue));
        Tree05RBJDKHashmap<Integer> hashmap = new Tree05RBJDKHashmap<>(Comparator.comparing(Integer::doubleValue));
        Tree05RBJDKHotspot<Integer> hotspot = new Tree05RBJDKHotspot<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            treemap.add(v);
            hashmap.add(v);
            hotspot.add(v);
        }
        BTreeUtils.print(treemap.root, true);
        BTreeUtils.print(hashmap.root, true);
        BTreeUtils.print(hotspot.root, true);

        System.out.println("(treemap, hashmap)");
        assert BTreeUtils.eq(treemap, hashmap);
//        System.out.println("(hashmap, hotspot)");
//        assert BTreeUtils.eq(hashmap, hotspot); // hotspot 有问题
    }

    @Test
    public void test_vs_remove() {

        Set<Integer> set = new HashSet<>(valueSize);

        Tree05RBJDKTreemap<Integer> treemap = new Tree05RBJDKTreemap<>(Comparator.comparing(Integer::doubleValue));
        Tree05RBJDKHashmap<Integer> hashmap = new Tree05RBJDKHashmap<>(Comparator.comparing(Integer::doubleValue));
        Tree05RBJDKHotspot<Integer> hotspot = new Tree05RBJDKHotspot<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            treemap.add(v);
            if (hashmap.add(v)) {
                set.add(v);
            }
            hotspot.add(v);
        }
//        BTreeUtils.print(treemap.root, true);
//        BTreeUtils.print(hashmap.root, true);
//        BTreeUtils.print(hotspot.root, true);
        
        Iterator<Integer> iterator = set.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            iterator.remove();
            
            treemap.remove(next);
            if (!BTreeUtils.eq(set, treemap)) {
                System.out.println("hotspot error=" + next);
                System.out.println(BTreeUtils.print(treemap.root, false));
                System.out.println(Arrays.toString(set.toArray()));
                assert false;
            }

            hashmap.remove(next);
            if (!BTreeUtils.eq(set, hashmap)) {
                System.out.println("hashmap error=" + next);
                System.out.println(BTreeUtils.print(hashmap.root, false));
                System.out.println(Arrays.toString(set.toArray()));
                assert false;
            }

//            hotspot.del(next); // hotspot 有问题
//            if (!BTreeUtils.eq(set, hotspot)) {
//                System.out.println("hotspot error=" + next);
//                System.out.println(BTreeUtils.print(hotspot.root, false));
//                System.out.println(Arrays.toString(set.toArray()));
//                assert false;
//            }
        }
    }
    
}
