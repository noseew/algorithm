package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 59. 螺旋矩阵 II
 * 给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix 。
 */
public class Leetcode_54_spiralOrder {


    @Test
    public void test() {
//        List<Integer> ints = spiralOrder(new int[][]{
//                {1, 2, 3}, 
//                {4, 5, 6}, 
//                {7, 8, 9}
//        });
        List<Integer> ints = spiralOrder(new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        });
        for (Integer anInt : ints) {
            System.out.println(anInt);
        }
    }

    /*
    [
                    rightTop
                       \|/
    leftTop ->    [1,2,3],
                  [8,9,4],
    leftButton -> [7,6,5]
                  /|\
                rightButton
    ]
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> res = new ArrayList<>();

        int leftTop = 0, leftButton = matrix.length - 1,
                rightTop = matrix[0].length - 1, rightButton = 0;

        while (leftButton >= leftTop && rightTop >= rightButton) {

            // 从左往右 -> 
            for (int i = rightButton; i <= rightTop; i++) {
                res.add(matrix[leftTop][i]);
            }
            leftTop++;

            // 从上到下 \|/
            for (int i = leftTop; i <= leftButton; i++) {
                res.add(matrix[i][rightTop]);
            }
            rightTop--;

            // 过程中中断, 奇数行, 偶数列
            if (leftButton < leftTop || rightTop < rightButton) break;

            // 从右到左
            for (int i = rightTop; i >= rightButton; i--) {
                res.add(matrix[leftButton][i]);
            }
            leftButton--;

            // 从下到上
            for (int i = leftButton; i >= leftTop; i--) {
                res.add(matrix[i][rightButton]);
            }
            rightButton++;
        }
        return res;
    }
}
