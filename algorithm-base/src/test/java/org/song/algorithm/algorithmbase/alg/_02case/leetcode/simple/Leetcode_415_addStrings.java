package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 415. 字符串相加
 * 给定两个字符串形式的非负整数 num1 和num2 ，计算它们的和。
 * <p>
 * 提示：
 * num1 和num2 的长度都小于 5100
 * num1 和num2 都只包含数字 0-9
 * num1 和num2 都不包含任何前导零
 * 你不能使用任何內建 BigInteger 库， 也不能直接将输入的字符串转换为整数形式
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_415_addStrings {

    @Test
    public void test() {
        System.out.println(addStrings("11", "22"));
        System.out.println(addStrings("9", "1"));
        System.out.println(addStrings("99", "1"));
        System.out.println(addStrings("89", "11"));
    }

    public String addStrings(String num1, String num2) {
        char[] chars1 = num1.toCharArray();
        char[] chars2 = num2.toCharArray();
        int maxLen = chars1.length > chars2.length ? chars1.length : chars2.length;
        int minLen = chars1.length > chars2.length ? chars2.length : chars1.length;
        char[] maxChars = chars1.length > chars2.length ? chars1 : chars2;
        char[] minChars = chars1.length > chars2.length ? chars2 : chars1;

        // 是否需要进一
        boolean plus = false;
        for (int lenMax = maxLen - 1, lenMix = minLen - 1;
             lenMax >= 0 || lenMix >= 0;
             lenMax--, lenMix--) {

            // 从后往前 逐一相加, 逐一进一
            int temp = charToInt(maxChars[lenMax]);
            if (lenMix >= 0) {
                temp += charToInt(minChars[lenMix]);
            }
            if (plus) {
                // 进一
                temp++;
                plus = false;
            }
            if (temp > 9) {
                // 是否需要进一
                plus = true;
                maxChars[lenMax] = intToChar(temp % 10);
            } else {
                maxChars[lenMax] = intToChar(temp);
            }
        }

        // 最后一位的 进一 处理
        if (plus) {
            // 范围溢出处理
            return "1" + new String(maxChars);
        }
        return new String(maxChars);
    }

    private int charToInt(char c) {
        return c - 48;
    }

    private char intToChar(int c) {
        return (char) (c + 48);
    }


}
