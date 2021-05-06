package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 136. 只出现一次的数字
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * <p>
 * 说明：
 * <p>
 * 你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/single-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_136_singleNumber {

    @Test
    public void test() {
        System.out.println(singleNumber2(new int[]{1, 2, 2}));
    }

    /**
     *
     * @param nums
     * @return
     */
    public int singleNumber(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        Set<Integer> set = new HashSet<>(nums.length);

        for (int num : nums) {
            if (!set.contains(num)) {
                set.add(num);
            } else {
                set.remove(num);
            }
        }
        Iterator<Integer> iterator = set.iterator();
        return iterator.next();

    }

    /**
     * 采用异或运算
     * 两个相同的数进行异或等于0
     * a ^ a = 0;
     * b ^ 0 = b
     * 成对出现的a可以相互抵消, 没有成对出现的b就会凸显出来
     *
     * @param nums
     * @return
     */
    public int singleNumber2(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }


}
