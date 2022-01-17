package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.hard;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02alg.appcase.leetcode.ListNode;

/**
 * 23. 合并K个升序链表
 * 给你一个链表数组，每个链表都已经按升序排列。
 * <p>
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 */
public class Leetcode_23_mergeKLists {

    @Test
    public void test() {

        ListNode head1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));
        ListNode head2 = new ListNode(5, new ListNode(6, new ListNode(7, new ListNode(8, null))));
        ListNode listNode = mergeKLists(new ListNode[]{head1, head2});
        System.out.println(listNode);
    }

    /**
     * 太慢
     */
    public ListNode mergeKLists(ListNode[] lists) {

        if (lists.length == 0) {
            return null;
        }
        if (lists.length == 1) {
            return lists[0];
        }

        ListNode newNode = null, newNext = null;

        while (true) {
            ListNode minNode = null;
            int minNodeIndex = -1;
            // 每次循环, 都找出所有链表中, 表头最小的元素, 并拼接在新链表中
            for (int i = 0; i < lists.length; i++) {
                ListNode node = lists[i];
                if (node == null) {
                    continue;
                }
                if (minNode == null) {
                    minNode = node;
                    minNodeIndex = i;
                } else {
                    if (node.val < minNode.val) {
                        minNode = node;
                        minNodeIndex = i;
                    }
                }
            }

            if (minNode == null) {
                // 所有链表都到头了
                break;
            }

            // 数组选中链表 后移
            lists[minNodeIndex] = minNode.next;

            // 新链表
            if (newNode == null) {
                newNode = minNode;
                newNext = minNode;
            } else {
                newNext.next = minNode;
                newNext = newNext.next;
            }
        }
        return newNode;

    }

}
