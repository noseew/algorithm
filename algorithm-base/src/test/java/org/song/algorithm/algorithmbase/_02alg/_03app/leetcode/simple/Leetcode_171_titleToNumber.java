package org.song.algorithm.algorithmbase._02alg._03app.leetcode.simple;

import org.junit.jupiter.api.Test;

/**
 * 171. Excel表列序号
 * 给定一个Excel表格中的列名称，返回其相应的列序号。
 */
public class Leetcode_171_titleToNumber {

    @Test
    public void test() {
        System.out.println("1 = " + titleToNumber("A"));
        System.out.println("26 = " + titleToNumber("Z"));
        System.out.println("27 = " + titleToNumber("AA"));
        System.out.println("28 = " + titleToNumber("AB"));
        System.out.println("703 = " + titleToNumber("AAA"));
        System.out.println("701 = " + titleToNumber("ZY"));
        System.out.println("52 = " + titleToNumber("AZ"));
    }

    /**
     * 思路
     * 就是A-Z进制的数, 26进制
     * A = 1
     * Z = 26
     *
     * 速度击败人数有点少
     *
     * @param columnTitle
     * @return
     */
    public int titleToNumber(String columnTitle) {
        char[] chars = columnTitle.toCharArray();
        int num = 0;
        // 26进制
        int scale = 26;
        for (int i = 0; i < chars.length; i++) {
            /*
             第1位 26^0 = 1           * 当前数
             第2位 26^1 = 26          * 当前数
             第3位 26^2 = 26 * 26     * 当前数
             */
            num += Math.pow(scale, i) * parse(chars[chars.length - i - 1]);
        }
        return num;
    }

    private int parse(char c) {
        // 将字符 A 转成 1, 字符 B 转成 2 ...
        return c - 'A' + 1;
    }

}
