package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 46. 全排列 II
 * 给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
 * <p>
 * 类似题目 46. 全排列 升级版
 */
public class Leetcode_47_permuteUnique {


    @Test
    public void test() {
        List<List<Integer>> permute = permuteUnique(new int[]{1, 1, 3});
        print(permute);
    }

    /**
     * DFS 使用形参变量, 减少一个参数 selectAble, 同时效率O(n)
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
     * <p>
     * 这样一次DFS下来, 第一个组合就得到了, 以此类推
     * 
     * 
     * 
     * 不一样的地方是, 有重复数据, 将重复的组合去掉
     * 
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        // 最终的结果
        List<List<Integer>> results = new ArrayList<>();
        dfs(0, results, nums);
        return results;
    }

    private void dfs(int index, List<List<Integer>> results, int[] selectLevel) {
        if (index == selectLevel.length) {
            List<Integer> r = new ArrayList<>();
            for (int i : selectLevel) {
                r.add(i);
            }
            results.add(r);
            return;
        }

        for (int i = index; i < selectLevel.length; i++) {
            if (contain(selectLevel, index, i)) {
                // 过滤重复的
                /*
                |___start(index层)_____i(替换的目标)_____|
                交换的双方是 index 和 i
                如果 index 和 i 之间存在和 i 相等的数字, 则跳过此次的i
                 */
                continue;
            }

            // 交换两个地方值
            swap(selectLevel, index, i);
            dfs(index + 1, results, selectLevel);
            // 换回来
            swap(selectLevel, index, i);
        }
    }

    private boolean contain(int[] selectLevel, int current, int i) {
        for (int j = current; j < i; j++) {
            if (selectLevel[j] == selectLevel[i]) {
                return true;
            }
        }
        return false;
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
