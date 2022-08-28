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

}
