package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.Test;

/**
 * 28. 实现 strStr()
 * 实现 strStr() 函数。
 * <p>
 * 给定一个 haystack 字符串和一个 needle 字符串，在 haystack 字符串中找出 needle 字符串出现的第一个位置 (从0开始)。如果不存在，则返回  -1。
 * <p>
 * 示例 1:
 * <p>
 * 输入: haystack = "hello", needle = "ll"
 * 输出: 2
 * 示例 2:
 * <p>
 * 输入: haystack = "aaaaa", needle = "bba"
 * 输出: -1
 * 说明:
 * <p>
 * 当 needle 是空字符串时，我们应当返回什么值呢？这是一个在面试中很好的问题。
 * <p>
 * 对于本题而言，当 needle 是空字符串时我们应当返回 0 。这与C语言的 strstr() 以及 Java的 indexOf() 定义相符。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/implement-strstr
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_28_strstr {

    @Test
    public void test() {
        System.out.println(strStr("mississippi", "issip"));
    }

    /**
     * 思路
     * needle 在 haystack 中的第一个位置
     * 暴力匹配
     *
     * 思路2
     * 官网 KPM算法, 哎
     *
     * @param haystack
     * @param needle
     * @return
     */
    public int strStr(String haystack, String needle) {
        if (haystack == null || needle == null || needle.length() == 0) {
            return 0;
        }
        if (haystack.length() == 0) {
            return -1;
        }
        if (needle.length() > haystack.length()) {
            return -1;
        }
        char[] needles = needle.toCharArray();
        char[] haystacks = haystack.toCharArray();

        // 已匹配数量
        int matchCount = 0;
        // 匹配的索引
        int matchIndex = -1;
        // 是否匹配完成
        boolean match = false;


        for (int i = 0; i < haystacks.length;i++) {
            if (match) {
                break;
            }
            // 初始匹配索引
            int itemp = i;
            for (int j = 0; j < needles.length; j++) {
                // 逐个匹配, 判断是否相等
                if (needles[j] == haystacks[i]
                        // 判断剩余字符是否满足未匹配长度
                        && haystacks.length - i >= needles.length - matchCount) {
                    // 匹配数+1
                    matchCount++;
                    // 匹配索引赋值
                    matchIndex = itemp;
                    // 外层i+1, 同内层j+1 逐个匹配
                    i++;
                    // 匹配完成
                    if (matchCount == needle.length()) {
                        match = true;
                        break;
                    }
                } else {
                    // 匹配失败, 重置
                    matchCount = 0;
                    matchIndex = -1;
                    // 外层从i重新开始
                    i = itemp;
                }
                if (matchCount != j + 1) {
                    // 外层从i重新开始
                    i = itemp;
                    break;
                }
            }
        }
        if (matchCount < needle.length()) {
            return -1;
        }

        return matchIndex;
    }

}
