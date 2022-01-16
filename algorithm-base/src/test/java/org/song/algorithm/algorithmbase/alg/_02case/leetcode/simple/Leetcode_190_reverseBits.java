package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

/**
 * 190. 颠倒二进制位
 */
public class Leetcode_190_reverseBits {

    @Test
    public void test() {
        int n = 1;
        System.out.println(BinaryUtils.binaryPretty(n));
        System.out.println(BinaryUtils.binaryPretty(reverseBits(n)));
        n = 5;
        System.out.println(BinaryUtils.binaryPretty(n));
        System.out.println(BinaryUtils.binaryPretty(reverseBits(n)));
        n = 8;
        System.out.println(BinaryUtils.binaryPretty(n));
        System.out.println(BinaryUtils.binaryPretty(reverseBits(n)));
    }

    /**
     * 思路
     * 分别进行每 1, 2, 4, 8, 16 交换
     */
    public int reverseBits(int n) {
        int c = 0;
        // 交换每1位
        c = ((n & 0B0101_0101_0101_0101_0101_0101_0101_0101) << 1) | ((n >>> 1) & 0B0101_0101_0101_0101_0101_0101_0101_0101);
        // 交换每2位
        c = ((c & 0B0011_0011_0011_0011_0011_0011_0011_0011) << 2) | ((c >>> 2) & 0B0011_0011_0011_0011_0011_0011_0011_0011);
        c = ((c & 0B0000_1111_0000_1111_0000_1111_0000_1111) << 4) | ((c >>> 4) & 0B0000_1111_0000_1111_0000_1111_0000_1111);
        c = ((c & 0B0000_0000_1111_1111_0000_0000_1111_1111) << 8) | ((c >>> 8) & 0B0000_0000_1111_1111_0000_0000_1111_1111);
        c = ((c & 0B0000_0000_0000_0000_1111_1111_1111_1111) << 16) | ((c >>> 16) & 0B0000_0000_0000_0000_1111_1111_1111_1111);
        return c;
    }
}
