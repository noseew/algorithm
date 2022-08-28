package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 121. 买卖股票的最佳时机
 * 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * <p>
 * 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
 * <p>
 * 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_121_maxProfit {

    @Test
    public void test() {
        System.out.println("5 = " + maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        System.out.println("0 = " + maxProfit(new int[]{7, 6, 4, 3, 1}));

    }

    /**
     * 思路, 一次遍历, 过程中, 找出最小值和利润最大值
     * 计算出, 第i天卖出时最大利润是多少, 遍历完成后, 也就得到了所有的天中, 最大利润
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) return 0;

        int min = prices[0]; // 最小值
        int profit = 0; // 最大利润
        for (int i = 1; i < prices.length; i++) {
            int price = prices[i];
            if (price > min) {
                profit = Math.max(profit, price - min);
            } else {
                min = price;
            }
        }
        return profit;
    }

    @Test
    public void test2() {
        System.out.println("5 = " + maxProfit2(new int[]{7, 1, 5, 3, 6, 4}));
        System.out.println("0 = " + maxProfit2(new int[]{7, 6, 4, 3, 1}));

    }

    /**
     * 思路2,
     * 动态规划
     * 第i天卖的最大利润就是, 从i开始往前每天的差值之和, 取最大的话就转化成了, 最大连续子序列和的问题, 可以使用动态规划
     * 用动态规划比第一种更复杂, 这里只是熟悉动态规划
     */
    public int maxProfit2(int[] prices) {
        if (prices == null || prices.length == 0) return 0;

        int[] maxProfit = new int[prices.length];

        int profit = 0; // 最大利润
        for (int i = 1; i < prices.length; i++) {
            // 初值为第i天和前一天的差值, 动态规划就是计算第i天截止, 求这些最大连续差值的和
            maxProfit[i] = prices[i] - prices[i - 1];
            // 转换成 第i天截止, 求这些最大连续差值的和
            maxProfit[i] = Math.max(maxProfit[i], maxProfit[i] + maxProfit[i - 1]);

            profit = Math.max(profit, maxProfit[i]);

        }
        return profit;
    }

}
