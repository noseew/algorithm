package algorithmbase.leetcode.midle;

import org.junit.Test;

/**
 * 2. 两数相加
 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。
 * <p>
 * 请你将两个数相加，并以相同形式返回一个表示和的链表。
 * <p>
 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[7,0,8]
 * 解释：342 + 465 = 807.
 * 示例 2：
 * <p>
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 * 示例 3：
 * <p>
 * 输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
 * 输出：[8,9,9,9,0,0,0,1]
 * 输入 [9], [1,9,9,9,9,9,9,9,9,9]
 * 输出 [0,0,0,0,0,0,0,0,0,0,1]
 * <p>
 * 提示：
 * <p>
 * 每个链表中的节点数在范围 [1, 100] 内
 * 0 <= Node.val <= 9
 * 题目数据保证列表表示的数字不含前导零
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/add-two-numbers
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_02_TowSum {

    @Test
    public void test() {
        ListNode listNode = addTwoNumbers4(
                new ListNode(9),
                new ListNode(1,
                        new ListNode(9,
                                new ListNode(9,
                                        new ListNode(9,
                                                new ListNode(9,
                                                        new ListNode(9,
                                                                new ListNode(9,
                                                                        new ListNode(9,
                                                                                new ListNode(9,
                                                                                        new ListNode(9)))))))))));


//        ListNode listNode = addTwoNumbers4(new ListNode(2, new ListNode(4, new ListNode(3))),
//                new ListNode(5, new ListNode(6, new ListNode(4))));
        System.out.println(listNode.val);
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        java.math.BigInteger l1Val = java.math.BigInteger.valueOf(0L);
        java.math.BigInteger wei = java.math.BigInteger.valueOf(1L);
        do {
            l1Val = l1Val.add(java.math.BigInteger.valueOf(l1.val).multiply(wei));
            l1 = l1.next;
            wei = wei.multiply(java.math.BigInteger.valueOf(10L));
        } while (l1 != null);
        wei = java.math.BigInteger.valueOf(1L);
        java.math.BigInteger l2Val = java.math.BigInteger.valueOf(0L);
        do {
            l2Val = l2Val.add(java.math.BigInteger.valueOf(l2.val).multiply(wei));
            l2 = l2.next;
            wei = wei.multiply(java.math.BigInteger.valueOf(10L));
        } while (l2 != null);
        String sum = String.valueOf(l1Val.add(l2Val).toString());
        char[] chars = sum.toCharArray();

        ListNode head = null;
        for (int i = 0; i < chars.length; i++) {
            if (head == null) {
                head = new ListNode(Integer.parseInt(chars[i] + ""));
            } else {
                head = new ListNode(Integer.parseInt(chars[i] + ""), head);
            }
        }

        return head;

    }

    public ListNode addTwoNumbers2(ListNode l1, ListNode l2) {

        StringBuilder l1Str = new StringBuilder();
        ListNode l1Current = l1;
        l1Str.append(l1Current.val);
        while (l1Current.next != null) {
            l1Current = l1Current.next;
            l1Str.append(l1Current.val);
        }
        char[] l1chars = l1Str.toString().toCharArray();

        StringBuilder l2Str = new StringBuilder();
        ListNode l2Current = l2;
        l2Str.append(l2Current.val);
        while (l2Current.next != null) {
            l2Current = l2Current.next;
            l2Str.append(l2Current.val);
        }
        char[] l2chars = l2Str.toString().toCharArray();

        int maxSize = Math.max(l1chars.length, l2chars.length);
        StringBuilder res = new StringBuilder(maxSize);

        boolean plus = false;
        for (int i = 0; i < maxSize; i++) {
            int l1Val = 0;
            if (i < l1chars.length) {
                l1Val = Integer.parseInt(String.valueOf(l1chars[i]));
            }
            int l2Val = 0;
            if (i < l2chars.length) {
                l2Val = Integer.parseInt(String.valueOf(l2chars[i]));
            }
            int newVal = l1Val + l2Val;
            if (plus) {
                newVal++;
                plus = false;
            }
            if (newVal > 9) {
                plus = true;
                newVal = newVal % 10;
            }
            res.append(newVal);
        }
        if (plus) {
            res.append(1);
        }

        char[] chars = res.reverse().toString().toCharArray();

        ListNode head = null;
        for (int i = 0; i < chars.length; i++) {
            if (head == null) {
                head = new ListNode(Integer.parseInt(chars[i] + ""));
            } else {
                head = new ListNode(Integer.parseInt(chars[i] + ""), head);
            }
        }

        return head;
    }

    public ListNode addTwoNumbers3(ListNode l1, ListNode l2) {

        ListNode l1Current = l1;
        ListNode l2Current = l2;

        StringBuilder res = new StringBuilder();
        boolean plus = false;
        do {
            int l1Val = 0;
            if (l1Current != null) {
                l1Val = l1Current.val;
            }
            int l2Val = 0;
            if (l2Current != null) {
                l2Val = l2Current.val;
            }
            int newVal = l1Val + l2Val;
            if (plus) {
                newVal++;
                plus = false;
            }
            if (newVal > 9) {
                plus = true;
                newVal = newVal % 10;
            }
            res.append(newVal);
        } while ((l1Current != null && (l1Current = l1Current.next) != null)
                | (l2Current != null && (l2Current = l2Current.next) != null));

        if (plus) {
            res.append(1);
        }
        char[] chars = res.reverse().toString().toCharArray();
        ListNode head = null;
        for (char aChar : chars) {
            if (head == null) {
                head = new ListNode(Integer.parseInt(aChar + ""));
            } else {
                head = new ListNode(Integer.parseInt(aChar + ""), head);
            }
        }

        return head;
    }

    public ListNode addTwoNumbers4(ListNode l1, ListNode l2) {

        ListNode head = null;
        ListNode next = null;

        ListNode l1Current = l1;
        ListNode l2Current = l2;

        boolean plus = false;
        do {
            int l1Val = 0;
            if (l1Current != null) {
                l1Val = l1Current.val;
            }
            int l2Val = 0;
            if (l2Current != null) {
                l2Val = l2Current.val;
            }
            int newVal = l1Val + l2Val;
            if (plus) {
                newVal++;
                plus = false;
            }
            if (newVal > 9) {
                plus = true;
                newVal = newVal % 10;
            }

            if (next == null) {
                head = new ListNode(newVal);
                next = head;
            } else {
                next.next = new ListNode(newVal);
                next = next.next;
            }
        } while ((l1Current != null && (l1Current = l1Current.next) != null)
                | (l2Current != null && (l2Current = l2Current.next) != null));

        if (plus) {
            next.next = new ListNode(1);
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
