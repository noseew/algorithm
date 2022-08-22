package org.song.algorithm.base._02alg.common;

public class LinkedBase {

    public void test01() {

    }

    /**
     * 遍历反转链表
     */
    public ListNode reversal1(ListNode node) {
        return node;
    }

    /**
     * 递归反转链表
     */
    public ListNode reversal2(ListNode node) {
        return node;
    }

    /**
     * 双指针取中
     */
    public ListNode middle(ListNode node) {
        return node;
    }


    static class ListNode {

        public int val;
        public ListNode next;

        public ListNode(int x, ListNode next) {
            this.val = x;
            this.next = next;
        }
    }
}
