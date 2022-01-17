package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.simple;

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

    /**
     * 官方题解
     * 反转一半数字(防止溢出)
     *
     * @param x
     * @return
     */
    public boolean isPalindrome03(int x) {
        // 特殊情况：
        // 如上所述，当 x < 0 时，x 不是回文数。
        // 同样地，如果数字的最后一位是 0，为了使该数字为回文，
        // 则其第一位数字也应该是 0
        // 只有 0 满足这一属性
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int revertedNumber = 0;
        while (x > revertedNumber) {
            revertedNumber = revertedNumber * 10 + x % 10;
            x /= 10;
        }

        // 当数字长度为奇数时，我们可以通过 revertedNumber/10 去除处于中位的数字。
        // 例如，当输入为 12321 时，在 while 循环的末尾我们可以得到 x = 12，revertedNumber = 123，
        // 由于处于中位的数字不影响回文（它总是与自己相等），所以我们可以简单地将其去除。
        return x == revertedNumber || x == revertedNumber / 10;
    }
}
