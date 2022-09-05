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
        System.out.println(longestPalindrome("xaabacxcabaaxcabaax"));
        System.out.println(longestPalindrome("cbbd"));
        System.out.println(longestPalindrome("aaaaa"));
        System.out.println();
    }

    /**
     * 中心扩散
     * 效率O(n^2)
     * 
     * 思路
     * 以某个字符作为中心 奇对称
     * 以某两个字符中间作为中心, 偶对称
     */
    public String longestPalindrome(String s) {
        if (s.length() == 1) return s;
        char[] chars = s.toCharArray();

        int max = 1;
        int start = 0;
        // 长度超过剩余则不用继续
        for (int i = 0; i <= chars.length - (max >>> 1); i++) {
            // 奇对称
            int m2 = palindrome(chars, i - 1, i + 1) + 1;
            if (m2 > max) {
                max = m2;
                start = i - (m2 / 2);
            }
            // 偶对称
            int m1 = palindrome(chars, i, i + 1);
            if (m1 > max) {
                max = m1;
                start = i - (m1 >>> 1) + 1;
            }
        }
        if (max == chars.length) return s;
        return s.substring(start, start + max);
    }

    private int palindrome(char[] chars, int start, int end) {
        int len = 0;
        while (start >= 0 && end < chars.length && chars[start--] == chars[end++]) {
            // 相等一次+2
            len += 2;
        }
        return len;
    }

    @Test
    public void test2() {
        // "xaabacxcabaax"
        System.out.println(longestPalindrome2("xaabacxcabaaxcabaax"));
        System.out.println(longestPalindrome2("cbbd"));
        System.out.println(longestPalindrome2("aaaaa"));
        System.out.println(longestPalindrome2("ac"));
        System.out.println(longestPalindrome2("abb"));
        System.out.println();
    }

    /**
     * 中心扩散优化,
     * 效率O(n^2), 不过会比O(n^2)低, 在有连续相等的字符中效果更明显
     * 
     * 思路
     * 将中心变成一个相等的子串
     * 最少的是, 要么是一个字符(奇对称), 要么是两个相等的字符(偶对称)
     */
    public String longestPalindrome2(String s) {
        if (s.length() == 1) return s;

        char[] chars = s.toCharArray();
        int max = 1;
        int start = 0;
        // 长度超过剩余则不用继续
        for (int i = 0; i < chars.length - (max >> 2); ) {
            // r = 跳过连续相等, 的下一个位置
            int r = i;
            while (r < chars.length && chars[r] == chars[i]) r++;
//            System.out.printf("i=%s, r=%s \r\n", i, r);

            // begin = 连续相等的左边位置
            int begin = i - 1;
            int end = r;
            // 开始重新扩散
            while (begin >= 0 && end < chars.length && chars[begin] == chars[end]) {
                begin--;
                end++;
            }
            // 扩散后的最大长度
            int len = end - (begin + 1);
//            System.out.println("len=" + len);
            if (len > max) {
                // 更新长度和开始位置
                max = len;
                start = begin + 1;
            }
            // 跳过连续的长度
            i = r;
        }
        if (max == chars.length) return s;
        return s.substring(start, start + max);
    }

    @Test
    public void test3() {
        // "xaabacxcabaax"
        System.out.println(longestPalindrome3("xaabacxcabaaxcabaax"));
        System.out.println(longestPalindrome3("cbbd"));
        System.out.println(longestPalindrome3("aaaaa"));
        System.out.println(longestPalindrome3("ac"));
        System.out.println(longestPalindrome3("abb"));
        System.out.println();
    }
    @Test
    public void testm() {
        // "xaabacxcabaax"
        System.out.println(manacher("xaabacxcabaaxcabaax".toCharArray()));
        System.out.println(manacher("cbbd".toCharArray()));
        System.out.println(manacher("aaaaa".toCharArray()));
        System.out.println(manacher("ac".toCharArray()));
        System.out.println(manacher("abb".toCharArray()));
        System.out.println();
    }

    /**
     * manacher 马拉车 TODO
     * 
     */
    public String longestPalindrome3(String s) {
        if (s.length() == 1) return s;

        char[] oldCs = s.toCharArray();
        char[] cs = manacher(oldCs);
        int[] m = new int[cs.length];
        int lastIndex = m.length - 2;

        // 最大长度
        int max = 0;
        // 最大长度的索引中心
        int maxIndex = 0;

        // 当前最大长度索引中心
        int c = 1;
        // 基于c为中心的最大长度对称的右边最后一个位置
        int r = 1;
        for (int i = 2; i < lastIndex; i++) {
            // 找到对称位置的最大子串
            if (r > i) {
                int li = (c << 1) - i;
                m[i] = i + m[li] <= r ? m[li] : r - i;
            }

            // 以i为中心, 扩散
            while (cs[i + m[i] + 1] == cs[i - m[i] - 1]) {
                m[i]++; // 长度增加
            }

            // 更新 c, r
            if (i + m[i] > r) {
                c = i;
                r = i + m[i];
            }

            // 找出更大的子串
            if (m[i] > max) {
                max = m[i];
                // 记录最大子串的位置
                maxIndex = i;
            }
        }

        int start = (maxIndex - max) >> 1;
        if (max == s.length()) return s;
        return new String(oldCs, start, max);
    }

    /**
     * 预处理原字符串
     */
    private char[] manacher(char[] chars) {
        char[] m = new char[(chars.length << 1) + 3];
        char delimiter = '#'; // 自定义分隔符, 可以使任意字符
        m[0] = '^'; // 开始字符, 必须唯一, 不能出现在主串中
        m[1] = delimiter; 
        m[m.length - 1] = '$'; // 截止字符, 必须唯一, 不能出现在主串中

        for (int i = 0; i < chars.length; i++) {
            int j = (i << 1) + 2;
            m[j] = chars[i];
            m[j + 1] = delimiter;
        }
        return m;
    }
}
