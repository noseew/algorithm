package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model_lab.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model_lab.Tree03_AVL_Ratio1;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model_lab.Tree03_AVL_Ratio2;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model_lab.Tree03_AVL_RatioTimes;

import java.util.Comparator;
import java.util.Random;

public class AVL_test {

    /**
     * 验证 不同平衡因子对调整次数的影响
     */
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

    /**
     * 验证 新增节点和删除节点的调整次数
     */
    @Test
    public void test_03_start() {
        Tree03_AVL_RatioTimes<Integer> tree1 = new Tree03_AVL_RatioTimes<>(Comparator.comparing(Integer::doubleValue));

        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int val = random.nextInt(100);
            if (tree1.push(val)) {
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
