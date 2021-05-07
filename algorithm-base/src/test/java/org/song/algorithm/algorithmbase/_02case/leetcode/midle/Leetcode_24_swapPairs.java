package org.song.algorithm.algorithmbase._02case.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02case.leetcode.ListNode;

/**
 * 19. 删除链表的倒数第 N 个结点
 * O(n) 单次遍历
 */
public class Leetcode_24_swapPairs {

    @Test
    public void test() {

        ListNode head = new ListNode(1,
                new ListNode(2,
                        new ListNode(3,
                                new ListNode(4,
                                        new ListNode(5, null)))));
        ListNode listNode = swapPairs(head);
        System.out.println(listNode.val);

    }

    public ListNode swapPairs(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode prev = null, n = null, newHead = null, last = null;
        while (head != null && head.next != null) {
            // 两两赋值
            prev = head;
            n = head.next;

            // 交换
            ListNode next = n.next;
            n.next = prev;
            prev.next = next;
            if (last != null) {
                // 和上一个末尾链接
                last.next = n;
            }

            last = prev;
            if (newHead == null) {
                newHead = n;
            }

            head = next;
        }
        return newHead;
    }
}
