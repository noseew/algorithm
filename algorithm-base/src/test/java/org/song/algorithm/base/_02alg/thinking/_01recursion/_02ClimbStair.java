package org.song.algorithm.base._02alg.thinking._01recursion;

import org.junit.jupiter.api.Test;

/*
爬楼梯
给你一段楼梯, 一次可以爬1阶或者2阶, 请问爬到顶上总共有多少种方式
 */
public class _02ClimbStair {
    
    
    @Test
    public void test01() {
        
    }
    /*
    假设 n 阶需要 f(n) 次
        如果第一次走1阶, 则剩下还有 f(n-1) 次
        如果第一次走2阶, 则剩下还有 f(n-2) 次
    由于第一次只能选择1阶或者2阶所以:
        那么 f(n) = f(n-1) + f(n-2)
     */
    public int cs1(int n) {
        if (n <= 2) {
            return n;
        }
        return cs1(n - 1) + cs1(n - 2);
    }

    /*
    由于算法和斐波那契一样, 所以优化方式也一样
     */
    public int cs2(int n) {
        if (n <= 2) {
            return n;
        }
        int i = 1, j = 2;
        for (int k = 3; k <= n; k++) {
            j = i + j;
            i = j - i;
        }
        return j;
    }
}
