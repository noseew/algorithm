package org.song.algorithm.algorithmbase._02alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02alg._02case.leetcode.ListNode;

/**
 * 剑指 Offer 24. 反转链表
 * 定义一个函数，输入一个链表的头节点，反转该链表并输出反转后链表的头节点。
 */
public class Leetcode_offer24_reverseList {

    @Test
    public void test() {

//        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));
        ListNode head = new ListNode(1, null);

        ListNode node = getKthFromEnd(head);

        System.out.println(node);
    }

    public ListNode getKthFromEnd(ListNode head) {
        /*
         两种方式
         1. 循环
         2. 递归,
         具体实现参见另一个类
         */
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
