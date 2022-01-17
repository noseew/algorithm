package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.midle;

import org.junit.Test;

import java.util.Arrays;

/**
 * 34. 在排序数组中查找元素的第一个和最后一个位置
 * 给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。
 * <p>
 * 如果数组中不存在目标值 target，返回 [-1, -1]。
 * <p>
 * 进阶：
 * <p>
 * 你可以设计并实现时间复杂度为 O(log n) 的算法解决此问题吗？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_34_searchRange {

    @Test
    public void test() {
        System.out.println(Arrays.toString(searchRange(new int[]{1, 2, 2, 3}, 2)));
    }

    /**
     * 思路 二分查找
     * 未完成
     */
    public int[] searchRange(int[] nums, int target) {

        int middle = nums.length / 2;
        int left = 0;
        int right = nums.length - 1;
        while (true) {
            if (nums[middle] >= target) {
                left = middle--;
            } else if (nums[middle] <= target) {
                right = middle++;
            } else {
                if (nums[left] == nums[right]) {
                    return new int[]{left, right};
                }
            }
        }
    }
}
