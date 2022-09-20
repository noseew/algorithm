package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

/**
 * 55. 跳跃游戏
 * 给定一个非负整数数组 nums ，你最初位于数组的 第一个下标 。
 * <p>
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * <p>
 * 判断你是否能够到达最后一个下标。
 */
public class Leetcode_55_canJump {


    @Test
    public void test() {
        System.out.println(canJump(new int[]{2, 3, 1, 1, 4})); // true
        System.out.println(canJump(new int[]{3, 2, 1, 0, 4})); // false
        System.out.println(canJump(new int[]{2, 0})); // true
        System.out.println(canJump(new int[]{2, 5, 0, 0})); // true ???
    }

    public boolean canJump(int[] nums) {
        if (nums.length == 1) return true;

        int start = 0;
        int last = nums.length - 1;

        while (start < last) {
            int num = nums[start];
            if (num == 0) break;
            start = start + num;
        }
        return start >= last;
    }
}
