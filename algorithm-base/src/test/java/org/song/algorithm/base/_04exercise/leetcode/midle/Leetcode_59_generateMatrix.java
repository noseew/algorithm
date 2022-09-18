package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

import java.util.Arrays;

/**
 * 59. 螺旋矩阵 II
 * 给你一个正整数 n ，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的 n x n 正方形矩阵 matrix 。
 */
public class Leetcode_59_generateMatrix {


    @Test
    public void test() {
        int[][] ints = generateMatrix(1);
        for (int[] anInt : ints) {
            System.out.println(Arrays.toString(anInt));
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
    public int[][] generateMatrix(int n) {
        int[][] res = new int[n][n];

        int leftTop = 0, leftButton = n - 1, 
                rightTop = n - 1, rightButton = 0;
        int m = 1;

        while (leftButton >= leftTop && rightTop >= rightButton) {
            
            // 从左往右 -> 
            for (int i = rightButton; i <= rightTop; i++) {
                res[leftTop][i] = m++;
            }
            leftTop++;
            
            // 从上到下 \|/
            for (int i = leftTop; i <= leftButton; i++) {
                res[i][rightTop] = m++;
            }
            rightTop--;
            
            // 从右到左
            for (int i = rightTop; i >= rightButton; i--) {
                res[leftButton][i] = m++;
            }
            leftButton--;

            // 从下到上
            for (int i = leftButton; i >= leftTop; i--) {
                res[i][rightButton] = m++;
            }
            rightButton++;
        }
        return res;
    }
}
