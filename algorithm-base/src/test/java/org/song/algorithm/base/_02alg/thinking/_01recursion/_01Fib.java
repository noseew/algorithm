package org.song.algorithm.base._02alg.thinking._01recursion;

import org.junit.jupiter.api.Test;

/*
菲波那切数列
1,1,2,3,5,8,13...
 */
public class _01Fib {
    
    
    @Test
    public void test01() {
        assert fib01(1) == 1;
        assert fib01(2) == 1;
        assert fib01(4) == 3;
        assert fib01(5) == 5;
        assert fib01(6) == 8;
    }

    /**
     * 效率 O(2^n)
     * 效率很低, 有重复计算问题
     */
    public int fib01(int n) {
        if (n < 2) return n;
        return fib01(n - 1) + fib01(n - 2);
    }

    @Test
    public void test02() {
        assert fib02(1) == 1;
        assert fib02(2) == 1;
        assert fib02(4) == 3;
        assert fib02(5) == 5;
        assert fib02(6) == 8;
    }

    /**
     * 效率 O(n)
     * 
     * 利用重复计算的结果
     */
    public int fib02(int n) {
        if (n < 2) return n;
        // 重复计算的结果放入数组, 用的时候直接取即可
        int[] array = new int[n + 1];
        array[1] = array[2] = 1;
        return fib02(n, array);
    }

    public int fib02(int n, int[] array) {
        if (array[n] == 0) {
            // 如果该数没有计算, 则计算后放入数组
            array[n] = fib02(n - 1, array) + fib02(n - 2, array);
        }
        // 如果该数已经计算过了, 则直接返回, 达到利用已经计算过的数
        return array[n];
    }

    @Test
    public void test03() {
        assert fib03(1) == 1;
        assert fib03(2) == 1;
        assert fib03(4) == 3;
        assert fib03(5) == 5;
        assert fib03(6) == 8;
    }

    /**
     * 采用循环方式计算斐波那契
     */
    public int fib03(int n) {
        if (n < 2) return n;
        int i = 0, j = 1;
        int number = 0;
        for (int k = 2; k <= n; k++) {
            number = i + j;
            i = j;
            j = number;
        }
        return number;
    }

    @Test
    public void test04() {
        assert fib04(1) == 1;
        assert fib04(2) == 1;
        assert fib04(4) == 3;
        assert fib04(5) == 5;
        assert fib04(6) == 8;
    }

    /**
     * 采用循环方式计算斐波那契
     */
    public int fib04(int n) {
        if (n < 2) return n;
        int i = 0, j = 1;
        for (int k = 2; k <= n; k++) {
            j = i + j;
            i = j - i;
        }
        return j;
    }

    @Test
    public void test05() {
        assert fib05(1) == 1;
        assert fib05(2) == 1;
        assert fib05(4) == 3;
        assert fib05(5) == 5;
        assert fib05(6) == 8;
    }

    /**
     * 利用公式
     */
    public int fib05(int n) {
        double c = Math.sqrt(5);
        return (int) ((Math.pow((1 + c) / 2, n) - Math.pow((1 - c) / 2, n)) / c);
    }
}
