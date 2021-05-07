package org.song.algorithm.algorithmbase._02case.leetcode.hard;

import org.junit.jupiter.api.Test;

import java.util.Stack;

/**
 * 32. 最长有效括号
 * 给你一个只包含 '(' 和 ')' 的字符串，找出最长有效（格式正确且连续）括号子串的长度。
 */
public class Leetcode_32_longestValidParentheses {

    @Test
    public void test() {

        System.out.println(longestValidParentheses(""));
    }

    public int longestValidParentheses(String s) {
        if (s.length() == 0) {
            return 0;
        }
        char left = '(';
        char right = ')';

        Stack<Character> stack = new Stack<>();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == right && stack.isEmpty()) {
                continue;
            }
            if (chars[i] == left) {
                stack.push(chars[i]);
            }else  if (chars[i] == right) {
                if (stack.pop() == left) {
                }
            }
        }

        return 0;
    }

}
