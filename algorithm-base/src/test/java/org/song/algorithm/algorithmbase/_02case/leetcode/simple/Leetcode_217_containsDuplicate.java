package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 217. 存在重复元素
 * 给定一个整数数组，判断是否存在重复元素。
 * <p>
 * 如果存在一值在数组中出现至少两次，函数返回 true 。如果数组中每个元素都不相同，则返回 false 。
 */
public class Leetcode_217_containsDuplicate {

    @Test
    public void test() {
        System.out.println("true = " + containsDuplicate(new int[]{1, 2, 3, 1}));
        System.out.println("false = " + containsDuplicate(new int[]{1,2,3,4}));
    }

    @Test
    public void test2() {
        System.out.println(1 ^ 3 ^ 2);
    }

    /**
     * 使用 set
     *
     */
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>(nums.length);
        for (int num : nums) {
            if (!set.add(num)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsDuplicate2(int[] nums) {
        return false;
    }


}
