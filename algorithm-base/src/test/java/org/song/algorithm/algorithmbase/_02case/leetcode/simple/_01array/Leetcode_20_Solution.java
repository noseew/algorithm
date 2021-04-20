package org.song.algorithm.algorithmbase._02case.leetcode.simple._01array;

import org.junit.Test;

import java.util.*;

/**
 * 有效的括号
 */
public class Leetcode_20_Solution {

    @Test
    public void test(){
        System.out.println("print: " + isValid("()[]{}"));
        System.out.println("print: " + isValid("{[]}"));
        System.out.println("print: " + isValid("([)]"));
    }


    public boolean isValid(String s) {
        if (s == null) {
            return false;
        }
        if ("".equals(s)) {
            return true;
        }

        List<String> left = Arrays.asList("(", "[", "{", "<");
        List<String> right = Arrays.asList(")", "]", "}", ">");
        Map<String, String> map = new HashMap<>();
        map.put("(", ")");
        map.put("[", "]");
        map.put("{", "}");
        map.put("<", ">");

//        Set<String> keys = map.keySet();
//        Collection<String> values = map.values();

        char[] chars = s.toCharArray();
        ArrayDeque<String> arrayDeque = new ArrayDeque<>();
        for (int i = 0; i < chars.length; i++) {
            String c = String.valueOf(chars[i]);
            if (left.contains(c)) {
                arrayDeque.addFirst(c);
            }
            if (right.contains(c)) {
                if (arrayDeque.isEmpty()) {
                    return false;
                }
                if (!map.get(arrayDeque.pollFirst()).equals(c)) {
                    return false;
                }
            }
        }
        return arrayDeque.isEmpty();
    }

}
