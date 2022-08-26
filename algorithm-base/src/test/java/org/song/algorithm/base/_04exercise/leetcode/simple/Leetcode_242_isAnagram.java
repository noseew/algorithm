package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 242. 有效的字母异位词
 * 给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
 */
public class Leetcode_242_isAnagram {

    @Test
    public void test() {
        System.out.println(isAnagram("anagram", "nagaram"));
        System.out.println(isAnagram("rat", "car"));
        
        System.out.println(isAnagram2("anagram", "nagaram"));
        System.out.println(isAnagram2("rat", "car"));
    }

    /**
     * 采用map计数
     */
    public boolean isAnagram(String s, String t) {
        char[] schars = s.toCharArray();
        char[] tchars = t.toCharArray();

        if (schars.length != tchars.length) {
            return false;
        }

        Map<Character, Integer> map = new HashMap<>(schars.length);
        for (char sc : schars) {
            Integer val = map.getOrDefault(sc, 0);
            map.put(sc, val + 1);
        }
        for (char tc : tchars) {
            Integer val = map.getOrDefault(tc, 0);
            if (val <= 0) {
                return false;
            }
            map.put(tc, val - 1);
        }
        return true;
    }

    /**
     * 由于值范围确定 a-z, 所以采用数组计数
     */
    public boolean isAnagram2(String s, String t) {
        char[] schars = s.toCharArray();
        char[] tchars = t.toCharArray();

        if (schars.length != tchars.length) return false;

        int[] counter = new int['z' - 'a' + 1];

        for (int i = 0; i < schars.length; i++) {
            counter[schars[i] - 'a']++;
        }
        for (int i = 0; i < tchars.length; i++) {
            if (--counter[tchars[i] - 'a'] < 0) {
                return false;
            }
        }
        return true;
    }


}
