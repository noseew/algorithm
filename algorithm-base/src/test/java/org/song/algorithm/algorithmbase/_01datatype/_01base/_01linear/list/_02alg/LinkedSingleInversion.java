package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.LinkedSingle01;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ListPrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

public class LinkedSingleInversion {
    private SingleNode<Integer> initData(int count) {
        LinkedSingle01<Integer> linked = new LinkedSingle01<>();
        for (int i = 0; i < count; i++) {
            linked.add(i);
        }
        return linked.head;
    }

    /**
     * 链表倒转, 遍历方式
     */
    @Test
    public void test_traverse01() {
        SingleNode<Integer> head = initData(10);
        SingleNode<Integer> newHead = null;
        /*
         使用两个指针 head, newHead
         head用于遍历, newHead用户串新的链表
         */
        while (head != null) {
            SingleNode<Integer> next = head.next;
            // 串新链, 翻转
            head.next = newHead;
            // 记录上一个head
            newHead = head;
            // 用于串联遍历, 向后移动
            head = next;
        }
        ListPrinter.printSingleList(newHead, true);
    }

    /**
     * 链表倒转, 递归的方式
     */
    @Test
    public void test_recursion01() {
        SingleNode<Integer> n = initData(10);
        SingleNode<Integer> prev = null;
        SingleNode<Integer> newHead = recursion01_5(prev, n);

        ListPrinter.printSingleList(newHead, true);

    }

    /**
     * inversion_recursion01_1 => inversion_recursion01_5
     * 展示了递归方法的演变, 当通过两个递归参数时, 最终演变成 inversion_recursion01_5
     */
    private static SingleNode<Integer> recursion01_1(SingleNode<Integer> prev,
                                                     SingleNode<Integer> n,
                                                     SingleNode<Integer> next) {
        /*
        利用递归的方式进行遍历, 每次都将前中后三个节点传入递归, 递归方法进行重新反向关联
         */
        if (n == null) return prev;
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
        return recursion01_1(prev, n, next);
    }

    private static SingleNode<Integer> recursion01_2(SingleNode<Integer> prev,
                                                     SingleNode<Integer> n,
                                                     SingleNode<Integer> next) {
        if (n == null) return prev;
        // 初始赋值
//        if (next == null) {
        next = n.next;
//        }
        // 倒转
        n.next = prev;
        // 右移
        prev = n;
        n = next;
        next = null;
        return recursion01_2(prev, n, next);
    }

    private static SingleNode<Integer> recursion01_3(SingleNode<Integer> prev,
                                                     SingleNode<Integer> n) {
        if (n == null) return prev;
        SingleNode<Integer> next = n.next;
        // 倒转
        n.next = prev;
        // 右移
        prev = n;
        n = next;
        return recursion01_3(prev, n);
    }

    private static SingleNode<Integer> recursion01_4(SingleNode<Integer> prev,
                                                     SingleNode<Integer> n) {
        /*
        利用递归的方式进行遍历, 每次都将前中2个节点传入递归, 递归方法进行重新反向关联
         */
        if (n == null) return prev;
        SingleNode<Integer> next = n.next;
        n.next = prev; // 倒转
        return recursion01_4(n, next);
    }

    private static SingleNode<Integer> recursion01_5(SingleNode<Integer> prev,
                                                     SingleNode<Integer> n) {
        /*
        利用递归的方式进行遍历, 每次都将前中2个节点传入递归, 递归方法进行重新反向关联
        n==null说明递归到链表尾部, 所以需要返回它的前驱节点, 也就是新的头结点
         */
        if (n == null) return prev;
        SingleNode<Integer> next = n.next;
        n.next = prev; // 倒转
        return recursion01_5(n, next);
    }

    /**
     * 链表倒转, 递归的方式
     */
    @Test
    public void test_recursion02() {
        SingleNode<Integer> linked = initData(10);
        SingleNode<Integer> newHead = recursion02(linked);
        ListPrinter.printSingleList(newHead, true);
    }

    private static SingleNode<Integer> recursion02(SingleNode<Integer> head) {
        // 不能没有节点
        if (head == null 
                // 不能总返回null, 1. 需要返回的是尾节点, 来当新的头结点
                || head.next == null) {
            return head;
        }

        /*
        先递归链表到尾部, 使用线程栈来存放递归的元素, 每一个栈都保存有一个节点head, 当递归到尾部的时候, 第一个返回的就是尾部元素
        newHead 返回的永远是尾节点, 
         */
        SingleNode<Integer> newHead = recursion02(head.next);
        // 类似于反向遍历
        /*
        相当于 newHead.next = head; 但是不能用此行, 因为最终要返回新的head, 所以 newHead 引用不能变化
        所以这里用 head.next.next = head;
         */
        head.next.next = head;
        // 反向将next值为null
        head.next = null;
        // 返回最新的head, newHead 返回的永远是尾节点, 并不随着递归的出栈入栈深度而变化
        return newHead;
    }


    /**
     * 链表倒转, 递归的方式
     */
    @Test
    public void test_recursion03() {
        SingleNode<Integer> linked = initData(10);
        SingleNode<Integer> newHead = recursion03(linked);
        ListPrinter.printSingleList(newHead, true);
    }

    public static SingleNode<Integer> recursion03(SingleNode<Integer> head) {
        if (head == null || head.next == null) {
            return head;
        }
        SingleNode<Integer> next = head.next;
        head.next = null;
        SingleNode<Integer> newHead = recursion03(next);
        next.next = head;
        return newHead;
    }

}
