package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.Test;

public class Leetcode_14_LongestCommonPrefix {

    @Test
    public void test() {
        String prefix = longestCommonPrefix2(new String[]{"flower","flow","flight"});
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

        // 循环次数不可能超过任意一个字符串的长度
        char[] lastChars = new char[strs[0].length()];
        for (int j = 0; j < lastChars.length; j++) {

            boolean finish = false;
            // 循环每个字符串, 取第j个字符进行对比
            for (String str : strs) {
                if (lastChars[j] == '\u0000') {
                    lastChars[j] = str.toCharArray()[j];
                } else if (lastChars[j] != str.toCharArray()[j]) {
                    finish = true;
                }
            }
            // 如果提前结束, 则直接返回
            if (finish) {
                return new String(lastChars, 0, j);
            }
        }
        return "";
    }
}
