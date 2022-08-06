package org.song.algorithm.base._02alg.thinking._05dp;

import org.junit.jupiter.api.Test;

/**
 * 采用动态规划计算换零钱问题
 * 找零钱
 */
public class _01CoinChange02 {

    /*
    思路
    总长度n, 可选择路径 x in(a,b,c,d)四条, 求最少节点数m
    (a,b,c,d) = (1,5,20,25)
    
    1. 暴力
        第一步, 可选择 x in(a,b,c,d)四个中任意一个, 
        第二步, 可选择 x in(a,b,c,d)四个中任意一个, 
        ...以此类推
        假设总共需要m步, 则计算路径约为m*4
        由于m是不确定的, 每次选择路径是确定的(4步), 总路径长度是确定的n
        
        实现
        采用递归, 递归实现简单些, 每次递归选择有4个分支
     */
    
    
    
    /**
     * 零钱 1, 5, 20, 25
     * 找零 41=20+20+1, 3个
     * 找零 19=5+5+5+1+1+1+1, 7个
     */
    @Test
    public void test() {
        System.out.println(cc1(41));
        System.out.println(cc1(19));
    }

    /**
     * 暴力递归
     */
    private int cc1(int n) {
        if (n == 1
                || n == 5
                || n == 20
                || n == 25) {
            return 1;
        }
        int min = Integer.MAX_VALUE;
        if (n > 1) min = Math.min(min, cc1(n - 1));
        if (n > 5) min = Math.min(min, cc1(n - 5));
        if (n > 20) min = Math.min(min, cc1(n - 20));
        if (n > 25) min = Math.min(min, cc1(n - 25));
        if (min == Integer.MAX_VALUE) {
            // 如果凑不出来, 则返回0, 说明可凑的硬币数是多少, 剩下的就凑不出来了
            // 如果不加这个判断, 则如果凑不出来, 就返回最小值, 说明凑不出来
            return 0;
        }
        return min + 1;
    }
    @Test
    public void test3() {
        System.out.println(cc3(41));
        System.out.println(cc3(19));
    }

    /**
     * 非递归, 动态规划
     */
    private int cc3(int n) {

        // dp[i] 表示 兑换i金额需要多少个硬币, 下标从1开始到n, 所以数组到n+1
        int[] dp = new int[n + 1];
        if (n > 1) dp[1] = 1;
        if (n > 5) dp[5] = 1;
        if (n > 20) dp[20] = 1;
        if (n > 25) dp[25] = 1;
        for (int i = 1; i <= n; i++) {
            int min = Integer.MAX_VALUE;
            /*
            下面4步相当于, 
                接下来用下面4种方式都计算一次, 看看哪种得到的兑换次数最小, 
                由于有范围判断, 所以并不一定是4种方式都计算, 而是选择可能的方式
             */
            if (i > 1) min = Math.min(min, dp[i - 1]);
            if (i > 5) min = Math.min(min, dp[i - 5]);
            if (i > 20) min = Math.min(min, dp[i - 20]);
            if (i > 25) min = Math.min(min, dp[i - 25]);
            if (min != Integer.MAX_VALUE) {
                dp[i] =  min + 1;
            }
        }
        return dp[n];
    }
}
