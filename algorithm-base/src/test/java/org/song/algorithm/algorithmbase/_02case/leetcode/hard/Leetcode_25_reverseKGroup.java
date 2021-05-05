package org.song.algorithm.algorithmbase._02case.leetcode.hard;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02case.leetcode.simple.linked.ListNode;

/**
 * 25. K 个一组翻转链表
 * 给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
 * <p>
 * k 是一个正整数，它的值小于或等于链表的长度。
 * <p>
 * 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 * <p>
 * 进阶：
 * <p>
 * 你可以设计一个只使用常数额外空间的算法来解决此问题吗？
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-nodes-in-k-group
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_25_reverseKGroup {

    @Test
    public void test() {

        ListNode head1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));
        ListNode listNode = reverseKGroup(head1, 2);
        System.out.println(listNode);
    }

    /**
     * 未完成
     */
    public ListNode reverseKGroup(ListNode head, int k) {

        ListNode h = head, prev = null;

        ListNode temp_head = null, temp_tail = null;
        int temp_k = 1;
        while (h != null) {
            ListNode next = h.next;

            if (temp_tail == null) {
                temp_tail = prev;
            }


            if (temp_k == k) {
                temp_k = 0;
                temp_tail.next = next;

            }
            h.next = prev;


            temp_k++;
            prev = h;
            h = next;
        }
        return head;
    }

}
