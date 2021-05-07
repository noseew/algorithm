package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.Test;

public class Leetcode_14_LongestCommonPrefix {

    @Test
    public void test() {
        String prefix = longestCommonPrefix(new String[]{"flower","flow","flight"});
        System.out.println("最长前缀: " + prefix);
    }

    /**
     * 思路
     * 横向扫描, 一个一个字符串扫描全量对比
     * 1. 一个个对比, 先对比前两个, 找出公共最小前缀,
     * 2. 然后用已有的公共最小前缀, 和第三个对比, 依次类推
     *
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length <= 0) {
            return "";
        }

        // 公共最小前缀, 随着对比的字符串越来越多, 该值越来越准确
        char[] lastChars = null;
        for (String str : strs) {
            char[] currentChars = str.toCharArray();
            if (currentChars == null || currentChars.length <= 0) {
                return "";
            }
            if (lastChars == null) {
                lastChars = currentChars;
            } else {
                int minLen = Math.min(lastChars.length, currentChars.length);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < minLen; i++) {
                    // 前两个找出公共最小前缀
                    if (lastChars[i] == currentChars[i]) {
                        sb.append(lastChars[i]);
                        if (i == minLen - 1) {
                            // 更新 已有公共最小前缀
                            lastChars = sb.toString().toCharArray();
                        }
                        continue;
                    }
                    if (i == 0) {
                        // 只要存在前缀不同, 则没有公共前缀
                        return "";
                    }
                    lastChars = sb.toString().toCharArray();
                    break;
                }
            }
        }
        return new String(lastChars).toLowerCase();
    }

    /**
     * 思路
     * 纵向扫描, 一个一个字符串的前缀一次对比
     *
     * @param strs
     * @return
     */
    public static String longestCommonPrefix2(String[] strs) {
        if (strs == null || strs.length <= 0) {
            return "";
        }
        return null;
    }
}
