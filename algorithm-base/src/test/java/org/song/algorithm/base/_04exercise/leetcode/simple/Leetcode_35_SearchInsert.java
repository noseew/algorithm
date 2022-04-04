package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;

/**
 * 35. 搜索插入位置
 * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
 * <p>
 * 你可以假设数组中无重复元素。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [1,3,5,6], 5
 * 输出: 2
 * 示例 2:
 * <p>
 * 输入: [1,3,5,6], 2
 * 输出: 1
 * 示例 3:
 * <p>
 * 输入: [1,3,5,6], 7
 * 输出: 4
 * 示例 4:
 * <p>
 * 输入: [1,3,5,6], 0
 * 输出: 0
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/search-insert-position
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_35_SearchInsert {

    @Test
    public void test() {
        System.out.println(searchInsert(new int[]{1, 3, 5, 6}, 7));
        System.out.println(searchInsert2(new int[]{1, 3, 5, 6}, 7));
        System.out.println(searchInsert3(new int[]{1, 3, 5, 6}, 7));
    }

    /**
     * 简单暴力
     *
     * @param nums
     * @param target
     * @return
     */
    public int searchInsert(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] >= target) {
                return i;
            }
        }
        return nums.length;
    }

    /**
     * 二分法
     *
     * @param nums
     * @param val
     * @return
     */
    public int searchInsert2(int[] nums, int val) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int left = 0, right = nums.length - 1, ans = nums.length;
        while (left <= right) {
            int middle = ((right - left) >> 1) + left;
            if (val <= nums[middle]) {
                ans = middle;
                // -1 正好越过临界值, 下一次循环可以直接取ans
                right = middle - 1;
            } else {
                // +1 正好越过临界值
                left = middle + 1;
            }
        }
        return ans;
    }

    public int searchInsert3(int[] nums, int val) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int left = 0, right = nums.length - 1;
        while (left <= right) {
            int middle = ((right - left) >> 1) + left;
            if (val <= nums[middle]) {
                // -1 正好越过临界值, 下一次循环可以直接取ans
                right = middle - 1;
            } else {
                // +1 正好越过临界值
                left = middle + 1;
            }
        }
        return left;
    }
}
