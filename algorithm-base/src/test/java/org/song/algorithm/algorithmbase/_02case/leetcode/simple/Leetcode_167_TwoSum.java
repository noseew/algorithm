package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 167. 两数之和 II - 输入有序数组
 * <p>
 * 给定一个已按照 升序排列  的整数数组 numbers ，请你从数组中找出两个数满足相加之和等于目标数 target 。
 * <p>
 * 函数应该以长度为 2 的整数数组的形式返回这两个数的下标值。numbers 的下标 从 1 开始计数 ，所以答案数组应当满足 1 <= answer[0] < answer[1] <= numbers.length 。
 * <p>
 * 你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/two-sum-ii-input-array-is-sorted
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_167_TwoSum {

    @Test
    public void test() {
//        int[] numbers = new int[]{3, 3, 4};
//        int target = 6;
//        int[] numbers = new int[]{2, 7, 11, 15};
//        int target = 9;
//        int[] numbers = new int[]{-1,0};
//        int target = -1;
        int[] numbers = new int[]{2,3,4};
        int target = 6;
        System.out.println(Arrays.toString(twoSum(numbers, target)));
    }

    /**
     * 时间复杂度 o(n)
     * 思路
     * 和原始版本两数之和一致, 返回的时候加上顺序判断
     *
     */
    public int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> map = new HashMap<>(numbers.length);
        for (int i = 0; i < numbers.length; i++) {
            if (map.containsKey(target - numbers[i])) {
                int j = map.get(target - numbers[i]);
                if (i < j) {
                    return new int[]{i + 1, j + 1};
                }
                return new int[]{j + 1, i + 1};
            } else {
                map.put(numbers[i], i);
            }
        }
        return null;
    }

}
