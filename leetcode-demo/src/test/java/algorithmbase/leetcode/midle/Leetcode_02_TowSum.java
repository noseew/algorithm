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
 *  
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
        // 未通过
        ListNode listNode = addTwoNumbers(
                new ListNode(2, new ListNode(4, new ListNode(3))),
                new ListNode(5, new ListNode(6, new ListNode(4))));
        System.out.println(listNode.val);
    }


    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        int l1Val = 0;
        int wei = 1;
        do {
            l1Val += l1.val *= wei;
            l1 = l1.next;
            wei *= 10;
        } while (l1 != null);
        wei = 1;
        int l2Val = 0;
        do {
            l2Val += l2.val *= wei;
            l2 = l2.next;
            wei *= 10;
        } while (l2 != null);
        String sum = String.valueOf(l1Val + l2Val);
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
