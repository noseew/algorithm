package org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._03modelLab.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._01model.Tree03AVL01;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._03modelLab.Tree03AVLRatio1;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._03modelLab.Tree03AVLRatio2;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.binarytree._03modelLab.Tree03AVLRatioTimes;

import java.util.Comparator;
import java.util.Random;

public class AVL_test {

    /**
     * 验证 不同平衡因子对调整次数的影响
     */
    @Test
    public void test_02_start() {
        Tree03AVL01<Integer> tree1 = new Tree03AVL01<>(Comparator.comparing(Integer::doubleValue));
        Tree03AVLRatio1<Integer> tree2 = new Tree03AVLRatio1<>(Comparator.comparing(Integer::doubleValue));
        Tree03AVLRatio2<Integer> tree3 = new Tree03AVLRatio2<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int val = random.nextInt(100);
            tree1.add(val);
            tree2.add(val);
            tree3.add(val);
        }
        System.out.println(tree1.toString());
        System.out.println();
        System.out.println(tree2.toString());
        System.out.println();
        System.out.println(tree3.toString());
    }

    /**
     * 验证 新增节点和删除节点的调整次数
     */
    @Test
    public void test_03_start() {
        Tree03AVLRatioTimes<Integer> tree1 = new Tree03AVLRatioTimes<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int val = random.nextInt(100);
            if (tree1.add(val)) {
                // 测试每次操作的旋转次数
                System.out.println(tree1.toString());
                tree1.resetRotateTimes();
            }
        }
        System.out.println("删除");
        for (int i = 0; i < 100; i++) {
            if (tree1.remove(i) != null) {
                // 测试每次操作的旋转次数
                System.out.println(tree1.toString());
                tree1.resetRotateTimes();
            }
        }
    }
}
