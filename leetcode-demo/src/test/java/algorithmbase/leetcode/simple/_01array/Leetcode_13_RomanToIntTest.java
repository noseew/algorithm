package algorithmbase.leetcode.simple._01array;

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

    public int test01(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);
        map.put('L', 50);
        map.put('C', 100);
        map.put('D', 500);
        map.put('M', 1000);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("IV", 4);
        map2.put("IX", 9);
        map2.put("XL", 40);
        map2.put("XC", 90);
        map2.put("CD", 400);
        map2.put("CM", 900);

        if (s == null || s.length() == 0) {
            return 0;
        }
        int c = 0;
        char[] chars = s.toCharArray();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (i != chars.length - 1) {
                Integer c1 = map2.getOrDefault(aChar + "" + chars[i + 1], 0);
                if (c1 > 0) {
                    sb.append("+").append(aChar + "" + chars[i + 1]).append(":").append(c1);
                    c += c1;
                    i++;
                    continue;
                }
            }
            Integer c2 = map.getOrDefault(aChar, 0);
            sb.append("+").append(aChar).append(":").append(c2);
            c += c2;

        }
        System.out.println(sb);
        return c;
    }

}
