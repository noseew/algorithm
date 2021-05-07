package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 225. 用队列实现栈
 * 队列是 先进先出
 * 栈是 先进后出
 */
public class Leetcode_225_MyStack {

    @Test
    public void test() {
        MyStack obj = new MyStack();
//        obj.push(1);
//        Integer param_2 = obj.pop();
//        System.out.println(param_2);
//        Integer param_3 = obj.top();
//        System.out.println(param_3);
//        boolean param_4 = obj.empty();
//        System.out.println(param_4);

        System.out.println();
        for (int i = 0; i < 2; i++) {
            obj.push(i);
        }

        for (int i = 0; i < 2; i++) {
            System.out.println("top " + obj.top());
            System.out.println("pop " + obj.pop());
        }
    }

    /**
     * 平均 O(n)
     */
    class MyStack {

        Queue<Integer> queue1 = new LinkedList<>();
        int queue1Size = 0;
        Queue<Integer> queue2 = new LinkedList<>();
        int queue2Size = 0;
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
            Queue<Integer> queue = getDataQueue();
            addSize();
            queue.add(x);
        }

        /**
         * Removes the element on top of the stack and returns that element.
         */
        public Integer pop() {
            Integer last = getQueueLast(true);
            decSize();
            return last;
        }

        /**
         * Get the top element.
         */
        public Integer top() {
            return getQueueLast(false);
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return queue1.isEmpty() && queue2.isEmpty();
        }

        private void addSize() {
            if (dataIn == 1) {
                queue1Size++;
            } else {
                queue2Size++;
            }
        }

        private void decSize() {
            if (dataIn == 1) {
                queue1Size--;
            } else {
                queue2Size--;
            }
        }

        private Queue<Integer> getDataQueue() {
            if (dataIn == 1) {
                return queue1;
            }
            return queue2;
        }

        /**
         * 获取队列最后一个元素, 用户模拟栈的后进先出
         *
         * @param isPop 是否取出元素
         * @return
         */
        private Integer getQueueLast(boolean isPop) {

            if (dataIn == 1) {
                for (int i = 1; i < queue1Size; i++) {
                    queue2.add(queue1.poll());
                }
                dataIn = 2;

                Integer last = queue1.poll();
                if (isPop) {
                    queue2Size = queue1Size--;
                } else {
                    queue2.add(last);
                    queue2Size = queue1Size;
                }
                queue1Size = 0;
                return last;
            } else {
                for (int i = 1; i < queue2Size; i++) {
                    queue1.add(queue2.poll());
                }
                dataIn = 1;
                Integer last = queue2.poll();
                if (isPop) {
                    queue1Size = queue2Size--;
                } else {
                    queue1.add(last);
                    queue1Size = queue2Size;
                }
                queue2Size = 0;
                return last;
            }
        }
    }

    @Test
    public void test2() {
        MyStack2 obj = new MyStack2();

        System.out.println();
        for (int i = 0; i < 3; i++) {
            obj.push(i);
        }

        for (int i = 0; i < 3; i++) {
            System.out.println("top " + obj.top());
//            System.out.println("pop " + obj.pop());
        }
    }

    /**
     * 平均 O(n)
     */
    class MyStack2 {

        Queue<Integer> queue1 = new LinkedList<>();
        Queue<Integer> queue2 = new LinkedList<>();
        int dataIn = 1;

        /**
         * Initialize your data structure here.
         */
        public MyStack2() {

        }

        /**
         * Push element x onto stack.
         * <p>
         * O(1)
         */
        public void push(int x) {
            Queue<Integer> queue = getDataQueue();
            queue.add(x);
        }

        /**
         * Removes the element on top of the stack and returns that element.
         * <p>
         * O(n)
         */
        public Integer pop() {
            return getQueueLast(true);
        }

        /**
         * Get the top element.
         * O(n)
         */
        public Integer top() {
            return getQueueLast(false);
        }

        /**
         * Returns whether the stack is empty.
         */
        public boolean empty() {
            return queue1.isEmpty() && queue2.isEmpty();
        }

        private Queue<Integer> getDataQueue() {
            if (dataIn == 1) {
                return queue1;
            }
            return queue2;
        }

        /**
         * 获取队列最后一个元素, 用户模拟栈的后进先出
         *
         * @param isPop 是否取出元素
         * @return
         */
        private Integer getQueueLast(boolean isPop) {

            if (dataIn == 1) {
                dataIn = 2;
                Integer last = null;
                while (queue1.size() > 1) {
                    last = queue1.poll();
                    queue2.add(last);
                }
                last = queue1.poll();
                if (!isPop) {
                    queue2.add(last);
                }
                return last;
            } else {
                dataIn = 1;
                Integer last = null;
                while (queue2.size() > 1) {
                    last = queue2.poll();
                    queue1.add(last);
                }
                last = queue2.poll();
                if (!isPop) {
                    queue1.add(last);
                }
                return last;
            }
        }
    }
}
