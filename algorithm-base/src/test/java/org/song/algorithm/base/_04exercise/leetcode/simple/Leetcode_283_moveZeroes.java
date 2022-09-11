package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 283. 移动零
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 * <p>
 * 示例:
 * <p>
 * 输入: [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 * 说明:
 * <p>
 * 必须在原数组上操作，不能拷贝额外的数组。
 * 尽量减少操作次数
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/move-zeroes
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_283_moveZeroes {

    @Test
    public void test() {
        int[] ints = new int[]{0, 1, 0, 3, 12};
        moveZeroes2(ints);
        System.out.println(Arrays.toString(ints));
    }

    /**
     * 思路
     * 1. 一次遍历, 遇到0则二次向后遍历, 并替换下一个非零元素
     */
    public void moveZeroes(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                for (int j = i + 1; j < nums.length; j++) {
                    if (nums[j] != 0) {
                        nums[i] = nums[j];
                        nums[j] = 0;
                        break;
                    }
                }
            }
        }
    }

    /**
     * 思路
     * j 下一个非零元素索引保存, 减少遍历次数
     */
    public void moveZeroes2(int[] nums) {
        for (int i = 0, j = i + 1; i < nums.length; i++, j++) {
            if (nums[i] == 0) {
                // j 预测下一个非零元素下标
                for (; j < nums.length; j++) {
                    if (nums[j] != 0) {
                        nums[i] = nums[j];
                        nums[j] = 0;
                        break;
                    }
                }
            }
        }
    }

    @Test
    public void test3() {
        int[] ints = new int[]{0, 1, 0, 3, 12};
        moveZeroes3(ints);
        System.out.println(Arrays.toString(ints));
    }

    /**
     * 思路
     * 双指针, 将非0元素向前移动
     */
    public void moveZeroes3(int[] nums) {

        int noZero = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) continue;
            swap(nums, i, noZero++);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int v = nums[i];
        nums[i] = nums[j];
        nums[j] = v;
    }

    @Test
    public void test4() {
        int[] ints = new int[]{0, 1, 0, 3, 12};
        moveZeroes4(ints);
        System.out.println(Arrays.toString(ints));
        int[] ints2 = new int[]{1, 0};
        moveZeroes4(ints2);
        System.out.println(Arrays.toString(ints2));
    }

    /**
     * 思路
     * 双指针, 将非0元素向前移动
     */
    public void moveZeroes4(int[] nums) {
        if (nums.length == 1) return;
        int noZero = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) continue;
            int v = nums[i];
            nums[i] = nums[noZero];
            nums[noZero++] = v;
        }
    }


}
