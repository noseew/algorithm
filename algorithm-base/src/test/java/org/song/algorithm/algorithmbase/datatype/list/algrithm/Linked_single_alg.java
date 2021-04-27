package org.song.algorithm.algorithmbase.datatype.list.algrithm;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.datatype.list.Linked_single_01;
import org.song.algorithm.algorithmbase.datatype.list.ListPrinter;
import org.song.algorithm.algorithmbase.datatype.list.Stack_base_01;

/**
 * 单向链表相关算法
 */
public class Linked_single_alg {

    private Linked_single_01.Node<Integer> initData() {
        Linked_single_01<Integer> linked = new Linked_single_01<>();
        for (int i = 0; i < 3; i++) {
            linked.add(i);
        }
        return linked.head;
    }

    private Linked_single_01.Node<Integer> initRingData() {
        Linked_single_01.Node<Integer> node = initData();

        Linked_single_01.Node node1 = node.next.next;
        Linked_single_01.Node head = node;
        Linked_single_01.Node tail = head;
        while (head != null) {
            tail = head;
            head = head.next;
        }
        tail.next = node1;
        return node;
    }

    /**
     * 倒数第k个节点
     * 1. 单向列表
     * 2. 取倒数第k个节点
     * 3. 复杂度O(n)
     *
     * 解决思路:
     * 1. 有两个指针 p1, p2
     * 2. p1先从头遍历k个节点后, p2开始从头遍历
     * 3. p1遍历结束后, p2节点就是第k个节点
     *
     * size - k = p2需要遍历的位置 = 倒数第k个位置
     *
     */
    @Test
    public void test_01() {
        Linked_single_01.Node<Integer> linked = initData();

        Linked_single_01.Node<Integer> head = linked;
        Linked_single_01.Node<Integer> p1 = head;
        Linked_single_01.Node<Integer> p2 = head;

        int k = 5;

        while (p1 != null) {
            if (k-- <= 0) {
                p2 = p2.next;
            }
            p1 = p1.next;
        }
        System.out.println(p2.value);
    }

    /**
     * 链表倒转
     * 循环方式
     */
    @Test
    public void test_02_inversion_01() {
        Linked_single_01.Node<Integer> linked = initData();
        Linked_single_01.Node<Integer> prev = null, n = linked, next = null;
        while (n != null) {
            // 初始赋值
            if (next == null) {
                next = n.next;
            }

            // 倒转
            n.next = prev;

            // 右移
            prev = n;
            n = next;
            next = null;
        }

        ListPrinter.printSingleList(prev);

    }

    /**
     * 链表倒转
     * 递归的方式
     */
    @Test
    public void test_02_inversion_02() {
        Linked_single_01.Node<Integer> linked = initData();
        Linked_single_01.Node<Integer> prev = null, n = linked, next = null;
        Linked_single_01.Node<Integer> newHead = inversion_02(prev, n, next);

        ListPrinter.printSingleList(newHead);

    }

    private Linked_single_01.Node<Integer> inversion_02(Linked_single_01.Node<Integer> prev,
                                                        Linked_single_01.Node<Integer> n,
                                                        Linked_single_01.Node<Integer> next) {
        if (n == null) {
            return prev;
        }
        // 初始赋值
        if (next == null) {
            next = n.next;
        }

        // 倒转
        n.next = prev;

        // 右移
        prev = n;
        n = next;
        next = null;
        return inversion_02(prev, n, next);
    }

    /**
     * 反向打印
     * 循环方式
     */
    @Test
    public void test_03_inversionPrint_01() {
        Linked_single_01.Node<Integer> linked = initData();
        Stack_base_01<Integer> stack = new Stack_base_01<>();
        while (linked != null) {
            stack.push(linked.value);
            linked = linked.next;
        }

        Integer v;
        while ((v = stack.pop()) != null) {
            System.out.println(v);
        }
    }

    /**
     * 反向打印
     * 递归方式
     */
    @Test
    public void test_03_inversionPrint_02() {
        Linked_single_01.Node<Integer> linked = initData();
        inversionPrint_02(linked);
    }

    private void inversionPrint_02(Linked_single_01.Node<Integer> linked) {
        if (linked == null) {
            return;
        }
        inversionPrint_02(linked.next);
        System.out.println(linked.value);
    }

    /**
     * 判断链表是否有环
     * 使用快慢指针: slow, fast
     * slow: 慢指针一次遍历一个
     * fast: 快指针一次遍历2个,
     * 这样如果有环, 快指针迟早会追上慢指针, slow == fast, 注意slow, fast初始选择不要相等
     */
    @Test
    public void test_04_hasRing() {
        Linked_single_01.Node<Integer> linked = initRingData();
        Linked_single_01.Node<Integer> slow = linked;
        Linked_single_01.Node<Integer> fast = slow.next;

        boolean hasRing = false;
        while (true) {
            if (slow == null || fast == null || fast.next == null) {
                hasRing = false;
                break;
            }
            if (slow.value == fast.value) {
                hasRing = true;
                break;
            }
            slow = slow.next;
            fast = fast.next.next;
        }
        System.out.println(hasRing);

    }

    /**
     * 判断链表环大小
     * 1. 选判断是否有环, 然后统计再次相遇
     */
    @Test
    public void test_04_ringSize() {
        Linked_single_01.Node<Integer> linked = initRingData();
        Linked_single_01.Node<Integer> slow = linked;
        Linked_single_01.Node<Integer> fast = slow.next;

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
    public void test_04_ringEntry() {
        Linked_single_01.Node<Integer> linked = initRingData();

        int ringSize = ringSize();

        Linked_single_01.Node<Integer> slow = linked;
        Linked_single_01.Node<Integer> fast = slow;
        Linked_single_01.Node<Integer> entry = null;

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

    public int ringSize() {
        Linked_single_01.Node<Integer> linked = initRingData();
        Linked_single_01.Node<Integer> slow = linked;
        Linked_single_01.Node<Integer> fast = slow.next;

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

    /**
     * 给定节点指针, O(1)的复杂度删除之
     * 思路:
     * 1. 通常删除是先遍历到prev节点, 然后删除prev.next节点
     * 2. 可以将prev.next值赋值给prev, 同时删除prev.next, 达到类似的效果
     * 3. 但是如果删除的是最后一个元素, 则需要遍历, 总体复杂度仍然是O(1)
     */
    @Test
    public void test_05_removeO1() {
        Linked_single_01.Node<Integer> head = initData();
        Linked_single_01.Node<Integer> oHead = head;
        Linked_single_01.Node<Integer> waiteRemove = head.next.next;

        if (waiteRemove.next != null) {
            // 非尾节点
            waiteRemove.value = waiteRemove.next.value;
            waiteRemove.next = waiteRemove.next.next;
        } else {
            // 是尾结点
            Linked_single_01.Node<Integer> prev = head;
            while (head != null) {
                if (head.value == waiteRemove.value) {
                    break;
                }
                // 注意和普通遍历有区别, 并不是等到最后一个才停止遍历, 所以prev要到倒数第二个
                prev = head;
                head = head.next;
            }
            // 删除尾结点
            prev.next = null;
        }
        ListPrinter.printSingleList(oHead);
    }

    /**
     * 找到两个链表的公共尾结点 入口
     */
    @Test
    public void test_05_commonTail() {

    }
}
