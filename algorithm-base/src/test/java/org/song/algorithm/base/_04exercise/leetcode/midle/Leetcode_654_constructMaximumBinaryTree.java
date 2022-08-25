package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.TreeNode;

import java.util.Arrays;
import java.util.Stack;

/**
 * 654. 最大二叉树
 * 给定一个不重复的整数数组 nums 。 最大二叉树 可以用下面的算法从 nums 递归地构建:
 *
 * 创建一个根节点，其值为 nums 中的最大值。
 * 递归地在最大值 左边 的 子数组前缀上 构建左子树。
 * 递归地在最大值 右边 的 子数组后缀上 构建右子树。
 * 返回 nums 构建的 最大二叉树 。
 * 提示：
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 * nums 中的所有整数 互不相同
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/maximum-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_654_constructMaximumBinaryTree {

    @Test
    public void test() {
        TreeNode treeNode = constructMaximumBinaryTree(new int[]{3, 2, 1, 6, 0, 5});
        System.out.println(treeNode);

    }

    /**
     * 由于题目给出的值范围不大, 所以可以使用递归解决
     */
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        if (nums == null || nums.length == 0) return null;
        if (nums.length == 1) return new TreeNode(nums[0]);
        return constructMaximumBinaryTree1(nums, 0, nums.length);
    }

    private TreeNode constructMaximumBinaryTree1(int[] nums, int l, int r) {
        if (l >= r) return null;
        int maxIndex = l;
        for (int i = l; i < r; i++) {
            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }

        TreeNode parent = new TreeNode(nums[maxIndex]);
        parent.left = constructMaximumBinaryTree1(nums, l, maxIndex);
        parent.right = constructMaximumBinaryTree1(nums, maxIndex + 1, r);
        return parent;
    }


    /**
     * 题目变种
     * 返回的不是树的root, 而是返回一个数组, 该数组中的值表示其父节点的索引
     */
    @Test
    public void test2() {
        int[] treeNode = constructMaximumBinaryTree2(new int[]{3, 2, 1, 6, 0, 5});
        System.out.println(Arrays.toString(treeNode));
        int[] treeNode2 = constructMaximumBinaryTree3(new int[]{3, 2, 1, 6, 0, 5});
        System.out.println(Arrays.toString(treeNode2));

    }

    /**
     * 返回最大树, 对应的每个节点的父节点索引
     * <p>
     * 思路: 并不是将其变成树, 而是采用单调栈
     * 最大树的特点 1. 父节点比子节点值要大
     * 对应的数组就是, 最近一个比自己大(如果两个距离相等则找到更小的那个)那个节点
     * 如果没有父节点, 则存储-1
     * <p>
     * 思路2: 暴力法, 暴力找到满足上面条件的值
     */
    public int[] constructMaximumBinaryTree2(int[] nums) {

        // 从栈低到栈顶, 单调递减
        Stack<Integer> stack = new Stack<>();

        // 左边第一个比其小的位置
        int[] larray = new int[nums.length];
        // 右边第一个比其小的位置
        int[] rarray = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {
            rarray[i] = larray[i] = -1;

            if (stack.isEmpty()) {
                // 如果成功push, 则当前 栈顶元素就是新元素左边第一个比其更大的元素
                larray[i] = -1;
                stack.push(i);
            } else if (nums[stack.peek()] > nums[i]) {
                // 形成单调递减栈
                // 如果成功push, 则当前 栈顶元素就是新元素左边第一个比其更大的元素
                larray[i] = stack.peek();
                stack.push(i);
            } else {
                // 新元素比栈顶大, 则弹出栈顶元素
                // 则 弹出的栈顶元素右边的第一个比其大的元素就是当前新元素
                while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) rarray[stack.pop()] = i;

                // 如果成功push, 则当前 栈顶元素就是新元素左边第一个比其更大的元素
                if (stack.isEmpty()) {
                    // 如果成功push, 则当前 栈顶元素就是新元素左边第一个比其更大的元素
                    larray[i] = -1;
                    stack.push(i);
                } else {
                    // 如果成功push, 则当前 栈顶元素就是新元素左边第一个比其更大的元素
                    larray[i] = stack.peek();
                    stack.push(i);
                }
            }
        }

        System.out.println("左边: " + Arrays.toString(larray));
        System.out.println("右边: " + Arrays.toString(rarray));

        // 合并左右

        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            // 如果都为-1, 则取值-1
            if (larray[i] == -1 && rarray[i] == -1) res[i] = -1;

            if (larray[i] == -1) {
                res[i] = rarray[i];
                continue;
            }
            if (rarray[i] == -1) {
                res[i] = larray[i];
                continue;
            }

            // 左边为-1 或 左边更远 或 左边值更大 => 取右边
            if (larray[i] > rarray[i] || nums[larray[i]] > nums[rarray[i]]) {
                res[i] = rarray[i];
                continue;
            }
            // 右边为-1 或 右边更远 或 右边值更大 => 取左边
            if (larray[i] < rarray[i] || nums[larray[i]] < nums[rarray[i]]) {
                res[i] = larray[i];
                continue;
            }
        }

        return res;
    }

    /**
     * constructMaximumBinaryTree2 优化写法
     */
    public int[] constructMaximumBinaryTree3(int[] nums) {

        // 从栈低到栈顶, 单调递减
        Stack<Integer> stack = new Stack<>();

        // 左边第一个比其小的位置
        int[] larray = new int[nums.length];
        // 右边第一个比其小的位置
        int[] rarray = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {
            rarray[i] = larray[i] = -1;
            
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) rarray[stack.pop()] = i;
            larray[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }

        System.out.println("左边: " + Arrays.toString(larray));
        System.out.println("右边: " + Arrays.toString(rarray));

        // 合并左右
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            // 如果都为-1, 则取值-1
            if (larray[i] == -1 && rarray[i] == -1) res[i] = -1;
            
            if (larray[i] == -1) {
                res[i] = rarray[i];
                continue;
            }
            if (rarray[i] == -1) {
                res[i] = larray[i];
                continue;
            }

            // 左边为-1 或 左边更远 或 左边值更大 => 取右边
            if (larray[i] > rarray[i] || nums[larray[i]] > nums[rarray[i]]) {
                res[i] = rarray[i];
                continue;
            }
            // 右边为-1 或 右边更远 或 右边值更大 => 取左边
            if (larray[i] < rarray[i] || nums[larray[i]] < nums[rarray[i]]) {
                res[i] = larray[i];
                continue;
            }
        }

        return res;
    }

}
