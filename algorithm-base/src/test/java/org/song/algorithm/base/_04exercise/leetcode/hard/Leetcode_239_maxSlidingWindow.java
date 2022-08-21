package org.song.algorithm.base._04exercise.leetcode.hard;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * 239. 滑动窗口最大值
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * <p>
 * 返回 滑动窗口中的最大值 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/sliding-window-maximum
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_239_maxSlidingWindow {

    @Test
    public void test() {
        System.out.println(Arrays.toString(maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }

    /**
     * 要求是O(n)
     * 解法思路相当于最小栈升级版, 都是将路过的最值记录下来, 不过这里需要将溢出的值出队
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) return null;
        if (k == 1) return nums;

        // 窗口内最大值数组
        int[] maxArray = new int[nums.length - k + 1];
        // 双向队列, 用于存放窗口内的值, 从左往右, 从大到小排序
        LinkedList<Integer> queue = new LinkedList<>();

        for (int end = 0, start = end - k + 1; end < nums.length; end++, start++) {
            if (start >= 0) {
                // 将左侧, 超出窗口的值移除
                while (!queue.isEmpty() && queue.peekFirst() < start) queue.pollFirst();
            }

            // 将右侧, 小于当前的值移除, 因为要留下最大值
            while (!queue.isEmpty()) {
                if (nums[queue.peekLast()] <= nums[end]) {
                    queue.pollLast();
                } else {
                    break;
                }
            }

            queue.addLast(end);
//            System.out.println(queue.toString());
            if (start >= 0) {
                // 从左侧取出最大值, 也就是窗口内的最大值, 放入新的队列
                maxArray[start] = nums[queue.peekFirst()];
            }
        }

        return maxArray;
    }

    @Test
    public void test2() {
        System.out.println(Arrays.toString(maxSlidingWindow2(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }

    /**
     * 思路和 maxSlidingWindow 一致, 简化代码
     */
    public int[] maxSlidingWindow2(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) return null;
        if (k == 1) return nums;

        // 窗口内最大值数组
        int[] maxArray = new int[nums.length - k + 1];
        // 双向队列, 用于存放窗口内的值, 从左往右, 从大到小排序
        LinkedList<Integer> queue = new LinkedList<>();
        queue.addLast(0);

        for (int end = 1, start = end - k + 1; end < nums.length; end++, start++) {
            // 将右侧, 小于当前的值移除, 因为要留下最大值
            while (!queue.isEmpty() && nums[queue.peekLast()] <= nums[end]) queue.pollLast();

            queue.addLast(end);
//            System.out.println(queue.toString());
            if (start >= 0) {
                // 将左侧, 超出窗口的值移除
                if (queue.peekFirst() < start) queue.pollFirst();
                // 从左侧取出最大值, 也就是窗口内的最大值, 放入新的队列
                maxArray[start] = nums[queue.peekFirst()];
            }
        }

        return maxArray;
    }

    @Test
    public void test3() {
        System.out.println(Arrays.toString(maxSlidingWindow3(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3)));
    }

    /**
     * 思路和 maxSlidingWindow2 一致, 自己实现队列, TODO 未完成
     */
    public int[] maxSlidingWindow3(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k < 1 || k > nums.length) return null;
        if (k == 1) return nums;

        // 窗口内最大值数组
        int[] maxArray = new int[nums.length - k + 1];
        // 双向队列, 用于存放窗口内的值, 从左往右, 从大到小排序
        DQ queue = new DQ();
        queue.addLast(0);

        for (int end = 0, start = end - k + 1; end < nums.length; end++, start++) {
            // 将右侧, 小于当前的值移除, 因为要留下最大值
            while (!queue.isEmpty() && nums[queue.peekLast()] <= nums[end]) queue.pollLast();

            queue.addLast(end);
//            System.out.println(queue.toString());
            if (start >= 0) {
                // 将左侧, 超出窗口的值移除
                while (queue.peekFirst() < start) queue.pollFirst();
                // 从左侧取出最大值, 也就是窗口内的最大值, 放入新的队列
                maxArray[start] = nums[queue.peekFirst()];
            }
        }

        return maxArray;
    }

    class DQ {

        Node first, last;
        int size;

        DQ() {
            first = new Node(Integer.MIN_VALUE, null, null);
        }

        boolean isEmpty() {
            return size == 0;
        }

        void addLast(int val) {
            if (last != null) {
                last.last = new Node(val, last, null);
            } else {
                last = new Node(val, first, null);
            }
            size++;
        }

        int peekFirst() {
            return first.val;
        }

        int pollFirst() {
            Node f = this.first;
            Node newFirst = this.first.last;
            removeNode(f);
            this.first = newFirst;
            size--;
            return f.val;
        }

        int peekLast() {
            return last.val;
        }

        int pollLast() {
            Node l = this.last;
            Node newLast = this.last.first;
            removeNode(l);
            this.last = newLast;
            size--;
            return l.val;
        }
        
        Node removeNode(Node node) {
            if (node == null) return null;
            if (node.first != null) node.first.last = node.last;
            if (node.last != null) node.last.first = node.first;
            return node;
        }

        class Node {
            int val;
            Node first, last;

            public Node(int val, Node first, Node last) {
                this.val = val;
                this.first = first;
                this.last = last;
            }
        }
    }

}
