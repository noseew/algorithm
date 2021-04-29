package org.song.algorithm.algorithmbase._02case.leetcode.simple.linked;

import org.junit.jupiter.api.Test;

/**
 * 160. 相交链表
 * 编写一个程序，找到两个单链表相交的起始节点。
 */
public class Leetcode_160_IntersectionNode {

    @Test
    public void test() {
        ListNode head = new ListNode(2, null);
        ListNode headA = new ListNode(1, new ListNode(2, head));
//        ListNode headB = new ListNode(1, new ListNode(2, head));

        ListNode node = getIntersectionNode(headA, head);
        if (node != null) {
            System.out.println(node.val);
        }
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {

        ListNode ah = headA, bh = headB;
        int aCount = 0;
        int bCount = 0;
        while (ah != null || bh != null) {
            if (ah != null) {
                aCount++;
                ah = ah.next;
            }
            if (bh != null) {
                bCount++;
                bh = bh.next;
            }
        }

        ah = headA;
        bh = headB;

        while (ah != null && bh != null) {
            if (aCount > bCount) {
                aCount--;
                ah = ah.next;
            }
            if (bCount > aCount) {
                bCount--;
                bh = bh.next;
            }
            if (ah == bh && ah.val == bh.val) {
                return ah;
            }
            if (bCount == aCount) {
                ah = ah.next;
                bh = bh.next;
            }
        }

        return null;
    }



    class ListNode {
        int val;
        ListNode next;

        ListNode(int x, ListNode next) {
            this.val = x;
            this.next = next;
        }
    }
}
