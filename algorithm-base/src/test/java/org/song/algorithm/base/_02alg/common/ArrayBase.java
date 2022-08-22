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
        int l = 0, r = nums.length;

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

    @Test
    public void test02() {
        System.out.println("1 = " + mid2(new int[]{1}, 1));
        System.out.println("-1 = " + mid2(new int[]{1}, -1));
        System.out.println("1 = " + mid2(new int[]{1, 2, 3}, 1));
        System.out.println("3 = " + mid2(new int[]{1, 2, 3}, 3));
        System.out.println("-1 = " + mid2(new int[]{1, 2, 3}, -1));

        System.out.println("-1 = " + mid2(new int[]{1, 2, 3, 5}, -1));
        System.out.println("3 = " + mid2(new int[]{1, 2, 3, 4, 5}, 3));
    }

    /**
     * 二分法取中
     */
    public int mid2(int[] nums, int v) {
        return mid2(nums, v, 0, nums.length);
    }

    public int mid2(int[] nums, int v, int l, int r) {
        if (r <= l) return -1;

        int mid = l + (r - l) / 2;
        if (v < nums[mid]) {
            return mid2(nums, v, l, mid);
        } else if (v > nums[mid]) {
            return mid2(nums, v, mid + 1, r);
        }
        return nums[mid];
    }
}
