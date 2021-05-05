package org.song.algorithm.algorithmbase._02case.leetcode.hard;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02case.leetcode.simple.linked.ListNode;

import java.util.ArrayList;
import java.util.List;

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

        ListNode head1 = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, new ListNode(5, null)))));
        ListNode listNode = reverseKGroup(head1, 2);
        System.out.println(listNode);
    }

    /**
     * 思路1
     * 1. 翻转链表, 并按照k长度分组
     * 2. 分组倒序串联链表, 形成新链表. 实现比较简单
     *
     * 未完成
     * 思路2
     * 1. 遍历并反转链表, 同时根据k进行拆解链表, 同时串联新链表. 实现比较复杂
     */
    public ListNode reverseKGroup(ListNode head, int k) {

        List<ListNode> list = new ArrayList<>();

        int k_times = 1;

        ListNode h = head, prev = null;
        while (h != null) {
            ListNode next = h.next;
            if (k_times == k
                    // 末尾不够数量直接添加
                    || next == null) {
                k_times = 0;
                list.add(h);
            }

            if (k_times == 1) {
                // 断开连接
                h.next = null;
                prev = null;
            }
            h.next = prev;
            prev = h;

            h = next;
            k_times++;
        }

        // 总队头
        ListNode newHead = null;

        // 串新链表
        ListNode listTail = null;
        for (ListNode listHead : list) {
            // 本次队头
            if (newHead == null) {
                newHead = listHead;
            }

            if (listTail != null) {
                // 上一个队尾连接本次队头
                listTail.next = listHead;
            }

            // 本次队尾
            while (listHead != null) {
                listTail = listHead;
                listHead = listHead.next;
            }
        }

        return newHead;
    }

}
