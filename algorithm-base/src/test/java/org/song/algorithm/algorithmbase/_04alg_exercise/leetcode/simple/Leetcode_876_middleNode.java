package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._04alg_exercise.leetcode.ListNode;

/**
 * 876. 链表的中间结点
 * 给定一个头结点为 head 的非空单链表，返回链表的中间结点。
 * 如果有两个中间结点，则返回第二个中间结点。
 */
public class Leetcode_876_middleNode {

    @Test
    public void test() {

        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));

        ListNode listNode = middleNode(headA);
        System.out.println(listNode.val);
    }

    public ListNode middleNode(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }
}
