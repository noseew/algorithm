package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.Stack;

/**
 * 232. 用栈实现队列
 * 栈是 先进后出
 * 队列是 先进先出
 */
public class Leetcode_232_MyQueue {

    @Test
    public void test() {

        MyQueue queue = new MyQueue();

        for (int i = 0; i < 3; i++) {
            queue.push(i);
        }

        for (int i = 0; i < 3; i++) {
            System.out.println("peek : "+queue.peek());
            System.out.println("pop : "+queue.pop());
        }
        // ["MyQueue","push","push","push","push","pop","push","pop","pop","pop","pop"]
        // [[],[1],[2],[3],[4],[],[5],[],[],[],[]]

//        for (int i = 1; i < 5; i++) {
//            queue.push(i);
//        }
//        System.out.println("pop : " + queue.pop());
//        queue.push(5);
//        for (int i = 0; i < 4; i++) {
//            System.out.println("pop : " + queue.pop());
//        }
    }

    class MyQueue {

        Stack<Integer> stack1 = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();
        int dataIn = 1;
        boolean lastOpAdd = false;

        /**
         * Initialize your data structure here.
         */
        public MyQueue() {

        }

        /**
         * Push element x to the back of queue.
         */
        public void push(int x) {
            inversion(true);
            Stack<Integer> dataStack = getDataStack();
            lastOpAdd = true;
            dataStack.push(x);
        }

        /**
         * Removes the element from in front of queue and returns that element.
         */
        public Integer pop() {
            inversion(false);
            Stack<Integer> dataStack = getDataStack();
            lastOpAdd = false;
            return dataStack.pop();
        }

        /**
         * Get the front element.
         */
        public Integer peek() {
            inversion(false);
            Stack<Integer> dataStack = getDataStack();
            lastOpAdd = false;
            return dataStack.peek();
        }

        /**
         * Returns whether the queue is empty.
         */
        public boolean empty() {
            return stack1.size() == 0 && stack2.size() == 0;
        }

        private Stack<Integer> getDataStack() {
            if (dataIn == 1) {
                return stack1;
            }
            return stack2;
        }

        private boolean needExchange(boolean currentOpIsAdd) {
            return currentOpIsAdd != lastOpAdd;
        }

        private void inversion(boolean currentOpIsAdd) {
            if (!needExchange(currentOpIsAdd)) {
                return;
            }
            if (dataIn == 1) {
                dataIn = 2;
                while (stack1.size() > 0) {
                    stack2.add(stack1.pop());
                }
            } else {
                dataIn = 1;
                while (stack2.size() > 0) {
                    stack1.add(stack2.pop());
                }
            }
        }
    }
}
