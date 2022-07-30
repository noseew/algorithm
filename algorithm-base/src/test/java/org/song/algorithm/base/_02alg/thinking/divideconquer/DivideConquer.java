package org.song.algorithm.base._02alg.thinking.divideconquer;

import org.junit.jupiter.api.Test;

/**
 * 最大连续子序列和
 */
public class DivideConquer {

    @Test
    public void test( ) {
        int[] nums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println(maxSubArray(nums));
    }

    /**
     * 暴力 O(n^3)
     * 穷举所有可能, 取出最大的
     */
    int maxSubArray1(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        // 这里注意, 容易写成 int max = 0, 可能会出错, max 默认值必须是最小的值
        int max = Integer.MIN_VALUE;
        // 穷举, 列出所有可能的连续子序列, 分别计算它们的和, 最后取出最大值
        for (int begin = 0; begin < nums.length; begin++) {
            for (int end = begin; end < nums.length; end++) {
                int sum = 0; // sum是[begin, end]的和
                // nums[begin] 到 nums[ end]求和
                for (int i = begin; i <= end; i++) {
                    sum += nums[i];
                }
                max = Math.max(max, sum); // 取最大值
            }
        }
        return max;
    }

    /**
     * 暴力 - 优化
     *  O(n^2)
     */
    int maxSubArray2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        // 这里注意, 容易写成 int max = 0, 可能会出错, max 默认值必须是最小的值
        int max = Integer.MIN_VALUE;
        // 穷举, 列出所有可能的连续子序列, 分别计算它们的和, 最后取出最大值
        for (int begin = 0; begin < nums.length; begin++) {
            // 重复利用sum, 只有当begin修改才会重置
            int sum = 0;
            // begin不动, end修改的话, 子序列的和是叠加的, 无需每次都重新计算
            for (int end = begin; end < nums.length; end++) {
                sum += nums[end]; // sum是[begin, end]的和
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    /**
     * 分治
     * 
     * 思路:
     * 假设问题的解释 S[i,j) 左闭右开, 那么问题解有3中可能
     * 1. 在左边,
     * 2. 在右边
     * 3. 左右都有, 左边(沿着中间右边开始)最大子序列和 + 右边(沿着中间左边开始)最大子序列和, (必须包含中间的mid)
     * 然后将这3种和求出并取最大值
     */
    int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return maxSubArray(nums, 0, nums.length);
    }

    int maxSubArray(int[] nums, int begin, int end) {
        // 递归基:
        // end - begin < 2, 说明只有一个元素, nums[begin] == nums[end]
        if (end - begin < 2) return nums[begin];
        int mid = (begin + end) >> 1;
        // 半边最大子序列和(必须包含中间的mid)
        // 最长子序列是[i, mid)+ [mid, j)的情况
        int leftMax = Integer.MIN_VALUE;
        int leftSum = 0;
        for (int i = mid - 1; i >= begin; i--) { // [i,mid)
            leftSum += nums[i];
            leftMax = Math.max(leftSum, leftMax);
        }
        // 右半边最大子序列和(必须包含中间的mid)
        int rightMax = Integer.MIN_VALUE;
        int rightSum = 0;
        for (int i = mid; i < end; i++) { // [mid, end)
            rightSum += nums[i];
            rightMax = Math.max(rightSum, rightMax);
        }
        // 情况 左右取最大, 然后和mid取最大
        // 最长子序列在 left部分, right部分的情况
        return Math.max(leftMax + rightMax,
                // 情况1和情况2中取一个最大
                Math.max(
                        maxSubArray(nums, begin, mid),    // 最长子串在[begin, mid)的情况 
                        maxSubArray(nums, mid, end)    // 最长子串在[mid, end)的情况
                ));
    }
}
