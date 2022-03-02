package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 罗马数字转整数
 */
public class Leetcode_13_RomanToIntTest {

    @Test
    public void romanToInt() {
        System.out.println(test02("MCDLXXVI"));
        System.out.println(test01("MCDLXXVI"));
    }


    public int test02(String s) {
        Map<String, Integer> map = new HashMap<>();
        map.put("I", 1);
        map.put("V", 5);
        map.put("X", 10);
        map.put("L", 50);
        map.put("C", 100);
        map.put("D", 500);
        map.put("M", 1000);
        map.put("IV", 4);
        map.put("IX", 9);
        map.put("XL", 40);
        map.put("XC", 90);
        map.put("CD", 400);
        map.put("CM", 900);
        if (s == null || s.length() == 0) {
            return 0;
        }
        int c = 0;
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                Integer c1 = map.getOrDefault(chars[i] + "" + chars[i + 1], 0);
                if (c1 > 0) {
                    c += c1;
                    i++;
                    continue;
                }
            }
            c += map.getOrDefault(chars[i] + "", 0);
        }
        return c;
    }

    private int parse(char c) {
        switch (c) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            case 'D': return 500;
            case 'M': return 1000;

            case 'a': return 4 ;
            case 'b': return 9 ;
            case 'c': return 40 ;
            case 'd': return 90 ;
            case 'e': return 400 ;
            case 'f': return 900 ;
        }
        return 0;
    }

    private char parse2(String c) {
        switch (c) {
            case "IV": return 'a';
            case "IX": return 'b';
            case "XL": return 'c';
            case "XC": return 'd';
            case "CD": return 'e';
            case "CM": return 'f';
        }
        return '0';
    }

    /**
     * 官网/网上 思路, 使用 switch 速度更快
     *
     * @param s
     * @return
     */
    public int test01(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int c = 0;
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (i != chars.length - 1) {
                char c2 = parse2(aChar + "" + chars[i + 1]);
                if (c2 != '0') {
                    c += parse(c2);
                    i++;
                    continue;
                }
            }
            c += parse(aChar);

        }
        return c;
    }

}
