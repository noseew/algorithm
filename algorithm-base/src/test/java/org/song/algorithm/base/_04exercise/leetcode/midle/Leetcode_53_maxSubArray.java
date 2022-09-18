package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

/**
 * 53. 最大子数组和
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * <p>
 * 子数组 是数组中的一个连续部分。
 */
public class Leetcode_53_maxSubArray {


    @Test
    public void test() {
        System.out.println(maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    /**
     * 动态规划
     */
    public int maxSubArray(int[] nums) {
        int[] dp = new int[nums.length];
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            dp[i] = nums[i];
            if (i > 0) dp[i] = Math.max(dp[i], dp[i] + dp[i - 1]);
            max = Math.max(dp[i], max);
        }
        return max;
    }
}
