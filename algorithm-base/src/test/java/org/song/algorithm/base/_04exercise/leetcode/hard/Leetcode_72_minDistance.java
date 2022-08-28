package org.song.algorithm.base._04exercise.leetcode.hard;

import org.junit.jupiter.api.Test;

import java.util.Stack;

/**
 * 72. 编辑距离
 * 给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数  。
 *
 * 你可以对一个单词进行如下三种操作：
 *
 * 插入一个字符
 * 删除一个字符
 * 替换一个字符
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/edit-distance
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_72_minDistance {

    @Test
    public void test() {
        System.out.println(minDistance("horse", "ros"));
        System.out.println(minDistance("intention", "execution"));
        System.out.println(minDistance("", "a"));
    }

    /**
     * 思路, 采用动态规划
     * 具体的dp图, 参见本目录同名图片
     * 思路流程
     * 1. 一个字符转换成空串, 距离是1(新增一个字符), 两个字符转换成空串, 距离是2(新增两个字符)
     * 2. 一个字符转换成另一个字符, 距离是1(替换一个字符)
     * 3. 新增一个字符转换为另一个新增一个字符, 要对比他们是否相同, 如果相同则距离不变, 否则距离加一(替换一次)
     * 上面三种情况近似等价于, 判断dp二维图表中的, 左/上/左上, 然后三个对比, 取最小距离的那个
     * 
     */
    public int minDistance(String word1, String word2) {
        if (word1 == null || word2 == null) return 0;

        char[] rowArray = word1.toCharArray();
        char[] colArray = word2.toCharArray();
        /*
        dp[row][col] 表示, rowArray[0, row] 到 colArray[0, col] 的编辑距离
        这里的长度+1, 表示包含0, 0的意思是空字符串
         */
        int[][] dp = new int[rowArray.length + 1][colArray.length + 1];
        /*
        初始化第0行, 第0列
         */
        for (int row = 1; row <= rowArray.length; row++) {
            dp[row][0] = row;
        }
        for (int col = 1; col <= colArray.length; col++) {
            dp[0][col] = col;
        }

        for (int row = 1; row <= rowArray.length; row++) {
            for (int col = 1; col <= colArray.length; col++) {
                // 上一个 最短距离
                int top = 1 + dp[row - 1][col];
                // 左边 最短距离
                int left = 1 + dp[row][col - 1];
                // 左上角 最短距离
                int leftTop = rowArray[row - 1] != colArray[col - 1] ? 1 + dp[row - 1][col - 1] : dp[row - 1][col - 1];
                // 取最小
                dp[row][col] = Math.min(leftTop, Math.min(top, left));
            }
        }
        return dp[rowArray.length][ colArray.length];
    }
}
