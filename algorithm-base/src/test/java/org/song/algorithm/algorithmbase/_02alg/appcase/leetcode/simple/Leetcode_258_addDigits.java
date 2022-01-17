package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * 258. 各位相加
 * 给定一个非负整数 num，反复将各个位上的数字相加，直到结果为一位数。
 */
public class Leetcode_258_addDigits {

    @Test
    public void test() {
        System.out.println("1 = " + addDigits(1));
        System.out.println("2 = " + addDigits(38));

        System.out.println("1 = " + addDigits2(1));
        System.out.println("2 = " + addDigits2(38));

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int nextInt = random.nextInt(10000);
            if (nextInt < 0) {
                i--;
                continue;
            }
            if (addDigits(nextInt) != addDigits2(nextInt)) {
                System.out.println(nextInt);
            }
        }
    }

    /**
     * 简单思路 循环 递归
     */
    public int addDigits(int num) {
        if (num < 10) {
            return num;
        }
        int sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return addDigits(sum);
    }

    /**
     * 使用除余9
     * 因为最终结果肯定在 0-9 之间
     */
    public int addDigits2(int num) {
        if (num < 10) {
            return num;
        }
        int i = num % 9;
        if (i == 0) {
            return 9;
        }
        return i;
    }


}
