package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 22. 括号生成
 * 数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 有效的 括号组合。
 */
public class Leetcode_22_generateParenthesis {

    @Test
    public void test() {

        List<String> listNode = generateParenthesis(1);
        System.out.println(listNode);

    }

    /**
     * 采用dfs
     */
    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();

        char[] chars = new char[n << 1];
        chars[0] = '(';
        dfs(n - 1, n, chars, res);
        return res;
    }

    private void dfs(int leftCount, int rightCount, char[] chars, List<String> res) {
        if (leftCount == 0 && rightCount == 0) {
            res.add(new String(chars));
            return;
        }

        if (leftCount == rightCount) {
            // 如果剩余数量相同, 只能是拼接(
            chars[chars.length - leftCount - rightCount] = '(';
            dfs(leftCount - 1, rightCount, chars, res);
        } else {
            // 可以拼接(
            if (leftCount > 0) {
                chars[chars.length - leftCount - rightCount] = '(';
                dfs(leftCount - 1, rightCount, chars, res);
            }
            // 也可以拼接)
            if (rightCount > 0) {
                chars[chars.length - leftCount - rightCount] = ')';
                dfs(leftCount, rightCount - 1, chars, res);
            }
        }
    }
}
