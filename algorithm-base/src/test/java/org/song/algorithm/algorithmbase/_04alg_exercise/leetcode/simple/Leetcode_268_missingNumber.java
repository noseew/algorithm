package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 268. 丢失的数字
 * 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
 * <p>
 * 进阶：
 * <p>
 * 你能否实现线性时间复杂度、仅使用额外常数空间的算法解决此问题?
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/missing-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_268_missingNumber {

    @Test
    public void test() {
        System.out.println("2 = " + missingNumber(new int[]{3, 0, 1}));
        System.out.println("2 = " + missingNumber(new int[]{0, 1}));
        System.out.println("1 = " + missingNumber(new int[]{0}));
        System.out.println("8 = " + missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));

    }

    /**
     * 思路
     * 遍历并逐个相加
     * 预期之和 - 实际之和 = 缺少的数
     *
     */
    public int missingNumber(int[] nums) {
        int sum = 0;
        int expectSum = 0;
        for (int i = 0; i <= nums.length; i++) {
            if (i < nums.length) {
                sum += nums[i];
            }
            expectSum += i;
        }
        return expectSum - sum;
    }


}
