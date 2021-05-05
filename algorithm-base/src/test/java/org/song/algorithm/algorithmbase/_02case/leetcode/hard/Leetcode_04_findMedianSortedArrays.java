package org.song.algorithm.algorithmbase._02case.leetcode.hard;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * 4. 寻找两个正序数组的中位数
 */
public class Leetcode_04_findMedianSortedArrays {

    @Test
    public void test() {
        double val = findMedianSortedArrays(new int[]{1}, new int[]{2});
        System.out.println(val);
    }

    /**
     * 未完成
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int[] ints = new int[nums1.length + nums2.length];

        int n1_index = 0;
        int n2_index = 0;
        for (int i = 0; i < ints.length; i++) {
            if (n1_index >= nums1.length) {
                ints[i] = nums2[n2_index++];
            } else if (n2_index >= nums2.length) {
                ints[i] = nums1[n1_index++];
            } else if (nums1[n1_index] < nums2[n2_index]) {
                ints[i] = nums1[n1_index++];
            } else {
                ints[i] = nums2[n2_index++];
            }
        }

        int middle = (ints.length >> 1) - 1;
        if (ints.length % 2 == 0) {
            return (ints[middle] + ints[middle + 1]) / 2D;
        }
        return ints[middle + 1];
    }
}
