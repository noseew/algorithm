package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 面试题 01.09. 字符串轮转
 * 字符串轮转。给定两个字符串s1和s2，请编写代码检查s2是否为s1旋转而成（比如，waterbottle是erbottlewat旋转后的字符串）。
 */
public class Leetcode_01_09_isFlipedString {

    /**
     */
    @Test
    public void test() {
        
        System.out.println(isFlipedString("waterbottle", "erbottlewat"));
    }

    /**
     * 思路
     * 如果 s2是s1的轮转字符串, 则s2一定是(s1+s1)的子串
     */
    public boolean isFlipedString(String s1, String s2) {
        if (s1 == null || s2 == null) return false;
        if (s1.length() == 0 && s2.length() == 0) return true;
        if (s1.length() == 0 || s2.length() == 0) return false;
        return (s1 + s1).contains(s2);
    }

}
