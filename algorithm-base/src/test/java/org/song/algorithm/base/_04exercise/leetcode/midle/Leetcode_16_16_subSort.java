package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 面试题 16.16. 部分排序
 * 给定一个整数数组，编写一个函数，找出索引m和n，只要将索引区间[m,n]的元素排好序，整个数组就是有序的。注意：n-m尽量最小，也就是说，找出符合条件的最短序列。函数返回值为[m,n]，若不存在这样的m和n（例如整个数组是有序的），请返回[-1,-1]。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/sub-sort-lcci
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_16_16_subSort {

    @Test
    public void test() {
        int[] ints = {1, 2, 4, 7, 10, 11, 7, 12, 6, 7, 16, 18, 19}; // [3,9]
        System.out.println(Arrays.toString(subSort(ints)));

    }


    /**
     * 思路
     * 两次遍历,
     * 1. 从左往右, 找出最后逆序,
     * 2. 从右往左, 找出最后逆序,
     *
     * @param array
     * @return
     */
    public int[] subSort(int[] array) {
        if (array.length <= 1) return new int[]{-1, -1};

        // 1. 从左往右, 找出逆序, 取最大, 找出更小的
        int max = array[0];
        int r_reverse = -1;
        for (int i = 1; i < array.length; i++) {
            if (array[i] < max) {
                r_reverse = i;
            } else {
                max = array[i];
            }
        }
        if (r_reverse == -1) return new int[]{-1, -1};

        // 2. 从左往右, 找出逆序, 取最大, 找出更小的
        int min = array[array.length - 1];
        int l_reverse = -1;
        for (int i = array.length - 2; i >= 0; i--) {
            if (array[i] > min) {
                l_reverse = i;
            } else {
                min = array[i];
            }
        }
        return new int[]{l_reverse, r_reverse};
    }

}
