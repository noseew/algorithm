package org.song.algorithm.algorithmbase._02case.leetcode.simple.stack;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 225. 用队列实现栈
 */
public class Leetcode_225_MyStack {
    /**
     * 未完成
     */
    @Test
    public void test() {
        MyStack obj = new MyStack();
        obj.push(1);
        int param_2 = obj.pop();
        System.out.println(param_2);
        int param_3 = obj.top();
        System.out.println(param_3);
        boolean param_4 = obj.empty();
        System.out.println(param_4);
    }

    class MyStack {

        Queue<Integer> queue1 = new LinkedList<>();
        Queue<Integer> queue2 = new LinkedList<>();
        int dataIn = 1;

        /**
         * Initialize your data structure here.
         */
        public MyStack() {

        }

        /**
         * Push element x onto stack.
         */
        public void push(int x) {
            if (dataIn == 1) {
                queue1.add(x);
            } else {
                queue2.add(x);
            }
        }

        /**
         * Removes the element on top of the stack and returns that element.
         */
        public int pop() {
            if (dataIn == 1) {
                while (!queue1.isEmpty()) {
                    queue2.add(queue1.poll());
                }
                dataIn = 2;
                return queue2.poll();
            } else {
                while (!queue2.isEmpty()) {
                    queue1.add(queue2.poll());
                }
                dataIn = 1;
                return queue1.poll();
            }
        }

        /**
         * Get the top element.
         */
        public int top() {
            if (dataIn == 1) {
                while (!queue1.isEmpty()) {
                    queue2.add(queue1.poll());
                }
                dataIn = 2;
                return queue2.peek();
            } else {
                while (!queue2.isEmpty()) {
                    queue1.add(queue2.poll());
                }
                dataIn = 1;
                return queue1.peek();
            }
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return queue1.isEmpty() && queue2.isEmpty();
        }
    }
}
