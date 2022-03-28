package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.AbsBSTTree;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree02BST01;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03AVL01;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03AVL02;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer.BTreeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class AVL_test {

    private final int maxValue = 50;
    private final int valueSize = 20;

    @Test
    public void test_Tree03AVL01_start() {
        Tree03AVL01<Integer> tree = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));
//        Tree03AVL02<Integer> tree = new Tree03AVL02<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            tree.add(random.nextInt(100));
        }
        System.out.println(tree.toString());
        System.out.println("********************");
        System.out.println(BTreeUtils.print(tree.root, false));
        System.out.println("********************");
        System.out.println(BTreeUtils.simplePrint(tree.root, false));
    }

    @Test
    public void test_remove() {
        Tree03AVL01<Integer> tree = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));

        for (int i = 0; i < 20; i++) {
            tree.add(i);
        }
        System.out.println(tree.toString());
        for (int i = 0; i < 20; i++) {
            tree.remove(i);
            System.out.println(tree.toString());
        }
    }
    
    @Test
    public void test_remove2() {
//        Tree03AVL01<Integer> tree = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));
        Tree03AVL02<Integer> tree = new Tree03AVL02<>(Comparator.comparing(Integer::doubleValue));
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

    /**
     * 自动测试
     */

    @Test
    public void test_diff_BST_add() {
        // avl 和 bst 是否是同一棵树
        Tree03AVL01<Integer> avl = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));
//        Tree02BST01<Integer> bst = new Tree02BST01<>(Comparator.comparing(Integer::doubleValue));
        Tree03AVL02<Integer> avl2 = new Tree03AVL02<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            avl.add(v);
//            bst.add(v);
            avl2.add(v);
        }

//        if (!BTreeUtils.eq(avl, bst)) {
//            System.out.println(BTreeUtils.print(avl.root, false));
//            System.out.println(BTreeUtils.print(bst.root, false));
//            assert false;
//        }

        if (!BTreeUtils.eq(avl, avl2)) {
            System.out.println(BTreeUtils.print(avl.root, false));
            System.out.println(BTreeUtils.print(avl2.root, false));
            assert false;
        }
    }

    @Test
    public void test_isBalance() {
//        Tree03AVL01<Integer> avl = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));
        Tree03AVL02<Integer> avl = new Tree03AVL02<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            avl.add(v);
        }

        AtomicBoolean balance = new AtomicBoolean(true);

        Tree02BST01.traverse(avl.root, AbsBSTTree.Order.MidOrder, 
                e -> {
                    boolean goon = Math.abs(Math.abs(Tree02BST01.getHeight_recursive(e.left))
                            - Math.abs(Tree02BST01.getHeight_recursive(e.right))) <= 1;
                    if (!goon) {
                        // 不平衡
                        balance.set(false);
                        return false;
                    }
                    if (!balance.get()) {
                        return false;
                    }
                    balance.set(true);
                    return true;
                });

        assert balance.get();
        
    }

    /**
     * 删除是否正常
     */
    @Test
    public void test_start2_remove_AutoTest() {

        Set<Integer> set = new HashSet<>(valueSize);

        Tree03AVL01<Integer> tree = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));
        Random random = new Random();
        for (int i = 0; i < valueSize; i++) {
            int v = random.nextInt(maxValue);
            if (tree.add(v)) {
                set.add(v);
            }
        }
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
}
