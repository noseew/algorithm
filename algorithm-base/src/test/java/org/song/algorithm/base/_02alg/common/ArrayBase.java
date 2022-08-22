package org.song.algorithm.base._02alg.common;

import org.junit.jupiter.api.Test;

public class ArrayBase {

    @Test
    public void test01() {
        System.out.println("1 = " + mid(new int[]{1}, 1));
        System.out.println("-1 = " + mid(new int[]{1}, -1));
        System.out.println("1 = " + mid(new int[]{1, 2, 3}, 1));
        System.out.println("3 = " + mid(new int[]{1, 2, 3}, 3));
        System.out.println("-1 = " + mid(new int[]{1, 2, 3}, -1));
        
        System.out.println("-1 = " + mid(new int[]{1, 2, 3, 5}, -1));
        System.out.println("3 = " + mid(new int[]{1, 2, 3, 4, 5}, 3));
    }

    /**
     * 二分法取中
     */
    public int mid(int[] nums, int v) {
        return mid(nums, v, 0, nums.length);
    }

    public int mid(int[] nums, int v, int l, int r) {

        while (l < r) {
            int mid = l + (r - l) / 2;
            if (v < nums[mid]) {
                r = mid;
            } else if (v > nums[mid]) {
                l = mid + 1;
            } else {
                return nums[mid];
            }
        }
        return -1;
    }
}
