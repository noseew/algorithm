package org.song.algorithm.algorithmbase.alg._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.alg._02case.leetcode.ListNode;

/**
 * 234. 回文链表
 */
public class Leetcode_234_isPalindrome {

    @Test
    public void test() {

        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(1, null)));

        System.out.println(isPalindrome(headA));
    }

    public boolean isPalindrome(ListNode head) {

        ListNode node = head;

        int size = 0;
        while (node != null) {
            size++;
            node = node.next;
        }
        int middle = size >> 1;
        ListNode[] listNodes = new ListNode[size];
        node = head;
        for (int i = 0; i < size; i++) {
            listNodes[i] = node;
            node = node.next;
            if (i >= middle) {
                boolean equ = listNodes[size - i - 1].val == listNodes[i].val;
                if (!equ) {
                    return false;
                }
            }
        }


        return true;
    }
}
