package org.song.algorithm.algorithmbase._02alg._02case.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._02case.leetcode.ListNode;

/**
 * 83. 删除排序链表中的重复元素
 *
 * 存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除所有重复的元素，使每个元素 只出现一次 。
 *
 * 返回同样按升序排列的结果链表。
 *
 * 输入：head = [1,1,2]
 * 输出：[1,2]
 *
 * 输入：head = [1,1,2,3,3]
 * 输出：[1,2,3]
 *
 * 提示：
 *
 * 链表中节点数目在范围 [0, 300] 内
 * -100 <= Node.val <= 100
 * 题目数据保证链表已经按升序排列
 *
 */
public class Leetcode_83_DeleteDuplicate {

    @Test
    public void test() {
        ListNode l2 = new ListNode(1, new ListNode(1, new ListNode(2, null)));
        ListNode listNode = deleteDuplicates(l2);
        System.out.println(listNode);
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tail = head;
        while (tail.next != null) {
            if (tail.next.val > tail.val) {
                tail = tail.next;
            } else {
                tail.next = tail.next.next;
            }
        }
        return head;
    }
}
