package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.simple;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Leetcode_07_ReverseInteger {

    @Test
    public void test() {
        System.out.println(Math.log10(Math.pow(10, 8)));
        System.out.println(Math.log10(1));

    }

    /**
     * 来自算法网站 https://leetcode.com/problems
     * 难度: 简单
     * <p>
     * 翻转数字
     */
    @Test
    public void contextLoads() {
        int num = -2;
//        reverse(num);
        System.out.println(reverse2(num));
    }


    public void reverse(int num) {

        long i = num;
        if (i < 0) {
            i = -i;
        }

        List<Long> list = new ArrayList<>();
        while (true) {
            if (i <= 0) {
                break;
            }
            list.add(i % 10);
            i /= 10;
        }

        long val = 0;
        for (int i1 = 0; i1 < list.size(); i1++) {
            val += list.get(i1) * Math.pow(10, list.size() - i1 - 1);
        }
        if (i < 0) {
            val = -val;
        }

        if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE) {
            val = 0;
        }


        System.out.println(val);

    }

    public int reverse2(int num) {
        boolean isFu = num < 0;
        num = isFu ? -num : num;
        long val = 0;
        int len = (int) Math.log10(num) + 1;
        for (int i = 0; i < len; i++) {
            if (num <= 0) {
                break;
            }
            val += num % 10 * Math.pow(10, len - i - 1);
            num /= 10;
        }

        if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE) {
            val = 0;
        }
        return isFu ? (int) -val : (int) val;
    }


}
