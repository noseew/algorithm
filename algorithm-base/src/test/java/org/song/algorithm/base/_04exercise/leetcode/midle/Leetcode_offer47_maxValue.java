package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

/**
 * 剑指 Offer 47. 礼物的最大价值
 * 在一个 m*n 的棋盘的每一格都放有一个礼物，每个礼物都有一定的价值（价值大于 0）。你可以从棋盘的左上角开始拿格子里的礼物，并每次向右或者向下移动一格、直到到达棋盘的右下角。给定一个棋盘及其上面的礼物的价值，请计算你最多能拿到多少价值的礼物？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/li-wu-de-zui-da-jie-zhi-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_offer47_maxValue {

    @Test
    public void test() {
        int[][] ints = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };
        System.out.println(maxValue(ints)); // 12
        int[][] ints2 = {
                {1, 2},
                {5, 6},
                {1, 1}
        };
        System.out.println(maxValue(ints2)); // 13
    }

    /**
     * 典型的动态规划思想解题
     */
    public int maxValue(int[][] grid) {
        int row = grid[0].length; // 行
        int col = grid.length; // 列

        // 从左上角到每个格子的最大价值
        int[][] values = new int[col][row];
        // 初始化第一个格子
        values[0][0] = grid[0][0];

        // 初始化 第一行
        for (int i = 1; i < row; i++) {
            values[0][i] = values[0][i - 1] + grid[0][i];
        }
        // 初始化第一列
        for (int i = 1; i < col; i++) {
            values[i][0] = values[i - 1][0] + grid[i][0];
        }

        for (int c = 1; c < col; c++) {
            for (int r = 1; r < row; r++) {
                int val = grid[c][r];
                // 到当前格子的最大价值 = 当前格子价值 + max(左边最大价值, 上边最大价值)
                values[c][r] = val + Math.max(values[c][r - 1], values[c - 1][r]);
            }
        }
        return values[col - 1][row - 1];
    }

    @Test
    public void test2() {
        int[][] ints = {
                {1, 3, 1},
                {1, 5, 1},
                {4, 2, 1}
        };
        System.out.println(maxValue2(ints)); // 12
        int[][] ints2 = {
                {1, 2},
                {5, 6},
                {1, 1}
        };
        System.out.println(maxValue2(ints2)); // 13
    }

    /**
     * 空间优化版
     */
    public int maxValue2(int[][] grid) {
        int row = grid[0].length; // 行
        int col = grid.length; // 列

        // 第一行的每个格子最大价值
        int[] values = new int[row];
        // 初始化第一个格子
        values[0] = grid[0][0];

        // 初始化 第一行
        for (int i = 1; i < row; i++) {
            values[i] = values[i - 1] + grid[0][i];
        }

        for (int c = 1; c < col; c++) {
            // 初始化第一列
            values[0] = values[0] + grid[c][0];
            for (int r = 1; r < row; r++) {
                int val = grid[c][r];
                // 到当前格子的最大价值 = 当前格子价值 + max(左边最大价值, 上边最大价值)
                values[r] = val + Math.max(values[r - 1], values[r]);
            }
        }
        return values[row - 1];
    }

}
