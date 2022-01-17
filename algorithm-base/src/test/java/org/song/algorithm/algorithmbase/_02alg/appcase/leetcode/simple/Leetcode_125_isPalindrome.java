package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 125. 验证回文串
 * 给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写。
 * <p>
 * 说明：本题中，我们将空字符串定义为有效的回文串。
 */
public class Leetcode_125_isPalindrome {

    @Test
    public void test() {
        System.out.println("true = " + isPalindrome("A man, a plan, a canal: Panama"));
        System.out.println("false = " + isPalindrome("race a car"));
        System.out.println("true = " + isPalindrome("aa"));
        System.out.println("true = " + isPalindrome("aba"));
        System.out.println("true = " + isPalindrome("abba"));
        System.out.println("false = " + isPalindrome("ab"));
        System.out.println("false = " + isPalindrome("0P"));
        System.out.println("false = " + isPalindrome("abb"));

    }

    /**
     * 思路
     * 1. 过滤有效字符(只获取大小写字符)
     * 2. 判断过滤后的字符是否是回文数
     */
    public boolean isPalindrome(String s) {
        if (s == null || s.length() == 0 || s.length() == 1) {
            return true;
        }

        // 最终有效的数组
        char[] newChar = new char[s.length()];
        char[] chars = s.toCharArray();
        // 最终有效的长度
        int len = 0;
        for (char c : chars) {
            // 过滤有效
            if (isValid(c)) {
                // 全转成大写
                newChar[len++] = upper(c);
            }
        }


        // 从中间开始对比
        int middle = len / 2;
        boolean even = len % 2 == 0;
        for (int i = 0; (even && middle - i - 1 >= 0) || (!even && middle - i >= 0); i++) {
            boolean equ;
            if (even) {
                equ = newChar[middle + i] == newChar[middle - i - 1];
            } else {
                equ = newChar[middle + i] == newChar[middle - i];
            }
            if (!equ) {
                return false;
            }
        }
        return true;
    }

    /**
     * 写法优化
     *
     */
    public boolean isPalindrome2(String s) {
        if (s == null || s.length() == 0 || s.length() == 1) {
            return true;
        }

        // 最终有效的数组
        char[] newChar = new char[s.length()];
        char[] chars = s.toCharArray();
        // 最终有效的长度
        int len = 0;
        for (char c : chars) {
            if (isValid(c)) {
                newChar[len++] = upper(c);
            }
        }

        // 从中间开始对比
        int middle = len >> 1;
        boolean even = len % 2 == 0;
        for (int i = 0; ; i++) {
            boolean equ;
            if (even && middle - i - 1 >= 0) {
                // 偶数
                equ = newChar[middle + i] == newChar[middle - i - 1];
            } else if (!even && middle - i >= 0) {
                // 奇数
                equ = newChar[middle + i] == newChar[middle - i];
            } else {
                break;
            }
            if (!equ) {
                return false;
            }
        }
        return true;
    }

    private boolean isValid(char c) {
        boolean valid = c >= 'a' && c <= 'z';
        valid = valid || c >= 'A' && c <= 'Z';
        return valid || c >= '0' && c <= '9';
    }

    private char upper(char c) {
        if (c >= 'A' && c <= 'Z') {
            return c;
        }
        if (c >= '0' && c <= '9') {
            return c;
        }
        return (char) (c + 'A' - 'a');
    }


}
