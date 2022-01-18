package org.song.algorithm.algorithmbase._02alg._03app.leetcode.simple;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class Leetcode_01_TwoSum {

    /**
     * 来自算法网站 https://leetcode.com/problems/two-sum/
     * 难度: 简单
     * 算法题目名称: tow sum
     * <p>
     * 给定整数数组，返回指数在这两个数字中，它们加起来就是一个特定的目标。
     * <p>
     * 您可以假设每个输入都有一点儿没错一个解决方案，您可以不使用同元素两次。
     * <p>
     * 例子：
     * <p>
     * Given nums = [2, 7, 11, 15], target = 9,
     * <p>
     * Because nums[0] + nums[1] = 2 + 7 = 9,
     * return [0, 1].
     */
    @Test
    public void test() {
        int[] nums = new int[]{3, 3, 4};
        int target = 6;
//        print(twoSum(nums, target));
        print(twoSum2(nums, target));
    }

    /**
     * 时间复杂度 o(n^2)
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < nums.length; j++) {
                if (nums[i] + nums[j] == target && i != j) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * 时间复杂度 o(n)
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum2(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(target - nums[i])) {
                int j = map.get(target - nums[i]);
                if(i < j){
                    return new int[]{i, j};
                }
                return new int[]{j, i};
            } else {
                map.put(nums[i], i);
            }
        }
        return null;
    }

    public static void print(int[] data) {
        if (data != null) {
            Arrays.stream(data).forEach(System.out::println);
        }
    }

}
