package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 349. 两个数组的交集
 * 给定两个数组，编写一个函数来计算它们的交集。
 */
public class Leetcode_349_intersection {

    @Test
    public void test() {
        System.out.println(Arrays.toString(intersection(new int[]{1, 2, 2, 1}, new int[]{2, 2})));
        System.out.println(Arrays.toString(intersection(new int[]{4, 9, 5}, new int[]{9, 4, 9, 8, 4})));

    }

    /**
     * 思路1
     * 使用set
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>(Math.min(nums1.length, nums2.length));
        Set<Integer> inter = new HashSet<>(Math.min(nums1.length, nums2.length));

        int[] minNums = nums1.length < nums2.length ? nums1 : nums2;
        int[] maxNums = nums1.length < nums2.length ? nums2 : nums1;

        for (int minNum : minNums) {
            set.add(minNum);
        }
        for (int maxNum : maxNums) {
            if (set.contains(maxNum)) {
                inter.add(maxNum);
            }
        }

        int[] intersection = new int[inter.size()];
        int i = 0;
        for (Integer integer : inter) {
            intersection[i++] = integer;
        }

        return intersection;

    }

}
