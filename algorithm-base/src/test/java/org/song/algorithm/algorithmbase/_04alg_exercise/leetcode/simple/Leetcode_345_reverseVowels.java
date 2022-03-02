package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 345. 反转字符串中的元音字母
 * 编写一个函数，以字符串作为输入，反转该字符串中的元音字母。
 */
public class Leetcode_345_reverseVowels {

    @Test
    public void test() {
        System.out.println("holle = " + reverseVowels("hello"));
        System.out.println("leotcede = " + reverseVowels("leetcode"));
        System.out.println("Aa = " + reverseVowels("aA"));
    }

    public String reverseVowels(String s) {
        if (s.length() <= 1) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int head = 0, tail = chars.length - 1; head < tail; ) {
            char hc = chars[head];
            if (!isVowels(hc)) {
                head++;
                continue;
            }
            char tc = chars[tail];
            if (!isVowels(tc)) {
                tail--;
                continue;
            }
            // 交换
            chars[tail] = hc;
            chars[head] = tc;

            head++;
            tail--;
        }
        return new String(chars);
    }

    private boolean isVowels(char c) {
        boolean b = c == 'a'
                || c == 'e'
                || c == 'i'
                || c == 'o'
                || c == 'u';
        return b || c == 'A'
                || c == 'E'
                || c == 'I'
                || c == 'O'
                || c == 'U';
    }

}
