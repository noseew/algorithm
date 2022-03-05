package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.LinkedSingle01;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

/**
 * 单向链表相关算法
 */
public class LinkedSingleCycle {

    private SingleNode<Integer> initData(int count) {
        LinkedSingle01<Integer> linked = new LinkedSingle01<>();
        for (int i = 0; i < count; i++) {
            linked.add(i);
        }
        return linked.head;
    }

    private SingleNode<Integer> initRingData() {
        SingleNode<Integer> node = initData(10);

        SingleNode node1 = node.next.next;
        SingleNode head = node;
        SingleNode tail = head;
        while (head != null) {
            tail = head;
            head = head.next;
        }
        tail.next = node1;
        return node;
    }

    /**
     * 判断链表是否有环
     */
    @Test
    public void test_hasRing() {
        SingleNode<Integer> linked = initRingData();
//        SingleNode<Integer> linked = initData(10);
        System.out.println(isHasRing(linked));
    }

    /**
     * 使用快慢指针: slow, fast
     * slow: 慢指针一次遍历一个
     * fast: 快指针一次遍历2个,
     * 这样如果有环, 快指针迟早会追上慢指针, slow == fast, 注意slow, fast初始选择不要相等
     */
    private boolean isHasRing(SingleNode<Integer> linked) {
        SingleNode<Integer> slow = linked, fast = slow.next;

        while (fast != null && fast.next != null) { // fast 走到头了, 说明没有环
            if (slow.value == fast.value) { // 指针相遇, 说明有环
                return true;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        return false;
    }

    /**
     * 判断链表环大小
     * 1. 选判断是否有环, 然后统计再次相遇
     */
    @Test
    public void test_ringSize() {
        SingleNode<Integer> linked = initRingData();
        SingleNode<Integer> slow = linked;
        SingleNode<Integer> fast = slow.next;

        int ringSize = 0;
        while (true) {
            if (slow == null || fast == null || fast.next == null) {
                break;
            }
            if (slow.value == fast.value) {
                if (ringSize > 0) {
                    // 第二次相遇, 中断
                    break;
                }
                // 第一次相遇 +1
                ringSize++;
            } else if (ringSize > 0) {
                // 之后每次 +1
                ringSize++;
            }

            slow = slow.next;
            fast = fast.next.next;
        }
        System.out.println(ringSize);
    }

    /**
     * 链表环入口节点
     * 1. 如果有环
     * 2. 获取环大小k
     * 3. 两个指针slow, fast, slow在head, fast先走到k, 然后同时走, 当他们相遇时, 就是入口节点
     */
    @Test
    public void test_ringEntry() {
        SingleNode<Integer> linked = initRingData();

        int ringSize = ringSize();

        SingleNode<Integer> slow = linked;
        SingleNode<Integer> fast = slow;
        SingleNode<Integer> entry = null;

        while (true) {
            if (ringSize > 0) {
                fast = fast.next;
                ringSize--;
            } else {
                slow = slow.next;
                fast = fast.next;
                if (slow.value == fast.value) {
                    entry = slow;
                    break;
                }
            }
        }
        System.out.println(entry.value);

    }

    /**
     * 获取环大小, 快慢指针第一次相遇后, 确定有环
     * 然后慢指针再走一圈, 即可确定环大小
     */
    public int ringSize() {
        SingleNode<Integer> linked = initRingData();
        SingleNode<Integer> slow = linked;
        SingleNode<Integer> fast = slow.next;

        int ringSize = 0;
        while (true) {
            if (slow == null || fast == null || fast.next == null) {
                break;
            }
            if (slow.value == fast.value) {
                if (ringSize > 0) {
                    // 第二次相遇, 中断
                    break;
                }
                // 第一次相遇 +1
                ringSize++;
            } else if (ringSize > 0) {
                // 之后每次 +1
                ringSize++;
            }

            slow = slow.next;
            fast = fast.next.next;
        }
        return ringSize;
    }

}
