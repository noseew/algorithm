package org.song.algorithm.algorithmbase._04alg_exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._04alg_exercise.leetcode.ListNode;

/**
 * 92. 反转链表 II
 * 给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节点，返回 反转后的链表 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/reverse-linked-list-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_92_reverseBetween {

    @Test
    public void test() {

        ListNode head = new ListNode(10,
                new ListNode(20,
                        new ListNode(30,
                                new ListNode(40,
                                        new ListNode(50, null)))));
//        ListNode head = new ListNode(3, new ListNode(5, null));

        ListNode listNode = reverseBetween(head, 4, 5);
        System.out.println(listNode.val);

    }

    /**
     * 思路
     * <p>
     * 1. 一次遍历, 当遍历到 left 下标时, 记录该位置prev
     * 2. 之后的位置边遍历边翻转, 直到遇到 right 下标, 并记录right和next
     * 3. 重新串联
     *
     * @param head
     * @param left
     * @param right
     * @return
     */
    public ListNode reverseBetween(ListNode head, int left, int right) {
        if (head == null || head.next == null || left == right) {
            return head;
        }

        // 第一段
        ListNode p1_head = null, p1_next = null,
                // 第二段 需要翻转的
                p2_head = null, p2_tail = null,
                // 第三段
                p3_head = null;

        boolean reverse = false;

        ListNode prev = null;
        int index = 1;
        while (head != null) {

            // 遇到left
            if (index == left && !reverse) {
                // 标记翻转
                reverse = true;
                // p1 p2 断开
                p1_next = prev;
                if (prev != null) {
                    prev.next = null;
                }
                p2_head = head;
                p2_tail = head;
                prev = null;
            }

            if (p1_head == null && p2_head == null) {
                p1_head = head;
                p1_next = head;
            }

            ListNode next = head.next;
            if (reverse) {
                // 翻转列表
                head.next = prev;
                p2_head = head;
                // 遇到right
                if (index == right) {
                    // p2 p3 断开 head.next = prev
                    p3_head = next;
                    // 恢复翻转
                    reverse = false;
                    head = p3_head;
                }
            }
            prev = head;
            head = next;
            index++;
        }

        // 重新串联
        if (p1_next != null) {
            p1_next.next = p2_head;
        }
        if (p2_tail != null) {
            p2_tail.next = p3_head;
        }

        if (p1_head != null) {
            return p1_head;
        }
        return p2_head;
    }


}
