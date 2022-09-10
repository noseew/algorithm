package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 17. 电话号码的字母组合
 * 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
 * <p>
 * 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_17_letterCombinations {

    @Test
    public void test() {

        System.out.println(Arrays.toString(letterCombinations("23").toArray()));
        results.clear();
        System.out.println(Arrays.toString(letterCombinations("").toArray()));

    }

    char[][] number = {
            {'a', 'b', 'c'},
            {'d', 'e', 'f'},
            {'g', 'h', 'i'},
            {'j', 'k', 'l'},
            {'m', 'n', 'o'},
            {'p', 'q', 'r', 's'},
            {'t', 'u', 'v'},
            {'w', 'x', 'y', 'z'}
    };
    
    char[] chars;
    char[] str; // 用来存储每一层选择的字母
    List<String> results = new ArrayList<>();
    

    /**
     * 采用 dfs
     */
    public List<String> letterCombinations(String digits) {
        if (digits == null) return null;
        if (digits.length() == 0) return new ArrayList<>();
        
        chars = digits.toCharArray();
        str = new char[digits.length()];
        // 表示层号
        dfs(0);
        return results;
    }

    private void dfs(int index) {
        if (index == chars.length) {
            // 到了最后一层, 深度结束, 得到了一个结果
            results.add(new String(str));
            return;
        }

        // 数字字符
        char digit = chars[index];
        // 能做的选择字母表
        char[] letters = number[digit - '2'];
        for (char letter : letters) {
            str[index] = letter;
            dfs(index + 1);
        }
    }
}
