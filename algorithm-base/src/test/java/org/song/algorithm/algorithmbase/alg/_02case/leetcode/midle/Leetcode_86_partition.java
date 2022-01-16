package org.song.algorithm.algorithmbase.alg._02case.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.alg._02case.leetcode.ListNode;

/**
 * 86. 分隔链表
 * 给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。
 * <p>
 * 你应当 保留 两个分区中每个节点的初始相对位置。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/partition-list
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_86_partition {

    @Test
    public void test() {

//        ListNode head = new ListNode(1,
//                new ListNode(4,
//                        new ListNode(3,
//                                new ListNode(2,
//                                        new ListNode(5, null)))));
//        ListNode head = new ListNode(2, new ListNode(1, null));
        ListNode head = new ListNode(1, new ListNode(1, null));

        ListNode listNode = partition(head, 2);
        System.out.println(listNode.val);

    }

    /**
     * 思路
     * 1. 第一次遍历, 记录并摘出所有<=x的node, 并串成新的链表
     * 2. 新链表和原来的老链表穿起来
     *
     * @param head
     * @param x
     * @return
     */
    public ListNode partition(ListNode head, int x) {

        if (head == null || head.next == null) {
            return head;
        }

        ListNode newHead = null, newTail = null, oldHead = null;

        ListNode prev = null;
        while (head != null) {
            if (head.val < x) {
                if (prev == null) {

                    ListNode temp = head;
                    // 首节点
                    // 摘除老链表
                    if (head.next != null) {
                        // 非尾节点
                        head = head.next;
                    } else {
                        // 只剩尾结点, 且符合
                        oldHead = null;
                        head = null;
                    }

                    // 移到新链表
                    temp.next = null;
                    if (newHead == null) {
                        newHead = temp;
                        newTail = temp;
                    } else {
                        newTail.next = temp;
                        newTail = newTail.next;
                    }
                } else {
                    ListNode temp = head;
                    // 非首节点
                    // 摘除老链表
                    prev.next = head.next;
                    head = prev;

                    // 移到新链表
                    temp.next = null;
                    if (newHead == null) {
                        newHead = temp;
                        newTail = temp;
                    } else {
                        newTail.next = temp;
                        newTail = newTail.next;
                    }
                }
                continue;
            } else {
                if (oldHead == null) {
                    oldHead = head;
                }
            }

            prev = head;
            head = head.next;
        }
        // 串联新老链表
        if (newTail != null) {
            newTail.next = oldHead;
            return newHead;
        }

        return oldHead;
    }

}
