package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 387. 字符串中的第一个唯一字符
 * 给定一个字符串，找到它的第一个不重复的字符，并返回它的索引。如果不存在，则返回 -1。
 */
public class Leetcode_387_firstUniqChar {

    @Test
    public void test() {
        System.out.println("0 = " + firstUniqChar("leetcode"));
        System.out.println("2 = " + firstUniqChar("loveleetcode"));
        System.out.println("-1 = " + firstUniqChar("aabb"));
    }

    /**
     * 思路, 使用 hashmap 计数, 遍历两遍
     */
    public int firstUniqChar(String s) {
        Map<Character, Integer> map = new HashMap<>(s.length());
        char[] chars = s.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            Integer val = map.getOrDefault(chars[i], 0);
            map.put(chars[i], val + 1);
        }
        for (int i = 0; i < chars.length; i++) {
            Integer val = map.getOrDefault(chars[i], 0);
            if (val == 1) {
                return i;
            }
        }
        return -1;
    }

}
