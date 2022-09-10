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
        print(permute);
    }

    /**
     * DFS 使用成员变量
     */
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
        print(permute);
    }

    /**
     * DFS 使用形参变量
     */
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

    @Test
    public void test3() {
        List<List<Integer>> permute = permute3(new int[]{1, 2, 3});
        print(permute);
    }

    /**
     * DFS 使用形参变量
     * 取消一个参数, 复用现有参数
     */
    public List<List<Integer>> permute3(int[] nums) {
        List<Integer> result = new ArrayList<>();
        // 最终的结果
        List<List<Integer>> results = new ArrayList<>();
        dfs3(0, results, result, nums);
        return results;
    }

    private void dfs3(int index, List<List<Integer>> results, List<Integer> result, int[] selectLevel) {
        if (index == selectLevel.length) {
            results.add(new ArrayList<>(result));
            return;
        }

        for (int i = 0; i < selectLevel.length; i++) {
            if (result.contains(selectLevel[i])) {
                // 如果参数已经存在, 则直接跳过, 等价上面的 selectAble, 不过这里效率变成了O(n), 时间换空间
                continue;
            }
            // 当前层选择的值
            result.add(selectLevel[i]);
            dfs3(index + 1, results, result, selectLevel);
            result.remove(index);
        }
    }


    @Test
    public void test4() {
        List<List<Integer>> permute = permute4(new int[]{1, 2, 3});
        print(permute);
    }

    /**
     * DFS 使用形参变量
     * 采用值替换的思路, 类似于冒泡排序将所有组合换一遍
     * 思路:
     * 原数组: 1,2,3
     * 第一层, 
     * -    值1和1交换(下标0和0交换), 得到第1个值 注意需要换回来
     * -    值1和2交换(下标0和1交换), 得到第2个值
     * -    值1和3交换(下标0和2交换), 得到第3个值
     * 第二层, 
     * -    值2和2交换(下标1和1交换), 得到第1个值
     * -    值2和3交换(下标1和2交换), 得到第2个值
     * 第三层, 
     * -    值3和3交换(下标2和2交换), 得到第1个值
     * 
     * 这样一次DFS下来, 第一个组合就得到了, 以此类推
     * 
     */
    public List<List<Integer>> permute4(int[] nums) {
        // 最终的结果
        List<List<Integer>> results = new ArrayList<>();
        dfs4(0, results, nums);
        return results;
    }

    private void dfs4(int index, List<List<Integer>> results, int[] selectLevel) {
        if (index == selectLevel.length) {
            List<Integer> r = new ArrayList<>();
            for (int i : selectLevel) {
                r.add(i);
            }
            results.add(r);
            return;
        }

        for (int i = index; i < selectLevel.length; i++) {
            // 交换两个地方值
            swap(selectLevel, index, i);
            dfs4(index + 1, results, selectLevel);
            // 换回来
            swap(selectLevel, index, i);
        }
    }
    
    private static void swap(int[] array, int i, int j) {
        if (i == j) return;
        int val = array[i];
        array[i] = array[j];
        array[j] = val;
    }


    private static void print(List<List<Integer>> permute) {
        for (List<Integer> integers : permute) {
            for (Integer integer : integers) {
                System.out.print(" " + integer);
            }
            System.out.println();
        }
    }
}
