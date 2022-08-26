package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.Stack;

/**
 * 151. 反转字符串中的单词
 * 给你一个字符串 s ，请你反转字符串中 单词 的顺序。
 *
 * 单词 是由非空格字符组成的字符串。s 中使用至少一个空格将字符串中的 单词 分隔开。
 *
 * 返回 单词 顺序颠倒且 单词 之间用单个空格连接的结果字符串。
 *
 * 注意：输入字符串 s中可能会存在前导空格、尾随空格或者单词间的多个空格。返回的结果字符串中，单词间应当仅用单个空格分隔，且不包含任何额外的空格。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/reverse-words-in-a-string
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_151_reverseWords {

    @Test
    public void test() {
        System.out.println(reverseWords("a good   example"));
        System.out.println(reverseWords("the sky is blue"));
        System.out.println(reverseWords("  hello world  "));
    }

    public String reverseWords(String s) {
        String[] split = s.split(" ");
        String[] array = new String[split.length];
        int j = 0;
        for (int i = 0; i < split.length; i++) {
            if (split[i].length() > 0) {
                array[j++] = split[i];
            }
        }
        if (j == 1) return array[0];
        
        StringBuilder sb = new StringBuilder();
        sb.append(array[j-1]);
        for (int i = j - 2; i >= 0; i--) {
            sb.append(" ").append(array[i]);
        }
        return sb.toString();
    }
}
