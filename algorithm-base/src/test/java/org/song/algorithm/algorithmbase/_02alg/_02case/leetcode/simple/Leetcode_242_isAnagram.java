package org.song.algorithm.algorithmbase._02alg._02case.leetcode.simple;

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
    }

    /**
     *
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


}
