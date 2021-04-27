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
        for (int i = 0; i < 10; i++) {
            linked.add(i);
        }
        return linked.head;
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
}
