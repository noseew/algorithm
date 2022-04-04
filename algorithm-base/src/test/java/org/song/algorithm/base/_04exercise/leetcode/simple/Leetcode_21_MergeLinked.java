package org.song.algorithm.base._04exercise.leetcode.simple;

import org.junit.Test;
import org.song.algorithm.base._04exercise.leetcode.ListNode;

/**
 * 21. 合并两个有序链表
 * <p>
 * 将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 * <p>
 * 输入：l1 = [1,2,4], l2 = [1,3,4]
 * 输出：[1,1,2,3,4,4]
 * 示例 2：
 * <p>
 * 输入：l1 = [], l2 = []
 * 输出：[]
 * 示例 3：
 * <p>
 * 输入：l1 = [], l2 = [0]
 * 输出：[0]
 *  
 * <p>
 * 提示：
 * <p>
 * 两个链表的节点数目范围是 [0, 50]
 * -100 <= Node.val <= 100
 * l1 和 l2 均按 非递减顺序 排列
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/merge-two-sorted-lists
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_21_MergeLinked {

    @Test
    public void test() {
        ListNode l1 = new ListNode(2, null);
//        ListNode l1 = null;
        ListNode l2 = new ListNode(1, new ListNode(1, new ListNode(1, null)));
        ListNode listNode = mergeTwoLists4(l1, l2);
        System.out.println(listNode);
    }

    public ListNode mergeTwoLists3(ListNode l1, ListNode l2) {

        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }

        ListNode[] currentHandle = {l1, l2};

        ListNode head = null;

        ListNode tail = null;
        for (; ; ) {
            ListNode mix = null;
            int currentIndex = -1;
            boolean single = false;
            for (int i = 0; i < currentHandle.length; i++) {
                if (mix == null && currentHandle[i] != null) {
                    mix = currentHandle[i];
                    currentIndex = i;
                } else if (currentHandle[i] != null && mix != null) {
                    if (mix.val > currentHandle[i].val) {
                        mix = currentHandle[i];
                        currentIndex = i;
                    }
                } else {
                    single = true;
                }
            }
            if (mix == null) {
                break;
            }
            if (currentIndex >= 0) {
                currentHandle[currentIndex] = mix.next;
            }
            if (head == null) {
                head = mix;
                tail = mix;
            } else {
                tail.next = mix;
                tail = mix;
            }
            if (single) {
                break;
            }
        }

        return head;
    }

    public ListNode mergeTwoLists4(ListNode l1, ListNode l2) {

        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }

        ListNode[] currentHandle = {l1, l2};

        ListNode head = null;

        ListNode tail = null;
        for (; ; ) {
            // 每次循环获取到的 最小的一个node
            ListNode minNode = null;
            int currentIndex = -1;
            boolean single = false;

            // 循环多个链表, 得到最小的node, 并拼接成下一个node
            for (int i = 0; i < currentHandle.length; i++) {
                if (minNode == null && currentHandle[i] != null) {
                    minNode = currentHandle[i];
                    currentIndex = i;
                } else if (currentHandle[i] != null && minNode != null) {
                    if (minNode.val > currentHandle[i].val) {
                        minNode = currentHandle[i];
                        currentIndex = i;
                    }
                } else {
                    single = true;
                }
            }
            if (minNode == null) {
                break;
            }
            if (currentIndex >= 0) {
                // 取到min之后, 数组后移
                currentHandle[currentIndex] = minNode.next;
            }
            // 串成新链表
            if (head == null) {
                head = minNode;
                tail = minNode;
            } else {
                tail.next = minNode;
                tail = minNode;
            }

            // 如果当前链表 node = node.next, 则不切换链表, 直接取下一个
            while (minNode.next != null && minNode.val == minNode.next.val) {
                minNode = minNode.next;
                tail.next = minNode;
                tail = minNode;
                currentHandle[currentIndex] = minNode.next;
            }

            if (single) {
                break;
            }
        }

        return head;
    }

}
