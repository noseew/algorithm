package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.alg._02case.leetcode.ListNode;

import java.util.Arrays;

/**
 * 剑指 Offer 06. 从尾到头打印链表
 * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
 */
public class Leetcode_offer06_reversePrint {

    @Test
    public void test() {

        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(4, null))));

        int[] ints = reversePrint(headA);
        System.out.println(Arrays.toString(ints));
    }

    public int[] reversePrint(ListNode head) {
        ListNode node = head;
        int size = 0;
        while (node != null) {
            size++;
            node = node.next;
        }
        int[] ints = new int[size];
        node = head;
        while (node != null) {
            ints[--size] = node.val;
            node = node.next;
        }
        return ints;
    }
}
