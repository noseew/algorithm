package org.song.algorithm.algorithmbase._02case.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02case.leetcode.simple.linked.ListNode;

import java.util.Stack;

/**
 * 61. 旋转链表
 * 给你一个链表的头节点 head ，旋转链表，将链表每个节点向右移动 k 个位置。
 *
 * 等价于: 将链表首尾相连, 原尾部倒数第k个位置当成新的头结点
 */
public class Leetcode_61_rotateRight {

    @Test
    public void test() {

//        ListNode head = new ListNode(1,
//                new ListNode(2,
//                        new ListNode(3,
//                                new ListNode(4,
//                                        new ListNode(5, null)))));

//        ListNode head = new ListNode(0, new ListNode(1, new ListNode(2, null)));
        ListNode head = new ListNode(1, new ListNode(2, null));
        ListNode listNode = rotateRight(head, 2);
        System.out.println(listNode.val);

    }

    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null || k == 0) {
            return head;
        }
        ListNode h = head;
        // 反向遍历
        Stack<ListNode> stack = new Stack<>();
        while (h != null) {
            stack.push(h);
            h = h.next;
        }
        ListNode tail = stack.peek();
        // 形成环
        tail.next = head;
        // 倒数第k个位置就是新head
        k = k % stack.size();
        ListNode newHead = head, newHeadPrev = tail;
        for (int i = 0; i < k; i++) {
            newHead = stack.pop();
        }
        newHeadPrev = stack.pop();
        // 断开环k的位置
        newHeadPrev.next = null;

        return newHead;
    }
}
