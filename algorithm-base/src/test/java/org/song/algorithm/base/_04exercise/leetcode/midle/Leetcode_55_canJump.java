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

    /**
     * 思路不对, 每一步最多跳[i]步, 而不是只能跳[i]步
     * 未完成
     */
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

    @Test
    public void test2() {
        System.out.println(canJump2(new int[]{2, 3, 1, 1, 4})); // true
        jumped = false;
        System.out.println(canJump2(new int[]{3, 2, 1, 0, 4})); // false
        jumped = false;
        System.out.println(canJump2(new int[]{2, 0})); // true
        jumped = false;
        System.out.println(canJump2(new int[]{2, 5, 0, 0})); // true ???
        jumped = false;
    }

    /**
     * 采用暴力递归
     * 超出时间限制
     */
    public boolean canJump2(int[] nums) {
        if (nums.length == 1) return true;

        jump(nums, 0, nums[0]);
        return jumped;
    }

    private boolean jumped = false;

    /**
     * @param nums
     * @param currentIdx 当前位置
     * @param maxStep    下一步能跳到最远位置
     */
    private void jump(int[] nums, int currentIdx, int maxStep) {
        if (jumped) return;
//        System.out.printf("currentIdx=%s, maxStep=%s, jumped=%s \r\n", currentIdx, maxStep, jumped);
        if (currentIdx + maxStep >= nums.length - 1) {
            jumped = true;
            return;
        }
        for (int step = maxStep; step > 0; step--) {
            int nextIdx = currentIdx + step;
            jump(nums, nextIdx, nums[nextIdx]);
        }
    }
}
