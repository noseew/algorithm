package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.ListNode;

/**
 * 203. 移除链表元素
 * 给你一个链表的头节点 head 和一个整数 val ，请你删除链表中所有满足 Node.val == val 的节点，并返回 新的头节点 。
 */
public class Leetcode_203_removeElements {

    @Test
    public void test() {
        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(2, new ListNode(1, null))));

        ListNode newHead = removeElements2(headA, 1);
        System.out.println(newHead.val);
    }


    public ListNode removeElements(ListNode head, int val) {

        ListNode newHead = null, prev = null;

        while (head != null) {
            if (head.val == val) {
                if (prev != null) {
                    prev.next = head.next;
                    head = head.next;
                    continue;
                }
            } else if (newHead == null) {
                newHead = head;
            }

            prev = head;
            head = head.next;
        }

        return newHead;
    }


    public ListNode removeElements2(ListNode head, int val) {
        ListNode link = new ListNode(-1, head);
        ListNode node = link;
        while (node.next != null) {
            if (node.next.val == val) {
                node.next = node.next.next;
            } else {
                node = node.next;
            } 
        }
        return link.next;
    }
}
