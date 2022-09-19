package org.song.algorithm.base._04exercise.leetcode.hard;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.ListNode;

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
        ListNode listNode = reverseKGroup(head1, 3);
        System.out.println(listNode);
    }

    /**
     * 执行超时
     * 思路1
     * 1. 翻转链表, 并按照k长度分组
     * 2. 分组倒序串联链表, 形成新链表. 实现比较简单
     *
     * 思路2
     * 1. 遍历并反转链表, 同时根据k进行拆解链表, 同时串联新链表. 实现比较复杂
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || head.next == null) return head;

        ListNode newHead = null;

        ListNode h = head;
        ListNode headPrev = null;

        while (h != null) {
            // 找到k组的尾部, 如果不够返回null
            ListNode tail = tailNodeByK(h, k);
            // 不够k提前终止
            if (tail == null) break;

            ListNode tailNext = tail.next;
            // 翻转到tailNext就终止
            ListNode nh = revert(headPrev, h, tailNext);
            if (newHead == null) {
                newHead = nh;
            }
            headPrev = h;
            h = tailNext;
        }
        return newHead;
    }

    private ListNode tailNodeByK(ListNode list, int k) {
        while (k > 1 && list != null) {
            list = list.next;
            k--;
        }
        return list;
    }

    /**
     * @param headPrev head的前一个
     * @param list     链表头, 要反转的就是他
     * @param tailNext 链表尾部的下一个, 也是翻转截止的下一个位置
     * @return
     */
    private ListNode revert(ListNode headPrev, ListNode list, ListNode tailNext) {
        ListNode newTail = list, newHead = recursion(list, tailNext);
        newTail.next = tailNext;
        if (headPrev != null) {
            headPrev.next = newHead;
            return headPrev;
        }
        return newHead;
    }

    private ListNode recursion(ListNode head, ListNode brokenNode) {
        if (head == null || head.next == null || head.next == brokenNode) {
            return head;
        }
        ListNode newHead = recursion(head.next, brokenNode);
        head.next.next = head;
        head.next = null;
        return newHead;
    }

}
