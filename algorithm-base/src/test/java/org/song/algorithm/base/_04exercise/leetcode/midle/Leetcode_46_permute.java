package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 46. 全排列
 * 给定一个不含重复数字的数组 nums ，返回其 所有可能的全排列 。你可以 按任意顺序 返回答案。
 * <p>
 * 类似题目 17. 电话号码的字母组合
 */
public class Leetcode_46_permute {

    @Test
    public void test() {
        List<List<Integer>> permute = permute(new int[]{1, 2, 3});
        for (List<Integer> integers : permute) {
            for (Integer integer : integers) {
                System.out.print(" " + integer);
            }
            System.out.println();
        }
    }

    public List<List<Integer>> permute(int[] nums) {
        result = new int[nums.length];
        selectAble = new boolean[nums.length];
        selectLevel = nums;
        Arrays.fill(selectAble, true);

        dfs(0);
        return results;
    }

    // 最终的结果
    List<List<Integer>> results = new ArrayList<>();
    // 每个选择的结果
    int[] result;
    // 数字是否可以选择
    boolean[] selectAble;
    // 选择的值/层
    int[] selectLevel;

    private void dfs(int index) {
        if (index == selectLevel.length) {
            List<Integer> r = new ArrayList<>();
            for (int i : result) {
                r.add(i);
            }
            results.add(r);
            return;
        }

        for (int i = 0; i < selectLevel.length; i++) {
            if (!selectAble[i]) continue;

            // 标记为不可选
            selectAble[i] = false;
            // 当前层选择的值
            result[index] = selectLevel[i];
            dfs(index + 1);
            // 退出后, 恢复现场, 标记为可选
            selectAble[i] = true;
        }
    }

    @Test
    public void test2() {
        List<List<Integer>> permute = permute2(new int[]{1, 2, 3});
        for (List<Integer> integers : permute) {
            for (Integer integer : integers) {
                System.out.print(" " + integer);
            }
            System.out.println();
        }
    }

    public List<List<Integer>> permute2(int[] nums) {
        int[] result = new int[nums.length];
        boolean[] selectAble = new boolean[nums.length];
        Arrays.fill(selectAble, true);

        // 最终的结果
        List<List<Integer>> results = new ArrayList<>();
        dfs2(0, results, result, selectAble, nums);
        return results;
    }

    private void dfs2(int index, List<List<Integer>> results, int[] result, boolean[] selectAble, int[] selectLevel) {
        if (index == selectLevel.length) {
            List<Integer> r = new ArrayList<>();
            for (int i : result) {
                r.add(i);
            }
            results.add(r);
            return;
        }

        for (int i = 0; i < selectLevel.length; i++) {
            if (!selectAble[i]) continue;

            // 标记为不可选
            selectAble[i] = false;
            // 当前层选择的值
            result[index] = selectLevel[i];
            dfs2(index + 1, results, result, selectAble, selectLevel);
            // 退出后, 恢复现场, 标记为可选
            selectAble[i] = true;
        }
    }
}
