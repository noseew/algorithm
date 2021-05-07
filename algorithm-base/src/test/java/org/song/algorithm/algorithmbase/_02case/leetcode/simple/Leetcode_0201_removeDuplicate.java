package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * 面试题 02.01. 移除重复节点
 * 编写代码，移除未排序链表中的重复节点。保留最开始出现的节点。
 *
 * 如果不得使用临时缓冲区，该怎么解决？
 */
public class Leetcode_0201_removeDuplicate {

    @Test
    public void test() {

        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(3, null))));
//        ListNode head = new ListNode(3, new ListNode(3, null));

        ListNode node = removeDuplicateNodes3(head);

        System.out.println(node);
    }

    /**
     * 采用临时缓冲区 HashSet
     */
    public ListNode removeDuplicateNodes(ListNode head) {
        Set<Integer> set = new HashSet<>();
        ListNode prev = null, h = head;
        while (h != null) {
            if (set.add(h.val)) {
                prev = h;
                h = h.next;
            } else {
                prev.next = h.next;
                h = prev.next;
            }
        }
        return head;
    }

    /**
     * 不采用临时缓冲区, 优先删除前面
     * leetcode 测试用例倒序输出了, 不知为何
     *
     */
    public ListNode removeDuplicateNodes2(ListNode head) {
        ListNode prev = null, h = head;
        while (h != null) {
            // 双层遍历, 删除重复
            if (has(h.next, h.val)) {
                if (h.next != null) {
                    // 如果不是尾结点 删除
                    h.val = h.next.val;
                    h.next = h.next.next;
                } else {
                    // 是尾结点 删除
                    prev.next = null;
                    break;
                }
            } else {
                // 不重复
                prev = h;
                h = h.next;
            }
        }
        return head;
    }

    private boolean has(ListNode node, int val) {
        if (node == null) {
            return false;
        }
        while (node != null) {
            if (node.val == val) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    /**
     * 不采用临时缓冲区 优先删除后面
     */
    public ListNode removeDuplicateNodes3(ListNode head) {
        ListNode prev = null, h = head;
        while (h != null) {
            // 双层遍历, 删除重复
            if (prev != null && has3(head, h.val, prev.val)) {
                // 有重复
                prev.next = h.next;
                h = prev.next;
            } else {
                // 不重复
                prev = h;
                h = h.next;
            }
        }
        return head;
    }

    private boolean has3(ListNode node, int val, int endVal) {
        if (node == null) {
            return false;
        }
        while (node != null) {
            // 在此之前是否有重复
            if (node.val == val) {
                return true;
            }
            // 遍历到指定位置
            if (node.val == endVal) {
                return false;
            }
            node = node.next;
        }
        return false;
    }


}
