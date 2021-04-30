package org.song.algorithm.algorithmbase._02case.leetcode.simple.array;

import org.junit.Test;

/**
 * 回文数
 */
public class Leetcode_09_PalindromeTest {

    @Test
    public void test() {
//        System.out.println(isPalindrome(01));
        System.out.println(isPalindrome02(01));
    }

    public boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        return reverse2(x) == x;
    }

    public int reverse2(int num) {
        int origin = num;

        boolean isFu = num < 0;
        num = isFu ? -num : num;
        long val = 0;
        int len = (int) Math.log10(num) + 1;
        for (int i = 0; i < len; i++) {
            if (num <= 0) {
                break;
            }
            val += num % 10 * Math.pow(10, len - i - 1);
            if (val > origin) {
                return -1;
            }
            num /= 10;
        }

        if (val > Integer.MAX_VALUE || val < Integer.MIN_VALUE) {
            val = 0;
        }
        return isFu ? (int) -val : (int) val;
    }

    public boolean isPalindrome02(int x) {
        if (x < 0) {
            return false;
        }

        int len = (int) Math.log10(x) + 1;
        int[] values = new int[len];

        for (int i = 0; i < len; i++) {
            values[i] = x % 10;
            if (i  >= (len >> 1)) {
                boolean equ = values[len - i - 1] == values[i];
                if (!equ) {
                    return false;
                }
            }
        }
        return true;
    }
}
