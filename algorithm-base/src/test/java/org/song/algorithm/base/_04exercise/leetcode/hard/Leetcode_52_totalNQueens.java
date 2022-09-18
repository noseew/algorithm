package org.song.algorithm.base._04exercise.leetcode.hard;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 51. N 皇后
 * 按照国际象棋的规则，皇后可以攻击与之处在同一行或同一列或同一斜线上的棋子。
 * <p>
 * n 皇后问题 研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * <p>
 * 给你一个整数 n ，返回所有不同的 n 皇后问题 的解决方案。
 * <p>
 * 每一种解法包含一个不同的 n 皇后问题 的棋子放置方案，该方案中 'Q' 和 '.' 分别代表了皇后和空位。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/n-queens
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_52_totalNQueens {

    @Test
    public void test() {
        System.out.println(totalNQueens(4));
    }

    int[] queen;
    int way;

    public int totalNQueens(int n) {
        if (n < 1) return 0;
        queen = new int[n];
        // 从第0行开始摆
        start(0);
        return way;
    }

    /**
     * 采用回溯法,
     * 回溯采用递归方式
     */
    void start(int row) {
        if (row == queen.length) {
            way++;
            return;
        }
        for (int col = 0; col < this.queen.length; col++) {
            boolean valid = valid(row, col);
            if (valid) {
                // 放置皇后
                this.queen[row] = col;
                // 下一行
                start(row + 1);
                // 递归就是回溯
            }
        }
    }

    /**
     * 该位置是否可以放置皇后
     * row 行
     * col 列
     */
    boolean valid(int row, int col) {
        for (int r = 0; r < row; r++) {
            if (queen[r] == col) {
                // 该列已经有皇后了
                return false;
            }
            if (Math.abs(r - row) == Math.abs(col - queen[r])) {
                // 在同一个对角线
                return false;
            }
        }
        return true;
    }
}
