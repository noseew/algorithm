package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 169. 多数元素
 * 给定一个大小为 n 的数组，找到其中的多数元素。多数元素是指在数组中出现次数 大于 ⌊ n/2 ⌋ 的元素。
 *
 * 你可以假设数组是非空的，并且给定的数组总是存在多数元素。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/majority-element
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_169_majorityElement {

    @Test
    public void test() {
        System.out.println(majorityElement(new int[]{1, 2, 2}));
        System.out.println(majorityElement(new int[]{2, 2, 1, 1, 1, 2, 2}));
        System.out.println(majorityElement2(new int[]{1, 2, 2}));
        System.out.println(majorityElement2(new int[]{2, 2, 1, 1, 1, 2, 2}));
    }

    /**
     * 思路
     * 使用map计数
     * 速度较慢
     */
    public int majorityElement(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        for (int num : nums) {
            Integer val = map.getOrDefault(num, 0);
            map.put(num, ++val);
            if (val >= (nums.length >> 1) + 1) {
                return num;
            }
        }
        return 0;
    }

    /**
     * 思路 将数组中两种元素映射成 a 和 -a, 一次遍历进行相加, 根据结果 是正数或者负数判断 a多还是-a多
     * 未通过, 此方法仅限于2个数
     */
    public int majorityElement2(int[] nums) {
        Integer n1_temp = null, n2_temp = null , sum = 0;
        for (int num : nums) {
            if (n1_temp == null) {
                n1_temp = num;
            } else if (n1_temp == num) {
                sum += num;
            } else if (n2_temp == null) {
                sum -= n1_temp;
                n2_temp = num;
            } else if (n2_temp == num) {
                sum -= n1_temp;
            }
        }
        return sum >= 0 ? n1_temp : n2_temp;
    }


}
