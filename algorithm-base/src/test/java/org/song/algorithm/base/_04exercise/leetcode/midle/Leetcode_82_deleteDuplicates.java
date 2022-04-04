package org.song.algorithm.base._04exercise.leetcode.midle;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._04exercise.leetcode.ListNode;

import java.util.HashSet;
import java.util.Set;

/**
 * 82. 删除排序链表中的重复元素 II
 * 存在一个按升序排列的链表，给你这个链表的头节点 head ，请你删除链表中所有存在数字重复情况的节点，只保留原始链表中 没有重复出现 的数字。
 *
 * 返回同样按升序排列的结果链表。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_82_deleteDuplicates {

    @Test
    public void test() {

        ListNode head = new ListNode(1,
                new ListNode(2,
                        new ListNode(2,
                                new ListNode(3,
                                        new ListNode(3, null)))));
//        ListNode head = new ListNode(1, new ListNode(1, null));

        ListNode listNode = deleteDuplicates(head);
        System.out.println(listNode.val);

    }

    /**
     * 思路
     * 1. 第一次遍历, 通过set1记录重复, 并删除重复, 同时记录重复的值到另一个set2
     * 2. 第二次遍历, 删除set2中的值
     *
     * @param head
     * @return
     */
    public ListNode deleteDuplicates(ListNode head) {
        ListNode prev = null, h = head;
        Set<Integer> set = new HashSet<>();
        // 记录出现过重复的val, 等待二次删除
        Set<Integer> dupSet = new HashSet<>();

        while (h != null) {
            if (set.add(h.val)) {
                // 没有重复
                prev = h;
                h = h.next;
            } else {
                dupSet.add(h.val);
                if (prev != null) {
                    // 非首节点
                    prev.next = h.next;
                    h = prev.next;
                } else {
                    if (h.next != null) {
                        // 非尾结点
                        h.val = h.next.val;
                        h.next = h.next.next;
                        continue;
                    } else {
                        // 单独节点且命中
                        return null;
                    }
                }
            }
        }
        prev = null;
        h = head;

        while (h != null && !dupSet.isEmpty()) {
            if (dupSet.contains(h.val)) {
                dupSet.remove(h.val);
                // 第二波重复元素
                if (prev != null) {
                    // 非首节点
                    prev.next = h.next;
                    h = prev.next;
                    continue;
                } else {
                    if (h.next != null) {
                        // 非尾结点
                        h.val = h.next.val;
                        h.next = h.next.next;
                        continue;
                    } else {
                        // 单独节点且命中
                        return null;
                    }
                }
            }
            prev = h;
            h = h.next;
        }
        return head;

    }
}
