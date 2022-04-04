package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 205. 同构字符串
 * 给定两个字符串 s 和 t，判断它们是否是同构的。
 * <p>
 * 如果 s 中的字符可以按某种映射关系替换得到 t ，那么这两个字符串是同构的。
 * <p>
 * 每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。不同字符不能映射到同一个字符上，相同字符只能映射到同一个字符上，字符可以映射到自己本身。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/isomorphic-strings
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_205_isIsomorphic {

    @Test
    public void test() {
        System.out.println(isIsomorphic("egg", "add"));
        System.out.println(isIsomorphic("foo", "bar"));
        System.out.println(isIsomorphic("badc", "baba"));
    }

    /**
     */
    public boolean isIsomorphic(String s, String t) {

        char[] schars = s.toCharArray();
        char[] tchars = t.toCharArray();
        Map<Character, Character> map = new HashMap<>();

        for (int i = 0; i < schars.length; i++) {
            Character character = map.get(schars[i]);
            if (character == null) {
                map.put(schars[i], tchars[i]);
            }else if(character != tchars[i]){
                return false;
            }
        }
        map = new HashMap<>();

        for (int i = 0; i < schars.length; i++) {
            Character character = map.get(tchars[i]);
            if (character == null) {
                map.put(tchars[i], schars[i]);
            }else if(character != schars[i]){
                return false;
            }
        }
        return true;
    }

}
