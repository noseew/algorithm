package org.song.algorithm.algorithmbase._02alg._03app.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02alg._03app.leetcode.ListNode;

public class Leetcode_206_reverseList {

    @Test
    public void test() {

        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));

        ListNode newHead = reverseList(headA);
        System.out.println(newHead.val);
    }

    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode newHead = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = newHead;
            newHead = head;
            head = next;
        }
        return newHead;
    }
}
