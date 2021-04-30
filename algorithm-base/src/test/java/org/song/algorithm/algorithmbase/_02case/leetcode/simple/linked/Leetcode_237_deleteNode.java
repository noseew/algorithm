package org.song.algorithm.algorithmbase._02case.leetcode.simple.linked;

import org.junit.jupiter.api.Test;

/**
 * 237. 删除链表中的节点
 * 请编写一个函数，使其可以删除某个链表中给定的（非末尾）节点。传入函数的唯一参数为 要被删除的节点 。
 */
public class Leetcode_237_deleteNode {

    @Test
    public void test() {

        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(1, null)));

        deleteNode(headA);
        System.out.println();
    }

    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
}
