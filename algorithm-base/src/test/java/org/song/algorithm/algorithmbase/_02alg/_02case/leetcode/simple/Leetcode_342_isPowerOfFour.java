package org.song.algorithm.algorithmbase._02alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

/**
 * 342. 4的幂
 *
 * 给定一个整数，写一个函数来判断它是否是 4 的幂次方。如果是，返回 true ；否则，返回 false 。
 *
 * 整数 n 是 4 的幂次方需满足：存在整数 x 使得 n == 4x
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/power-of-four
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_342_isPowerOfFour {

    @Test
    public void test() {
        System.out.println("true = " + isPowerOfFour(1));
        System.out.println("false = " + isPowerOfFour(2));
        System.out.println("true = " + isPowerOfFour(16));
        System.out.println("false = " + isPowerOfFour(8));
        System.out.println("false = " + isPowerOfFour(5));
        System.out.println("false = " + isPowerOfFour(20));

        System.out.println(BinaryUtils.binaryPretty(16));
        System.out.println(BinaryUtils.binaryPretty(-16));
    }

    /**
     * 思路
     * 是2次幂数, 同时又在4次幂数范围内
     */
    public boolean isPowerOfFour(int n) {
        if (n == 1) {
            return true;
        }
        // 先判断2次幂数, 同时又在4次幂数范围内
        int four = 0B0101_0101_0101_0101_0101_0101_0101_0100;
        return (n > 0 && ((n & -n & four) == n));
    }


}
