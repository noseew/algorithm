package org.song.algorithm.algorithmbase._02case.leetcode.simple._04linked;

import org.junit.jupiter.api.Test;

/**
 * 面试题 02.01. 移除重复节点
 * 编写代码，移除未排序链表中的重复节点。保留最开始出现的节点。
 */
public class Leetcode_0201_removeDuplicate {

    @Test
    public void test() {

        ListNode head = new ListNode(1, new ListNode(2, new ListNode(3, new ListNode(3, null))));

        // 未完成
        ListNode node = removeDuplicateNodes(head);

        System.out.println(node);
    }

    public ListNode removeDuplicateNodes(ListNode head) {
        ListNode newHead = head;
        return newHead;
    }

}
