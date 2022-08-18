package org.song.algorithm.base._01datatype._01base._03string_array_matrix._01string.alg;

import org.junit.jupiter.api.Test;

public class PatternMatcher {
    
    /*
    字符串匹配算法
    
    - 暴力(BF)
        逐个匹配对比, 复杂度 O(m*n) m为主串长度, n为子串长度
    - Rabin-Karp(RK)
        hash方法对比, 复杂度 O(m+n) m为主串长度, n为子串长度
        特点在于, hash方法的计算复杂度优化到O(m)
    - KMP
        经典字符串匹配
        重复利用已经匹配过的字符, 
    - Boyer-Moore(BM)
        坏字符规则和好后缀规则
    - Sunday
        失配之后, 判断主串多出的下一个字符
    - AC自动机
     */


    @Test
    public void test01() {
        String text = "ABCABCDEF";
        assert bf(text, "AB") >= 0;
        assert bf(text, "BC") >= 0;
        assert bf(text, "ABCD") >= 0;
        assert bf(text, "F") >= 0;
        assert bf(text, "ABB") == -1;
        assert bf(text, "G") == -1;
    }

    /**
     * 暴力解法
     */
    private int bf(String text, String pattern) {
        if (text == null || pattern == null) return -1;
        if (text.length() == 0 || pattern.length() == 0) return -1;
        if (pattern.length() > text.length()) return -1;

        char[] t = text.toCharArray();
        char[] p = pattern.toCharArray();

        int pi = 0, ti = 0, l = t.length - p.length;
        while (pi < p.length && ti - pi <= l) {
            if (t[ti] == p[pi]) {
                // 相等都+1
                ++pi;
                ++ti;
            } else {
                // 不等则复位
                ti -= pi - 1; // ti 前进1位
                pi = 0; // pi 恢复0
            }
        }
        return pi == p.length ? ti - pi : -1;
    }


    @Test
    public void test02() {
        String text = "ABCABCDEF";
        assert kmp(text, "AB") >= 0;
        assert kmp(text, "BC") >= 0;
        assert kmp(text, "ABCD") >= 0;
        assert kmp(text, "F") >= 0;
        assert kmp(text, "ABB") == -1;
        assert kmp(text, "G") == -1;
    }

    /**
     * KMP解法
     */
    private int kmp(String text, String pattern) {
        if (text == null || pattern == null) return -1;
        if (text.length() == 0 || pattern.length() == 0) return -1;
        if (pattern.length() > text.length()) return -1;

        char[] t = text.toCharArray();
        char[] p = pattern.toCharArray();
        int[] next = next(p);

        int pi = 0, ti = 0, l = t.length - p.length;
        while (pi < p.length && ti - pi <= l) {
            if (pi == -1 || t[ti] == p[pi]) {
                // 相等都+1
                ++pi;
                ++ti;
            } else {
                // 不等则前进next
                pi = next[pi];
            }
        }
        return pi == p.length ? ti - pi : -1;
    }

    private int[] next(char[] p) {
        int[] next = new int[p.length];
        next[0] = -1;
        int j = 0;
        int k = -1;
        while (j < p.length - 1) {
            if (k == -1 || p[j] == p[k]) {
                if (p[++j] == p[++k]) { // 当两个字符相等时要跳过
                    next[j] = next[k];
                } else {
                    next[j] = k;
                }
            } else {
                k = next[k];
            }
        }
        return next;
    }

}
