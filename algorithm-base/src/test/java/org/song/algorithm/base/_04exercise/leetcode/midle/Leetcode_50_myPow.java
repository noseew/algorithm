package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

/**
 * 50. Pow(x, n)
 * 实现 pow(x, n) ，即计算 x 的整数 n 次幂函数（即，xn ）。
 */
public class Leetcode_50_myPow {

    /**
     *
     */
    @Test
    public void test() {
        System.out.println(myPow(2.00000, 10));
        System.out.println(myPow(2.10000, 3));
        System.out.println(myPow(2.00000, -2));
    }

    /**
     * O(n)
     * 超出时间限制
     */
    public double myPow(double x, int n) {
        double res = 1;
        if (n > 0) {
            while (n > 0) {
                res *= x;
                n--;
            }
            return res;
        }
        while (n < 0) {
            res *= x;
            n++;
        }
        return 1 / res;
    }
}
