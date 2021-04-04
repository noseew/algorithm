package algorithmbase.leetcode.simple;

import org.junit.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

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
        ListNode l2 = new ListNode(1, new ListNode(1, new ListNode(2)));
        ListNode listNode = deleteDuplicates(l2);
        System.out.println(listNode);
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode tail = head;
        while (true) {
            if (tail.next  == null) {
                break;
            }
            if (tail.next.val > tail.val) {
                tail = tail.next;
            } else {
                tail.next = tail.next.next;
            }
        }
        return head;
    }

    /**
     * Definition for singly-linked list.
     */
     public class ListNode {
         int val;
         ListNode next;
         ListNode() {}
         ListNode(int val) { this.val = val; }
         ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     }

}
