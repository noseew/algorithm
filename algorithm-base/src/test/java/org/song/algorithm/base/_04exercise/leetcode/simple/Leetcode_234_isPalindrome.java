package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.ListNode;

/**
 * 234. 回文链表
 */
public class Leetcode_234_isPalindrome {

    @Test
    public void test() {

        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(1, null)));

        System.out.println(isPalindrome(headA));
    }

    @Test
    public void test2() {

//        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(1, null)));
        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(3, null)));

        System.out.println(isPalindrome2(headA));

        ListNode reversal = reversal(headA);
        System.out.println(reversal);
    }

    @Test
    public void test3() {

//        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(1, null)));
        ListNode headA = new ListNode(1, new ListNode(1, new ListNode(2, new ListNode(1, null))));
//        ListNode headA = new ListNode(1, new ListNode(2, null));
//        ListNode headA = new ListNode(1, new ListNode(2, new ListNode(3, null)));

        System.out.println(isPalindrome3(headA));

        ListNode reversal = reversal(headA);
        System.out.println(reversal);
    }

    /**
     * 思路, 链表转数组, 通过数组判断
     * 空间效率 O(n)
     */
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

    /**
     * 思路2, 
     * 将中间节点往后的节点进行翻转, 然后从头和从中间开始遍历对比
     * 如果要求不改变原有链表结构, 则反转完再翻转回去, (doge)
     */
    public boolean isPalindrome2(ListNode head) {
        if (head.next == null) return true;
        
        ListNode node = head;
        int size = 0;
        while (node != null) {
            size++;
            node = node.next;
        }
        // 中间的定义是, 其下一个节点是接下来需要遍历的节点
        int middle = (size + 1) >> 1;

        ListNode r = head, l = head;
        for (int i = 1; i <= size && r.next != null; i++) {
            // 达到中间节点后, 后面的开始翻转
            if (i == middle) r.next = reversal(r.next);
            r = r.next;
            if (i >= middle) {
                if (l.val != r.val) return false;
                l = l.next;
            }
        }
        return true;
    }

    /**
     * 思路2, 
     * 优化取中间节点
     */
    public boolean isPalindrome3(ListNode head) {
        if (head.next == null) return true;
        if (head.next.next == null) return head.val == head.next.val;

        // 取中间节点
        ListNode r = middle(head), l = head;
        // 达到中间节点后, 后面的开始翻转
        r = reversal(r);

        while (r != null) {
            if (l.val != r.val) return false;
            l = l.next;
            r = r.next;
        }
        return true;
    }
    
    public ListNode reversal(ListNode head) {
        ListNode newHead = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = newHead;
            newHead = head;
            head = next;
        }
        return newHead;
    }

    
    public ListNode middle(ListNode head) {
        ListNode s = head, q = head;
        while (q != null && q.next != null) {
            s = s.next;
            q = q.next.next;
        }
        return s;
    }

}
