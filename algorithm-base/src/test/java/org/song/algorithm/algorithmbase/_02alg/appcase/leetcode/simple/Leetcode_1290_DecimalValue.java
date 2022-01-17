package org.song.algorithm.algorithmbase._02alg.appcase.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._02alg.appcase.leetcode.ListNode;

/**
 * 1290. 二进制链表转整数
 * 给你一个单链表的引用结点 head。链表中每个结点的值不是 0 就是 1。已知此链表是一个整数数字的二进制表示形式。
 * <p>
 * 请你返回该链表所表示数字的 十进制值 。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/convert-binary-number-in-a-linked-list-to-integer
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_1290_DecimalValue {

    @Test
    public void test() {

        ListNode headA = new ListNode(0, new ListNode(0, new ListNode(0, new ListNode(0, null))));

        int decimalValue = getDecimalValue(headA);
        System.out.println(decimalValue);
    }

    public int getDecimalValue(ListNode head) {
        int val = 0;
        while (head != null) {
            val = val << 1;
            val |= (head.val);
            head = head.next;
        }
        return val;
    }
}
