package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 461. 汉明距离
 *
 * 两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
 *
 * 给出两个整数 x 和 y，计算它们之间的汉明距离。
 */
public class Leetcode_461_hammingDistance {

    @Test
    public void test() {
        System.out.println("2 = " + hammingDistance(1, 4));
    }

    /**
     * 未完成, 啥是距离 ?
     */
    public int hammingDistance(int x, int y) {
        int count = 0;

        if (x < y) {
            y = Integer.highestOneBit(y);
            while (x < y) {
                x = (x << 1);
                count++;
            }
        } else {
            x = Integer.highestOneBit(x);
            while (y < x) {
                y = (y << 1);
                count++;
            }
        }

        return count;
    }


}
