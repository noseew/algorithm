package org.song.algorithm.base._02alg.alg.recursion;

import org.junit.jupiter.api.Test;
/*
N 皇后问题
一个N*N的棋盘中, 需要摆下N个皇后, 请问总共有几种摆法
要求
1. 皇后相互之间不能攻击对方
2. 皇后的攻击方式是, 米子线
 */
public class _04NQueen {
    
    @Test
    public void test01() {
        NQueen01 nQueen01 = new NQueen01();
        nQueen01.place(4);
    }
    
    
    public static class NQueen01 {
        /**
         * 下标表示 行
         * 值表示 列
         * 当有值的时候, 表示该位置放置了皇后
         */
        int[] queen;
        int way;

        void place(int n) {
            if (n < 1) return;
            queen = new int[n];
            // 从第0行开始摆
            start(0);
        }

        /**
         * 采用回溯法, 
         * 回溯采用递归方式
         */
        void start(int row) {
            if (row == queen.length) {
                way++;
                print();
                return;
            }
            for (int col = 0; col < this.queen.length; col++) {
                boolean valid = valid(row, col);
                System.out.println(String.format("行%s, 列%s %s", row, col, valid));
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

        public void print() {
            for (int row = 0; row < queen.length; row++) {
                for (int col = 0; col < queen.length; col++) {
                    if (queen[row] == col) {
                        System.out.print("1 ");
                    } else {
                        System.out.print("0 ");
                    } 
                }
                System.out.println();
            }
            System.out.println("共有" + way + "种方式");
        }
    }
}
