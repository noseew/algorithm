package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.Test;

/**
 * 53. 最大子序和
 *
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 *  
 *
 * 示例 1：
 *
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 * 示例 2：
 *
 * 输入：nums = [1]
 * 输出：1
 * 示例 3：
 *
 * 输入：nums = [0]
 * 输出：0
 * 示例 4：
 *
 * 输入：nums = [-1]
 * 输出：-1
 *示例 5：
 *
 * 输入：nums = [-100000]
 * 输出：-100000
 *  
 *
 * 提示：
 *
 * 1 <= nums.length <= 3 * 104
 * -105 <= nums[i] <= 105
 *  
 *
 * 进阶：如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的 分治法 求解。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-subarray
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_53_MaxSubArray {

    @Test
    public void test() {
        System.out.println(maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4})); // 6
        System.out.println(maxSubArray2(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
    }

    /**
     * 实现复杂度太高, 执行超时
     *
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        int startIndex = 0;
        int maxWindow = 0;
        int maxCount = Integer.MIN_VALUE;
        int countTemp = 0;
        // 窗口大小从1到最大开始循环
        for (int window = 1; window <= nums.length; window++) {
            // 窗口向后移动
            for (int startWindow = 0; startWindow <= nums.length - window; startWindow++) {
                for (int i = startWindow; i < window + startWindow; i++) {
                    // 窗口内数据求和
                    countTemp += nums[i];
                }
                if (countTemp > maxCount) {
                    maxCount = countTemp;
                    maxWindow = window;
                    startIndex = startWindow;
                }
                countTemp = 0;
            }
        }
        System.out.println(String.format("最大 %s, startIndex %s, startWindow %s", maxCount, startIndex, maxWindow));
        return maxCount;
    }

    /**
     * 官方题解, 动态规划
     * 
     * @param nums
     * @return
     */
    public int maxSubArray2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        int pre = 0;
        int current = 0;
        for (int num : nums) {
            current += num;
            pre = Math.max(pre, current);
        }
        return current;
    }

}
