package org.song.algorithm.base._02alg.common;

public class StackBase {

    public void test01() {

    }

    /**
     * 单调栈, 取最近一个比其大/小的值
     */
    public int[] monotonic(int[] nums) {
        return nums;
    }


    static class ListNode {

        public int val;
        public ListNode prev, next;

        public ListNode(int val, ListNode prev, ListNode next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }
}
