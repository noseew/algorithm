package org.song.algorithm.base._02alg.thinking._05dp;

import org.junit.jupiter.api.Test;

/**
 * 最长上升子序列(不连续)的长度
 */
public class _03LIS02 {

    @Test
    public void main() {
        int[] ints = {10, 2, 2, 5, 1, 7, 101, 18};
        System.out.println(lis01(ints));
    }
    
    private int lis01(int[] ints) {
        /*
        dp[i] 表示截止到第i个位置的元素有多少个最长上升子序列
         */
        int[] dp = new int[ints.length];
        for (int i = 0; i < ints.length; i++) {
            boolean hasPre = false;
            int max = dp[i] = 1;
            // 把历史所有都对比一边, 因为要找到上升的, 所以要找比当前小的
            for (int j = 0; j < i; j++) {
                if (ints[j] < ints[i]) {
                    max = Math.max(max, dp[j]);
                    hasPre = true;
                }
            }
            if (hasPre) {
                // 有比当前小的, 则累加
                dp[i] = max + dp[i];
            }
        }
        return dp[ints.length - 1];
    }

}
