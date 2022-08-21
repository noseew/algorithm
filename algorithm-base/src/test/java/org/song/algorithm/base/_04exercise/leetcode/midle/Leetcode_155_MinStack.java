package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;

import java.util.Stack;

/**
 * 155. 最小栈
 * 设计一个支持 push ，pop ，top 操作，并能在常数时间内检索到最小元素的栈。
 *
 * 实现 MinStack 类:
 *
 * MinStack() 初始化堆栈对象。
 * void push(int val) 将元素val推入堆栈。
 * void pop() 删除堆栈顶部的元素。
 * int top() 获取堆栈顶部的元素。
 * int getMin() 获取堆栈中的最小元素。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/min-stack
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_155_MinStack {

    @Test
    public void test() {
        MinStack1 minStack = new MinStack1();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin()); // --> 返回 -3.
        minStack.pop();
        System.out.println(minStack.top()); //  --> 返回 0.
        System.out.println(minStack.getMin()); // --> 返回 -2.
    }

    /**
     * 要求O(1)级别
     * 思路1
     * 采用两个栈, 1个是正常栈, 用来实现常规栈操作
     * 另个一是最小值, 用来存放每次push后的最小值, 该栈中的值一定是降序的
     * 例如
     * 正常栈push后
     * 尾 [3,1,4,2]
     * 最小栈push后
     * 尾 [3,1,1,1], 只有比原来更小的值, 才会push进入, 正常栈每次pop时, 跟着pop
     * 
     */
    class MinStack1 {

        Stack<Integer> stack = new Stack<>();
        Stack<Integer> min = new Stack<>();

        public MinStack1() {

        }

        public void push(int val) {
            stack.push(val);
            if (min.isEmpty() || val <= min.peek()) {
                min.push(val);
            } else {
                min.push(min.peek());
            }
        }

        public void pop() {
            min.pop();
            stack.pop();
        }

        public int top() {
            return stack.peek();
        }

        public int getMin() {
            return min.peek();
        }
    }


    @Test
    public void test2() {
        MinStack2 minStack = new MinStack2();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.getMin()); // --> 返回 -3.
        minStack.pop();
        System.out.println(minStack.top()); //  --> 返回 0.
        System.out.println(minStack.getMin()); // --> 返回 -2.
    }

    /**
     * 思路2, 和思路1相同, 不过两个栈合并成一个栈
     */
    class MinStack2 {

        Stack<Node> stack = new Stack<>();

        public MinStack2() {

        }

        public void push(int val) {
            if (stack.isEmpty() || stack.peek().min > val) {
                stack.push(new Node(val, val));
            } else {
                stack.push(new Node(val, stack.peek().min));
            }
        }

        public void pop() {
            stack.pop();
        }

        public int top() {
            return stack.peek().val;
        }

        public int getMin() {
            return stack.peek().min;
        }
        
        class Node {
            int val;
            int min;
            public Node(int val, int min) {
                this.val = val;
                this.min = min;
            }
        }
    }
}
