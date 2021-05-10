package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.Test;

/**
 * 168. Excel表列名称
 * 给定一个正整数，返回它在 Excel 表中相对应的列名称。
 */
public class Leetcode_168_convertToTitle {

    @Test
    public void test() {
        System.out.println("AAA = " + convertToTitle(703));
        System.out.println("ZY = " + convertToTitle(701));
        System.out.println("AB = " + convertToTitle(28));
        System.out.println("AA = " + convertToTitle(27));
        System.out.println("Z = " + convertToTitle(26));
        System.out.println("A = " + convertToTitle(1));
        System.out.println("AZ = " + convertToTitle(52));

//        System.out.println(703 % 26);
//        System.out.println(703 / 26);
    }

    /**
     * 思路
     * 和171题目反过来
     *
     * 未完成
     */
    public String convertToTitle(int columnNumber) {
        if (columnNumber <= 0) {
            return "";
        }
        if (columnNumber == 1) {
            return parse(columnNumber) + "";
        }
        StringBuilder sb = new StringBuilder();
        int n = columnNumber;
        while (n > 1) {
            sb.append(parse(n % 26));
            n /= 26;
        }
        return sb.reverse().toString();
    }

    private char parse(int c) {
        if (c == 0) {
            return 'Z';
        }
        // 将字符 1 转成 A, 字符 2 转成 B ...
        return (char) (c - 1 + 'A');
    }


}
