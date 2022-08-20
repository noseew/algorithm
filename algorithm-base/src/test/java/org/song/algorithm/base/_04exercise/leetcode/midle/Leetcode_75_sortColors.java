package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 75. 颜色分类
 * 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums ，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
 * <p>
 * 我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
 * <p>
 * 必须在不使用库的sort函数的情况下解决这个问题。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/sort-colors
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_75_sortColors {

    @Test
    public void test() {
//        int[] ints = {2, 0, 2, 1, 1, 0}; // [0,0,1,1,2,2]
//        int[] ints = {2,0,1}; // [0,1,2]
//        int[] ints = {2,1};
//        int[] ints = {2,0};
        int[] ints = {1,0};
        sortColors(ints);
        System.out.println(Arrays.toString(ints));

    }

    /**
     * 思路, 对 0,1,2 进行排序,
     * 要求O(n)
     * 使用三指针
     *
     * @param nums
     */
    public void sortColors(int[] nums) {
        if (nums.length <= 1) return;
        int l = 0, r = nums.length - 1;
        int current = 0;
        while (current <= r) {
            if (nums[current] == 0 && current != l) {
                swap(nums, current, l++);
            } else if (nums[current] == 2) {
                swap(nums, current, r--);
            } else {
                current++;
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int val = nums[i];
        nums[i] = nums[j];
        nums[j] = val;
    }
}
