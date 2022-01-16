package org.song.algorithm.algorithmbase._02alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 190. 颠倒二进制位
 * 编写一个函数，输入是一个无符号整数（以二进制串的形式），返回其二进制表达式中数字位数为 '1' 的个数（也被称为汉明重量）。
 */
public class Leetcode_191_hammingWeight {

    @Test
    public void test() {
//        System.out.println("4 = " + hammingWeight(15));
//        System.out.println("3 = " + hammingWeight(7));
//        System.out.println("2 = " + hammingWeight(3));
//        System.out.println("2 = " + hammingWeight(5));

        for (int i = 0; i < 100; i++) {
            int v = ThreadLocalRandom.current().nextInt(10000);
            if (hammingWeight(v) != Integer.bitCount(v)) {
                System.out.println(v);
            }
        }

    }

    /**
     * 思路 计算汉明重量
     * 依次计算每 2, 4, 8, 16 位的1的数量, 并放在这 2, 4,...位上(也就是忽略原本位的概念, 只考虑2, 4, ...位)
     *
     * @param n
     * @return
     */
    public int hammingWeight(int n) {
        int c = 0;
        // 计算每2位的1的数量, 并放到这个2位上
        c = (n & 0B0101_0101_0101_0101_0101_0101_0101_0101) + ((n >>> 1) & 0B0101_0101_0101_0101_0101_0101_0101_0101);
        // 计算每4位的1的数量, 并放到这个4位上
        c = (c & 0B0011_0011_0011_0011_0011_0011_0011_0011) + ((c >>> 2) & 0B0011_0011_0011_0011_0011_0011_0011_0011);
        c = (c & 0B0000_1111_0000_1111_0000_1111_0000_1111) + ((c >>> 4) & 0B0000_1111_0000_1111_0000_1111_0000_1111);
        c = (c & 0B0000_0000_1111_1111_0000_0000_1111_1111) + ((c >>> 8) & 0B0000_0000_1111_1111_0000_0000_1111_1111);
        c = (c & 0B0000_0000_0000_0000_1111_1111_1111_1111) + ((c >>> 16) & 0B0000_0000_0000_0000_1111_1111_1111_1111);
        return c;
    }
}
