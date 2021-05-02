package org.song.algorithm.algorithmbase._02case.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02case.leetcode.simple.linked.ListNode;

/**
 * 19. 删除链表的倒数第 N 个结点
 * O(n) 单次遍历
 */
public class Leetcode_19_removeNthFromEnd {

    @Test
    public void test() {

        ListNode head = new ListNode(1,
                new ListNode(2,
                        new ListNode(3,
                                new ListNode(4,
                                        new ListNode(5, null)))));
//        ListNode head = new ListNode(1, null);
        ListNode listNode = removeNthFromEnd(head, 5);
        System.out.println(listNode.val);

    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (head == null) {
            return head;
        }
        if (head.next == null && n == 1) {
            return null;
        }

        ListNode prev = null, p = head, h = head;
        while (h != null) {
            if (n-- <= 0) {
                prev = p;
                p = p.next;
            }
            h = h.next;
        }

        if (prev == null) {
            // 删除头结点
            head.val = head.next.val;
            head.next = head.next.next;
            return head;
        }
        prev.next = prev.next.next;
        return head;
    }
}
