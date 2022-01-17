package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 219. 存在重复元素 II
 * <p>
 * 给定一个整数数组和一个整数 k，判断数组中是否存在两个不同的索引 i 和 j，使得 nums [i] = nums [j]，并且 i 和 j 的差的 绝对值 至多为 k。
 */
public class Leetcode_219_containsNearbyDuplicate {

    @Test
    public void test() {
        System.out.println(containsNearbyDuplicate2(new int[]{1, 2, 3, 1}, 3));
        System.out.println(containsNearbyDuplicate2(new int[]{1, 0, 1, 1}, 1));
        System.out.println(containsNearbyDuplicate2(new int[]{1, 2, 3, 1, 2, 3}, 2));
    }

    /**
     * 执行超时
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        for (int i = 0; i < nums.length; i += ((k * 2) - 1)) {
            if (getJ(i, k, nums)) {
                return true;
            }
        }
        return false;
    }

    public boolean getJ(int i, int k, int[] nums) {
        for (int j = Math.max(i - k, 0); j < nums.length; j++) {
            if (j <= i + k
                    && j >= i - k
                    && j != i
                    && nums[i] == nums[j]) {
//                System.out.println("i=" + i);
//                System.out.println("j=" + j);
//                System.out.println("k=" + k);
//                System.out.println(Arrays.toString(nums));
                return true;
            }
        }
        return false;
    }

    /**
     * 仍然超时
     */
    public boolean containsNearbyDuplicate2(int[] nums, int k) {
        for (int i = 0; i < nums.length; i += k) {
            if (getJ2(i, k, nums)) {
                return true;
            }
        }
        return false;
    }

    public boolean getJ2(int i, int k, int[] nums) {
        for (int j = i + 1; j < nums.length && j <= i + k; j++) {
            if (j >= i - k
                    && j != i
                    && nums[i] == nums[j]) {
//                System.out.println("i=" + i);
//                System.out.println("j=" + j);
//                System.out.println("k=" + k);
//                System.out.println(Arrays.toString(nums));
                return true;
            }
        }
        return false;
    }
}
