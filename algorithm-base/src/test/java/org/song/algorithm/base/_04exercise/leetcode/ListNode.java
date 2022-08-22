package org.song.algorithm.base._04exercise.leetcode;

public class ListNode {

    public int val;
    public ListNode next;
    public ListNode prev;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode prev, ListNode next) {
        this.val = val;
        this.prev = prev;
        this.next = next;
    }

    public ListNode(int x, ListNode next) {
        this.val = x;
        this.next = next;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(val);
        ListNode n = next;
        while (n != null) {
            sb.append(",").append(n.val);
            n = n.next;
        }
        return sb.toString();
    }

    public static ListNode build(int... val) {
        ListNode dummyHead = new ListNode();
        ListNode current = new ListNode();
        dummyHead.next = current;
        for (int i = 0; i < val.length; i++) {
            current.val = val[i];
            if (i != val.length - 1) {
                current.next = new ListNode();
                current = current.next;
            }
        }
        return dummyHead.next;
    }
}
