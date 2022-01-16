package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.alg._02case.leetcode.ListNode;

/**
 * 剑指 Offer 22. 链表中倒数第k个节点
 * 输入一个链表，输出该链表中倒数第k个节点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。
 * <p>
 * 例如，一个链表有 6 个节点，从头节点开始，它们的值依次是 1、2、3、4、5、6。这个链表的倒数第 3 个节点是值为 4 的节点。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/lian-biao-zhong-dao-shu-di-kge-jie-dian-lcof
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_offer22_KthFromEnd {

    @Test
    public void test() {

        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));

        ListNode node = getKthFromEnd(head, 2);

        System.out.println(node);
    }

    public ListNode getKthFromEnd(ListNode head, int k) {
        /*
         p1, p2 两个指针, p2先走k步, 然后p1再走, 当p2走到末尾则p1对应的位置就是链表倒数第k个位置
         */
        ListNode p1 = head, p2 = head;
        while (p2 != null) {
            if (k-- <= 0) {
                p1 = p1.next;
            }
            p2 = p2.next;
        }
        return p1;
    }
}
