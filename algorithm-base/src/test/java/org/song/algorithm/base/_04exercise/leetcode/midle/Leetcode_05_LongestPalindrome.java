package org.song.algorithm.base._04exercise.leetcode.midle;

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
        System.out.println(longestPalindrome2("xaabacxcabaaxcabaax"));
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

    /**
     * 暴力解法
     */
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

    @Test
    public void test3() {
        // "xaabacxcabaax"
        System.out.println(longestPalindrome3("xaabacxcabaaxcabaax"));
        System.out.println(longestPalindrome3("cbbd"));
        System.out.println(longestPalindrome3("aaaaa"));
        System.out.println();
    }

    /**
     * 中心扩散优化, 上面两个方法的优化
     */
    public String longestPalindrome3(String s) {

        if (s.length() <= 1) {
            return s;
        }

        char[] chars = s.toCharArray();
        int length = chars.length;

        int max = 1;
        int start = 0;
        // 长度超过剩余则不用继续
        for (int i = 0; i <= length - (max / 2); i++) {
            // 奇对称
            int m2 = palindrome(chars, i, i);
            if (m2 > max) {
                max = m2;
                start = i - (m2 / 2);
            }
            // 偶对称
            int m1 = palindrome(chars, i, i + 1);
            if (m1 > max) {
                max = m1;
                start = i - (m1 / 2) + 1;
            }
        }
        return s.substring(start, start + max);
    }

    private int palindrome(char[] chars, int start, int end) {
        int len = 0;
        if (start == end) {
            // 奇对称提前+1
            len++;
            start--;
            end++;
        }
        while (start >= 0 && end < chars.length) {
            if (chars[start--] != chars[end++]) break;
            // 相等一次+2
            len += 2;
        }
        return len;
    }

    @Test
    public void test4() {
        // "xaabacxcabaax"
        System.out.println(longestPalindrome4("xaabacxcabaaxcabaax"));
        System.out.println(longestPalindrome4("cbbd"));
        System.out.println(longestPalindrome4("aaaaa"));
        System.out.println();
    }

    /**
     * 中心扩散优化, 上面两个方法的优化
     */
    public String longestPalindrome4(String s) {
        char[] chars = s.toCharArray();

        int max = 1;
        int start = 0;
        // 长度超过剩余则不用继续
        for (int i = 0; i <= chars.length - (max / 2); i++) {
            // 奇对称
            int m2 = palindrome2(chars, i - 1, i + 1) + 1;
            if (m2 > max) {
                max = m2;
                start = i - (m2 / 2);
            }
            // 偶对称
            int m1 = palindrome2(chars, i, i + 1);
            if (m1 > max) {
                max = m1;
                start = i - (m1 / 2) + 1;
            }
        }
        return s.substring(start, start + max);
    }

    private int palindrome2(char[] chars, int start, int end) {
        int len = 0;
        while (start >= 0 && end < chars.length && chars[start--] == chars[end++]) {
            if (chars[start--] == chars[end++]) break;
            // 相等一次+2
            len += 2;
        }
        return len;
    }

    @Test
    public void test5() {
        // "xaabacxcabaax"
        System.out.println(longestPalindrome5("xaabacxcabaaxcabaax"));
        System.out.println(longestPalindrome5("cbbd"));
        System.out.println(longestPalindrome5("aaaaa"));
        System.out.println();
    }

    /**
     * 中心扩散优化, 上面两个方法的优化, 
     * 优化内存, 减少方法调用, 不必要的String
     */
    public String longestPalindrome5(String s) {
        char[] chars = s.toCharArray();

        int max = 1;
        int start = 0;
        // 长度超过剩余则不用继续
        for (int i = 0; i <= chars.length - (max >>> 2); i++) {
            // 奇对称
            int st = i - 1;
            int ed = i + 1;
            int len = 1;
            while (st >= 0 && ed < chars.length && chars[st--] == chars[ed++]) len += 2;
            if (len > max) {
                max = len;
                start = i - (len >>> 1);
            }
            // 偶对称
            st = i;
            ed = i + 1;
            len = 0;
            while (st >= 0 && ed < chars.length && chars[st--] == chars[ed++]) len += 2;
            if (len > max) {
                max = len;
                start = i - (len >>> 1) + 1;
            }
        }
        if (max == chars.length) return s;
        return s.substring(start, start + max);
    }
}
