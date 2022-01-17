package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

/**
 * 231. 2的幂
 * 给定一个整数，编写一个函数来判断它是否是 2 的幂次方。
 */
public class Leetcode_231_isPowerOfTwo {

    @Test
    public void test() {
        System.out.println("true = " + isPowerOfTwo(1));
        System.out.println("false = " + isPowerOfTwo(0));
        System.out.println("true = " + isPowerOfTwo(16));
        System.out.println("false = " + isPowerOfTwo(218));
        System.out.println("false = " + isPowerOfTwo(Integer.MIN_VALUE));
        System.out.println();
        System.out.println("true = " + isPowerOfTwo2(1));
        System.out.println("false = " + isPowerOfTwo2(0));
        System.out.println("true = " + isPowerOfTwo2(16));
        System.out.println("false = " + isPowerOfTwo2(218));
        System.out.println("false = " + isPowerOfTwo2(Integer.MIN_VALUE));

        System.out.println(BinaryUtils.binaryPretty(16));
        System.out.println(BinaryUtils.binaryPretty(-16));
    }

    /**
     * 思路
     * 1. 必须正整数
     * 2. 0100 & 0011 == 0
     */
    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * 思路
     * 1. 必须正整数
     * 2. 0100 & 1100 == 0100
     */
    public boolean isPowerOfTwo2(int n) {
        return n > 0 && (-n & n) == n;
    }


}
