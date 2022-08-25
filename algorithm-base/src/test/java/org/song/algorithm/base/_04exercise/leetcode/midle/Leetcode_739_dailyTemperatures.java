package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Stack;

/**
 * 739. 每日温度
 * <p>
 * 给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer ，其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。如果气温在这之后都不会升高，请在该位置用 0 来代替。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/daily-temperatures
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_739_dailyTemperatures {

    @Test
    public void test() {
        int[] treeNode = constructMaximumBinaryTree(new int[]{3, 2, 1, 6, 0, 5});
        System.out.println(Arrays.toString(treeNode));

        int[] treeNode2 = constructMaximumBinaryTree(new int[]{73, 74, 75, 71, 69, 72, 76, 73});
        System.out.println(Arrays.toString(treeNode2));

        int[] treeNode3 = constructMaximumBinaryTree(new int[]{30, 40, 50, 60});
        System.out.println(Arrays.toString(treeNode3));

        int[] treeNode4 = constructMaximumBinaryTree(new int[]{30, 60, 90});
        System.out.println(Arrays.toString(treeNode4));
    }

    /**
     * 思路1, 利用单调栈
     * 参考 Leetcode_654_constructMaximumBinaryTree 拓展
     * org.song.algorithm.base._04exercise.leetcode.midle.Leetcode_654_constructMaximumBinaryTree#constructMaximumBinaryTree3(int[])
     * 找到数组右边第一个比他大的值的索引, 和当前索引的差
     *
     * @param temperatures
     * @return
     */
    public int[] constructMaximumBinaryTree(int[] temperatures) {
        if (temperatures == null || temperatures.length == 0) {
            return null;
        }

        // 从栈低到栈顶, 单调递增
        Stack<Integer> stack = new Stack<>();
        // 右边第一个比其大的位置
        int[] rarray = new int[temperatures.length];

        for (int i = 0; i < temperatures.length; i++) {
            // 这里不能是 <=,因为相等不用弹出, 相等并不是第一个比其大的
            while (!stack.isEmpty() && temperatures[stack.peek()] < temperatures[i]) {
                // 这里存的是, 索引差
                Integer pop = stack.pop();
                rarray[pop] = i - pop;
            }
            stack.push(i);
        }
        return rarray;
    }

    /**
     * 思路2, 采用类似动态规划思想 TODO
     */
    public int[] constructMaximumBinaryTree2(int[] temperatures) {
        if (temperatures == null || temperatures.length == 0) {
            return null;
        }
        // 右边第一个比其大的位置
        int[] rarray = new int[temperatures.length];
        return rarray;
    }

}
