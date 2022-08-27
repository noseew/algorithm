package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

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
        sb.append(array[j - 1]);
        for (int i = j - 2; i >= 0; i--) {
            sb.append(" ").append(array[i]);
        }
        return sb.toString();
    }

    @Test
    public void test2() {
        System.out.println(reverseWords2("a good   example"));
        System.out.println(reverseWords2("the sky is blue"));
        System.out.println(reverseWords2("  hello world  "));
    }

    /**
     * 进阶：如果字符串在你使用的编程语言中是一种可变数据类型，请尝试使用 O(1) 额外空间复杂度的 原地 解法。
     * 
     * java中采用数组表示字符串, 这里模拟这个数组不变
     * 
     * 思路
     * 1. 去空格, 将单词左移
     * 2. 全部镜像翻转(逆序), 将有效的单词范围逆序
     * 3. 将单个单词逆序
     * 完成
     */
    public String reverseWords2(String s) {

        char[] chars = s.toCharArray();

        int l = 0; // 表示长度
        // 是否允许为空格, 考虑到开头或其他地方连续空格的情况要去除
        boolean space = false;
        for (int r = 0; r < chars.length; r++) {
            if (chars[r] == ' ') {
                if (!space) continue;
                space = false;
            } else {
                space = true;
            }
            chars[l++] = chars[r];
        }
        // 去掉最后一个空格
        if (l < chars.length && chars[l - 1] == ' ') l--;

        reverse(chars, 0, l - 1);

        int start = -1;
        for (int end = 0; end < l; end++) {
            if (chars[end] != ' ') continue;
            reverse(chars, start + 1, end - 1);
            start = end;
        }
        // 翻转最后一个单词
        reverse(chars, start + 1, l - 1);

        return new String(chars, 0, l);
    }
    
    private void reverse(char[] chars, int l, int r) {
        while (l < r) {
            char lc = chars[l];
            chars[l] = chars[r];
            chars[r] = lc;
            l++;
            r--;
        }
    }
    
}
