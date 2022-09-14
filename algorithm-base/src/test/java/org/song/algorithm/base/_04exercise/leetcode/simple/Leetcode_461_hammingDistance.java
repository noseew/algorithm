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
        System.out.println("1 = " + hammingDistance(3, 1));
        // ?
        System.out.println("2 = " + hammingDistance(93, 73));
    }

    /**
     * 
     * 啥是距离? TODO 未完成
     */
    public int hammingDistance(int x, int y) {
        if (x == 0 || y == 0) return 1;
        if (x == y) return 0;
        
        int max, min;

        if (x == Integer.MIN_VALUE || x > y) {
            max = x;
            min = y;
        } else {
            max = y;
            min = x;
        }
        int count = 0;
        while (max != min && max != 0) {
            count++;
            max = max >>> 1;
        }
        return count;
    }


}
