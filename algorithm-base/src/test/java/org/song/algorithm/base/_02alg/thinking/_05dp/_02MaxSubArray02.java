package org.song.algorithm.base._02alg.thinking._05dp;

import org.junit.jupiter.api.Test;

/**
 * 最大连续子序列和
 * 采用动态规划实现
 */
public class _02MaxSubArray02 {

    @Test
    public void test01() {
        int[] ints = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(ms1(ints));

    }

    /**
     * 采用动态规划
     */
    private int ms1(int[] arr) {
        // dp[i]表示, 当截止到前位置的的最大连续子序列和, 
        // 所以第一个很好算, 就是当前数据本身, 同时在没有计算的时候, 每个dp[i]都等于它本身
        int[] dp = new int[arr.length];
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            dp[i] = arr[i];
            /*
            第i个数的最大连续子序列和就是
            当前位置, 和前一个位置值(前一个的最大连续子序列和) 取最大
            dp[i] 和 dp[i] + dp[i-1] 取最大
             */
            if (i > 0) dp[i] = Math.max(dp[i], dp[i] + dp[i - 1]);
            max = Math.max(dp[i], max);
        }
        return max;
    }
}
