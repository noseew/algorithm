package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02case.leetcode.ListNode;

/**
 * 剑指 Offer 18. 删除链表的节点
 * <p>
 * 原链表和返回链表都要删除
 * 给定单向链表的头指针和一个要删除的节点的值，定义一个函数删除该节点。
 * 返回删除后的链表的头节点。
 * 注意：此题对比原题有改动
 */
public class Leetcode_offer18_deleteNode {

    @Test
    public void test() {

//        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));
        ListNode headA = new ListNode(4, null);

        ListNode ints = deleteNode(headA, 4);
        System.out.println(ints);
    }

    public ListNode deleteNode(ListNode head, int val) {
        ListNode newHead = null, prev = null;
        /*
         newHead 正常遍历删除, 拼接不用删除的节点
         head 替换删除, 因为可能会删除头结点
         */
        while (head != null) {
            if (head.val == val) {
                if (prev != null) {
                    // 不是头结点
                    prev.next = head.next;
                    head = prev;
                } else if (head.next != null) {
                    // 不是尾结点
                    head.val = head.next.val;
                    head.next = head.next.next;
                } else {
                    // 只有一个节点, 且是他
                    head.val = 0;
                    head.next = null;
                    return newHead;
                }
                continue;
            }

            if (newHead == null) {
                newHead = head;
            }
            prev = head;
            head = head.next;
        }
        return newHead;
    }
}
