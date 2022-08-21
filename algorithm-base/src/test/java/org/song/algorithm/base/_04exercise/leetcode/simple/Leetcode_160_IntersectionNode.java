package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.ListNode;

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

    @Test
    public void test2() {
//        ListNode head = new ListNode(2, null);
//        ListNode headA = new ListNode(1, new ListNode(2, head));
////        ListNode headB = new ListNode(1, new ListNode(2, head));
        ListNode head = new ListNode(0, null);
        ListNode headA = new ListNode(1, new ListNode(2, null));
//        ListNode headB = new ListNode(1, new ListNode(2, head));

        ListNode node = getIntersectionNode2(headA, head);
        if (node != null) {
            System.out.println(node.val);
        }
    }

    /**
     * 思路1, 
     * 取出两个链表的长度, a1, a2
     * 然后跳过更长的部分, 
     * 假设a1更长, 则 a1从(a1-a2)开始遍历, a2从头开始遍历, 然后逐个判断他们是否相交
     */
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

    /**
     * 思路2
     * 和思路1类似, 将a1和a2拼接一起, 逻辑上形成两个新的链表 A1=(a1, a2), A2=(a2, a1)
     * 然后遍历他们, 当他们相交的时候, 判断相交的节点
     */
    public ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
        if (headA== null || headB == null) return null;

        ListNode ah = headA, bh = headB;
        // 最多交换2次, 2次之后如果还不出现, 则不相交
        int change = 0;

        while (ah != bh && change < 3) {
            System.out.println(ah.val + "-" + bh.val);
            if (ah.next == null) {
                ah = headB;
                change++;
            } else {
                ah = ah.next;
            }
            if (bh.next == null) {
                bh = headA;
                change++;
            } else {
                bh = bh.next;
            }
        }
        System.out.println(ah.val + "-" + bh.val);
        
        return change >= 3 || ah != bh ? null : ah;
    }
}
