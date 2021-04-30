package org.song.algorithm.algorithmbase._02case.leetcode.simple._04linked;

import org.junit.Test;

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
        ListNode l1 = new ListNode(2);
//        ListNode l1 = null;
        ListNode l2 = new ListNode(1, new ListNode(1, new ListNode(1)));
        ListNode listNode = mergeTwoLists4(l1, l2);
        System.out.println(listNode);
    }

    public static class Ele {
        ListNode node;
        ListNode nextHandle;

        public Ele(ListNode node) {
            this.node = node;
            this.nextHandle = node;
        }
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {

        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode head = null;

        Ele ele1 = new Ele(l1);
        Ele ele2 = new Ele(l2);

        ListNode tail = null;
        Ele[] nodes = new Ele[]{ele1, ele2};
        for (; ; ) {
            ListNode mix = null;
            Ele currentEle = null;
            for (int i = 0; i < nodes.length; i++) {
                if (mix == null && nodes[i].nextHandle != null) {
                    mix = nodes[i].nextHandle;
                    currentEle = nodes[i];
                } else if (nodes[i].nextHandle != null && mix != null) {
                    if (mix.val > nodes[i].nextHandle.val) {
                        mix = nodes[i].nextHandle;
                        currentEle = nodes[i];
                    }
                }
            }
            if (mix == null) {
                break;
            }
            if (currentEle != null) {
                currentEle.nextHandle = mix.next;
            }
            if (head == null) {
                head = mix;
                tail = mix;
            } else {
                tail.next = mix;
                tail = mix;
            }
        }

        return head;
    }

    public ListNode mergeTwoLists2(ListNode l1, ListNode l2) {

        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode head = null;

        Ele ele1 = new Ele(l1);
        Ele ele2 = new Ele(l2);

        ListNode tail = null;
        Ele[] nodes = new Ele[]{ele1, ele2};
        for (; ; ) {
            ListNode mix = null;
            Ele currentEle = null;
            boolean single = false;
            for (int i = 0; i < nodes.length; i++) {
                if (mix == null && nodes[i].nextHandle != null) {
                    mix = nodes[i].nextHandle;
                    currentEle = nodes[i];
                } else if (nodes[i].nextHandle != null && mix != null) {
                    if (mix.val > nodes[i].nextHandle.val) {
                        mix = nodes[i].nextHandle;
                        currentEle = nodes[i];
                    }
                } else {
                    single = true;
                }
            }
            if (mix == null) {
                break;
            }
            if (currentEle != null) {
                currentEle.nextHandle = mix.next;
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

            while (mix.next != null && mix.val == mix.next.val) {
                mix = mix.next;
                tail.next = mix;
                tail = mix;
                currentHandle[currentIndex] = mix.next;
            }

            if (single) {
                break;
            }
        }

        return head;
    }


    // Definition for singly-linked list.
    public class ListNode {
        int val;
        ListNode next;

        ListNode() {
        }

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }


}
