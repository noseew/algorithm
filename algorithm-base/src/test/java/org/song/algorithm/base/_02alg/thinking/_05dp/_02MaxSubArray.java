package org.song.algorithm.base._02alg.thinking._05dp;

import org.junit.jupiter.api.Test;

/**
 * 最大连续子序列
 * 采用动态规划实现
 */
public class _02MaxSubArray {
    
    @Test
    public void main( ) {
        System.out.println(maxSubArray1(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        System.out.println(maxSubArray2(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    /*
    1. 定义状态
        dp[i]表示第i下标结尾的, 最大连续子串和
    2. 定义边界
        dp[0]=nums[0], 第一位的最大子串和一定是它本身
    3. 状态转移方程
        则 dp[i] = max(dp[i], dp[i-1] + dp[i])
        遍历i=0到i=数组长度, 从第0个开始计算, 当前下标结尾的最长子串和, 一定是 当前位置值本身或者当前位置值本身加上前一位的最长子串和
        
     */
    int maxSubArray1(int[] nums) {
        if (nums == null || nums.length == 0) return 0; // 非法数据检测
        int[] dp = new int[nums.length];
        int max = dp[0] = nums[0]; // 初始状态
        // 状态转移方程, i从1开始, 因为0已经确定了
        for (int i = 1; i < dp.length; i++) {
            if (dp[i - 1] > 0) {
                // 只有前一个>0, 加上它才有意义
                dp[i] = dp[i - 1] + nums[i];
            } else {
                // 否则最长子串和就是它本身
                dp[i] = nums[i];
            }
            // 在历史中获取一个最大值
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 优化dp数组, 由于获取dp[i], 只需要dp[i-1], 在此之前的dp[i-2]在后续的计算中都不需要的, 
     * 所以, 只需要保留 dp[i-1] 的值即可, 因此dp数组也不需要了
     */
    int maxSubArray2(int[] nums) {
        if (nums == null || nums.length == 0) return 0; // 非法数据监测
        int dp = nums[0]; // 初始状态
        int max = dp;
        // 状态转移方程
        for (int i = 1; i < nums.length; i++) {
            if (dp > 0) {
                dp = dp + nums[i];
            } else {
                dp = nums[i];
            }
            max = Math.max(max, dp);
        }
        return max;
    }
}
