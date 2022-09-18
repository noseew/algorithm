package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 46. 全排列 II
 * 给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
 * <p>
 * 类似题目 46. 全排列 升级版
 */
public class Leetcode_49_groupAnagrams {

    /**
     * 未完成
     */
    @Test
    public void test() {
        List<List<String>> permute = groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"});
        System.out.println(permute);
    }

    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            boolean add = false;
            for (Map.Entry<String, List<String>> listEntry : map.entrySet()) {
                if (listEntry.getKey().contains(str)) {
                    listEntry.getValue().add(str);
                    add = true;
                    break;
                }
            }
            if (!add) {
                List<String> list = new ArrayList<>();
                list.add(str);
                map.put(str + str, list);
            }
        }
        map.forEach((k, v) -> res.add(v));
        return res;
    }
}
