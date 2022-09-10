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
 * 
 * 类似题目 46. 全排列
 * 
 */
public class Leetcode_17_letterCombinations {

    @Test
    public void test() {

        System.out.println(Arrays.toString(letterCombinations("23").toArray()));
        results.clear();
        System.out.println(Arrays.toString(letterCombinations("").toArray()));

    }

    char[][] number = {
            {'a', 'b', 'c'}, // 2, index - 2
            {'d', 'e', 'f'}, // 3
            {'g', 'h', 'i'}, // 4
            {'j', 'k', 'l'}, // 5
            {'m', 'n', 'o'}, // 6
            {'p', 'q', 'r', 's'}, // 7
            {'t', 'u', 'v'}, // 8
            {'w', 'x', 'y', 'z'} // 9
    };
    
    char[] chars; // 用户输入的数字, 也就是层
    char[] str; // 用来存储每一层选择的字母
    List<String> results = new ArrayList<>(); // 最终的结果组合

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

    /**
     * 一层一层的递归选择
     */
    private void dfs(int index) {
        if (index == chars.length) {
            // 到了最后一层, 深度结束, 得到了一个结果
            results.add(new String(str));
            return;
        }

        // 当前层所对应的数字 [0-9], 由用户输入
        char digit = chars[index];
        // 每个数字对应的字母表
        char[] letters = number[digit - '2'];
        // 遍历这一层能选择的所有字母
        for (char letter : letters) {
            // 记录当前层的字母
            str[index] = letter;
            // 向下一层继续选择
            dfs(index + 1);
        }
    }

    @Test
    public void test2() {

        System.out.println(Arrays.toString(letterCombinations2("23").toArray()));
        System.out.println(Arrays.toString(letterCombinations2("").toArray()));

    }


    /**
     * 采用 dfs
     */
    public List<String> letterCombinations2(String digits) {
        if (digits == null) return null;
        if (digits.length() == 0) return new ArrayList<>();

        // 用户输入的数字, 也就是层
        char[] chars = digits.toCharArray();
        // 用来存储每一层选择的字母
        char[] str = new char[digits.length()];
        // 最终的结果组合
        List<String> results = new ArrayList<>();
        // 表示层号
        dfs2(0, results, chars, str);
        return results;
    }

    /**
     * 一层一层的递归选择
     */
    private void dfs2(int index, List<String> results, char[] chars, char[] str) {
        if (index == chars.length) {
            // 到了最后一层, 深度结束, 得到了一个结果
            results.add(new String(str));
            return;
        }

        char[] letters = number[ chars[index] - '2'];
        for (char letter : letters) {
            str[index] = letter;
            dfs2(index + 1, results, chars, str);
        }
    }
}
