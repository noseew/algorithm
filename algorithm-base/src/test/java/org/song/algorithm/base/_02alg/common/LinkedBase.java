package org.song.algorithm.base._02alg.common;

import org.song.algorithm.base._04exercise.leetcode.ListNode;

public class LinkedBase {

    public void test01() {

    }

    /**
     * 遍历反转链表
     */
    public ListNode reversal1(ListNode head) {
        ListNode newHead = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = newHead;
            newHead = head;
            head = next;
        }
        return newHead;
    }

    /**
     * 递归反转链表 1
     */
    public ListNode reversal2(ListNode head) {
        if (head == null || head.next == null) return head;
        
        ListNode next = head.next;
        head.next = null;
        ListNode newHead = reversal2(next);
        next.next = head;
        return newHead;
    }

    /**
     * 递归反转链表 2
     */
    public ListNode reversal3(ListNode head) {
        if (head == null || head.next == null) return head;
        
        ListNode newHead = reversal3(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

    /**
     * 双指针取中
     */
    public ListNode middle(ListNode head) {
        ListNode slow = head, fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }


}

