package org.song.algorithm.algorithmbase._02alg._03app.leetcode.midle;

import org.junit.Test;

/**
 * 5. 最长回文子串
 * 给你一个字符串 s，找到 s 中最长的回文子串。
 *
 * 示例 1：
 *
 * 输入：s = "babad"
 * 输出："bab"
 * 解释："aba" 同样是符合题意的答案。
 * 示例 2：
 *
 * 输入：s = "cbbd"
 * 输出："bb"
 * 示例 3：
 *
 * 输入：s = "a"
 * 输出："a"
 * 示例 4：
 *
 * 输入：s = "ac"
 * 输出："a"
 *  
 *
 * 提示：
 *
 * 1 <= s.length <= 1000
 * s 仅由数字和英文字母（大写和/或小写）组成
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_05_LongestPalindrome {

    @Test
    public void test() {
        // "xaabacxcabaax"
//        System.out.println(longestPalindrome2("xaabacxcabaaxcabaax"));
        System.out.println(longestPalindrome2("cbbd"));
        System.out.println();
    }

    public String longestPalindrome(String s) {

        if (s.length() <= 1) {
            return s;
        }

        char[] chars = s.toCharArray();

        if (chars.length == 2 && chars[0] != chars[1]) {
            return String.valueOf(chars[0]);
        }

        int startIndex = 0;
        int maxLen = 1;

        for (int i = 0; i < chars.length; i++) {
            if (maxLen == chars.length) {
                break;
            }

            if (i >= 1 && chars[i] == chars[i - 1]) {
                // 偶对称
                int maxLenTemp = 0;
                int startIndexTemp = i - 1;
                int maxJ = Math.min(chars.length - i, i);
                for (int j = 0; j < maxJ; j++) {
                    if (chars[i - j - 1] == chars[i + j]) {
                        maxLenTemp += 2;
                        startIndexTemp = i - j - 1;
                    } else {
                        break;
                    }
                }
                if (maxLenTemp > maxLen) {
                    startIndex = startIndexTemp;
                    maxLen = maxLenTemp;
                }
            }
            if (i > 0 && i < chars.length - 1
                    && chars[i - 1] == chars[i + 1]) {
                // 奇对称
                int maxLenTemp = 1;
                int startIndexTemp = i;
                int maxJ = Math.min(chars.length - i - 1, i);
                for (int j = 1; j <= maxJ; j++) {
                    if (chars[i + j] == chars[i - j]) {
                        maxLenTemp += 2;
                        startIndexTemp = i - j;
                    } else {
                        break;
                    }
                }
                if (maxLenTemp > maxLen) {
                    startIndex = startIndexTemp;
                    maxLen = maxLenTemp;
                }
            }

        }

        return s.substring(startIndex, startIndex + maxLen);
    }

    public String longestPalindrome2(String s) {

        if (s.length() <= 1) {
            return s;
        }

        char[] chars = s.toCharArray();

        if (chars.length == 2 && chars[0] != chars[1]) {
            return String.valueOf(chars[0]);
        }

        int startIndex = 0;
        int maxLen = 1;

        for (int i = 0; i < chars.length; i++) {
            if (maxLen == chars.length) {
                break;
            }

            if (i >= 1 && chars[i] == chars[i - 1]) {
                // 偶对称
                int maxLenTemp = 0;
                int startIndexTemp = i - 1;
                int maxJ = Math.min(chars.length - i, i);
                int maxAble = maxLenTemp + maxJ * 2;
                if (maxAble > maxLen) {
                    for (int j = 0; j < maxJ; j++) {
                        if (chars[i - j - 1] != chars[i + j]) {
                            break;
                        }
                        maxLenTemp += 2;
                        startIndexTemp = i - j - 1;
                    }
                }
                if (maxLenTemp > maxLen) {
                    startIndex = startIndexTemp;
                    maxLen = maxLenTemp;
                }
            }
            if (i > 0 && i < chars.length - 1
                    && chars[i - 1] == chars[i + 1]) {
                // 奇对称
                int maxLenTemp = 1;
                int startIndexTemp = i;
                int maxJ = Math.min(chars.length - i - 1, i);
                int maxAble = maxLenTemp + maxJ * 2;
                if (maxAble > maxLen) {
                    for (int j = 1; j <= maxJ; j++) {
                        if (chars[i + j] != chars[i - j]) {
                            break;
                        }
                        maxLenTemp += 2;
                        startIndexTemp = i - j;
                    }
                }
                if (maxLenTemp > maxLen) {
                    startIndex = startIndexTemp;
                    maxLen = maxLenTemp;
                }
            }

        }

        return s.substring(startIndex, startIndex + maxLen);
    }
}
