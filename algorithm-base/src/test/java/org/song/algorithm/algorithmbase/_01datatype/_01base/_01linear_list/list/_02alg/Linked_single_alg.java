package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.Linked_single_01;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.ListPrinter;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.node.SingleNode;
import org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.stack.Stack_Link_01;

import java.util.Arrays;

/**
 * 单向链表相关算法
 */
public class Linked_single_alg {

    private SingleNode<Integer> initData(int count) {
        Linked_single_01<Integer> linked = new Linked_single_01<>();
        for (int i = 0; i < count; i++) {
            linked.add(i);
        }
        return linked.head;
    }

    private SingleNode<Integer> initDupData(int count) {
        Linked_single_01<Integer> linked = new Linked_single_01<>();
        for (int i = 0; i < count; i++) {
            linked.add(i);
        }
        for (int i = 0; i < count / 2; i++) {
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

    private SingleNode<Integer> commonTailData(SingleNode<Integer> head1) {
        SingleNode<Integer> head2 = initData(10);
        SingleNode<Integer> head2Tail = head2;
        SingleNode<Integer> head1Temp = head1;

        // head2 的尾结点指向 head1 的 index 节点
        int index = 5;
        while (head2Tail.next != null) {
            head2Tail = head2Tail.next;
        }

        while (head1Temp != null) {
            if (index <= 0) {
                head2Tail.next = head1Temp;
                break;
            }
            head1Temp = head1Temp.next;
            index--;
        }
        return head2;
    }

    /**
     * 倒数第k个节点
     * 1. 单向列表
     * 2. 取倒数第k个节点
     * 3. 复杂度O(n)
     * <p>
     * 解决思路:
     * 1. 有两个指针 p1, p2
     * 2. p1先从头遍历k个节点后, p2开始从头遍历
     * 3. p1遍历结束后, p2节点就是第k个节点
     * <p>
     * size - k = p2需要遍历的位置 = 倒数第k个位置
     */
    @Test
    public void test_01() {
        SingleNode<Integer> linked = initData(10);

        SingleNode<Integer> head = linked;
        SingleNode<Integer> p1 = head;
        SingleNode<Integer> p2 = head;

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
        SingleNode<Integer> linked = initData(10);
        SingleNode<Integer> prev = null, n = linked, next = null;
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

        ListPrinter.printSingleList(prev, true);

    }

    /**
     * 链表倒转
     * 递归的方式
     */
    @Test
    public void test_02_inversion_02() {
        SingleNode<Integer> linked = initData(10);
        SingleNode<Integer> prev = null, n = linked, next = null;
        SingleNode<Integer> newHead = inversion_02(prev, n, next);

        ListPrinter.printSingleList(newHead, true);

    }

    private SingleNode<Integer> inversion_02(SingleNode<Integer> prev,
                                                        SingleNode<Integer> n,
                                                        SingleNode<Integer> next) {
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
     * 链表倒转
     * 循环方式
     */
    @Test
    public void test_02_inversion_03() {
        SingleNode<Integer> head = initData(10);
        SingleNode<Integer> newHead = null;
        /*
         使用两个指针 head, newHead
         head用于遍历, newHead用户串新的链表
         */
        while (head != null) {
            SingleNode<Integer> next = head.next;
            // 串新链
            head.next = newHead;
            // 记录上一个head
            newHead = head;
            // 用于串联遍历
            head = next;
        }

        ListPrinter.printSingleList(newHead, true);

    }

    /**
     * 链表倒转
     * 递归的方式
     */
    @Test
    public void test_02_inversion_04() {
        SingleNode<Integer> linked = initData(10);
        SingleNode<Integer> newHead = inversion_04(linked);
        ListPrinter.printSingleList(newHead, true);
    }

    private SingleNode<Integer> inversion_04(SingleNode<Integer> head) {
        if (head == null || head.next == null) {
            return head;
        }

        SingleNode<Integer> newHead = inversion_04(head.next);
        // 类似于反向遍历
        head.next.next = head;
        // 反向将next值为null
        head.next = null;
        // 返回最新的head
        return newHead;
    }


    /**
     * 链表倒转
     * 递归的方式
     * 等价于 inversion_04
     */
    @Test
    public void test_02_inversion_05() {
        SingleNode<Integer> linked = initData(10);
        SingleNode<Integer> newHead = inversion_05(linked);
        ListPrinter.printSingleList(newHead, true);
    }

    public SingleNode<Integer> inversion_05(SingleNode<Integer> head) {
        if (head == null || head.next == null) {
            return head;
        }
        SingleNode<Integer> next = head.next;
        head.next = null;
        SingleNode<Integer> newHead = inversion_05(next);
        next.next = head;
        return newHead;
    }

    /**
     * 反向打印
     * 循环方式
     */
    @Test
    public void test_03_inversionPrint_01() {
        SingleNode<Integer> linked = initData(10);
        Stack_Link_01<Integer> stack = new Stack_Link_01<>();
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
        SingleNode<Integer> linked = initData(10);
        inversionPrint_02(linked);
    }

    private void inversionPrint_02(SingleNode<Integer> linked) {
        if (linked == null) {
            return;
        }
        inversionPrint_02(linked.next);
        System.out.println(linked.value);
    }

    /**
     * 获取链表中间节点值
     * 方式
     * 快慢指针, 慢指针一次一格, 快指针一次两格, 快指针走到头的时候, 慢指针所在的位置就是中间节点
     */
    @Test
    public void test_04_midVal() {
        SingleNode<Integer> linked = initData(9);
        SingleNode<Integer> slow = linked;
        SingleNode<Integer> fast = linked;

        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }

        ListPrinter.printSingleList(linked, true);
        System.out.println("中间节点是: " + slow.value);
        
    }

    /**
     * 删除链表中重复元素
     * 已知链表中最大元素是n
     * 
     * 思路: 采用 Set/数组/位图 进行重复标记
     */
    @Test
    public void test_04_remove_dup() {
        int max = 10;
        SingleNode<Integer> linked = initDupData(max);

        int[] set = new int[10];
        Arrays.fill(set, -1);

        SingleNode<Integer> n = linked, prev = null;
        while (n != null) {
            if (set[n.value] != n.value) {
                set[n.value] = n.value;
            } else {
                if (prev.next.next != null) {
                    prev.next = prev.next.next;
                } else {
                    prev.next = null;
                }
                n = prev.next;
                continue;
            }
            prev = n;
            n = n.next;
        }
        ListPrinter.printSingleList(linked, true);
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
        SingleNode<Integer> linked = initRingData();
        SingleNode<Integer> slow = linked;
        SingleNode<Integer> fast = slow.next;

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
    public void test_05_ringSize() {
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
    public void test_06_ringEntry() {
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

    /**
     * 给定节点指针, O(1)的复杂度删除之
     * 思路:
     * 1. 通常删除是先遍历到prev节点, 然后删除prev.next节点
     * 2. 可以将prev.next值赋值给prev, 同时删除prev.next, 达到类似的效果
     * 3. 但是如果删除的是最后一个元素, 则需要遍历, 总体复杂度仍然是O(1)
     */
    @Test
    public void test_07_removeO1() {
        SingleNode<Integer> head = initData(10);
        SingleNode<Integer> oHead = head;
        SingleNode<Integer> waiteRemove = head.next.next;

        if (waiteRemove.next != null) {
            // 非尾节点
            waiteRemove.value = waiteRemove.next.value;
            waiteRemove.next = waiteRemove.next.next;
        } else {
            // 是尾结点
            SingleNode<Integer> prev = head;
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
        ListPrinter.printSingleList(oHead, true);
    }

    /**
     * 找到两个链表的公共尾结点 入口
     */
    @Test
    public void test_08_commonTail() {
        SingleNode<Integer> head1 = initData(10);
        SingleNode<Integer> head2 = commonTailData(head1);
//        ListPrinter.printSingleList(head1);
//        System.out.println();
//        ListPrinter.printSingleList(head2);

        SingleNode<Integer> head1Temp = head1;
        SingleNode<Integer> head2Temp = head2;

        int countHead1Count = 0;
        while (head1Temp != null) {
            countHead1Count++;
            head1Temp = head1Temp.next;
        }

        int countHead2Count = 0;
        while (head2Temp != null) {
            countHead2Count++;
            head2Temp = head2Temp.next;
        }


        head1Temp = head1;
        head2Temp = head2;

        if (countHead1Count == countHead2Count) {
            // 两个链表完全长度重合
            while (head1Temp != null && head2Temp != null) {
                if (head1Temp.value == head2Temp.value) {
                    System.out.println("公共入口 " + head1Temp.value);
                    break;
                }
                head1Temp = head1Temp.next;
                head2Temp = head2Temp.next;
            }
        } else if (countHead1Count > countHead2Count) {
            // head1 更长
            int more = countHead1Count - countHead2Count;
            while (head1Temp != null && head2Temp != null) {
                if (head1Temp.value == head2Temp.value) {
                    System.out.println("公共入口 " + head1Temp.value);
                    break;
                }
                if (more-- <= 0) {
                    head2Temp = head2Temp.next;
                }
                head1Temp = head1Temp.next;
            }
        } else {
            // head2 更长
            int more = countHead2Count - countHead1Count;
            while (head1Temp != null && head2Temp != null) {
                if (head1Temp.value == head2Temp.value && head1Temp == head2Temp) {
                    System.out.println("公共入口 " + head1Temp.value);
                    break;
                }
                if (more-- <= 0) {
                    head1Temp = head1Temp.next;
                }
                head2Temp = head2Temp.next;
            }
        }

    }

    /**
     * 合并两个有序 链表
     */
    @Test
    public void test_09_merge() {
        SingleNode<Integer> linked1 = initData(5);
        SingleNode<Integer> linked2 = initData(3);

        // 临时头结点
        SingleNode<Integer> newLinkedHead = new SingleNode<>(null, -1);
        SingleNode<Integer> newLinked = newLinkedHead;

        while (linked1 != null && linked2 != null) {
            if (linked1.value < linked2.value) {
                newLinked.next = linked1;
                linked1 = linked1.next;
            } else {
                newLinked.next = linked2;
                linked2 = linked2.next;
            }
            newLinked = newLinked.next;
        }
        if (linked1 == null) {
            newLinked.next = linked2;
        }
        if (linked2 == null) {
            newLinked.next = linked1;
        }
        // 跳过临时头结点
        ListPrinter.printSingleList(newLinkedHead.next, true);
    }

    /**
     * 合并两个有序 链表, 直接在原链表上合并
     */
    @Test
    public void test_09_merge2() {
        SingleNode<Integer> linked1 = initData(5);
        SingleNode<Integer> linked2 = initData(3);

        // 临时头结点
        SingleNode<Integer> newHead = new SingleNode<>(null, -1);
        SingleNode<Integer> n = newHead;
        if (linked1 == null) {
            newHead = linked2;
        } else if (linked2 == null) {
            newHead = linked1;
        } else {
            while (linked1 != null || linked2 != null) {
                if (linked1 != null && linked2 != null) {
                    if (linked1.value <= linked2.value) {
                        n.next = linked1;
                        linked1 = linked1.next;
                    } else {
                        n.next = linked2;
                        linked2 = linked2.next;
                    }
                    n = n.next;
                } else if (linked1 != null) {
                    n.next = linked1;
                    break;
                } else {
                    n.next = linked2;
                    break;
                }
            }
        }
        // 跳过临时头结点
        ListPrinter.printSingleList(newHead.next, true);
    }

    /**
     * 找出链表中间元素
     * 方式1: O(n^2)遍历2遍, 第一遍获取总长度, 第二遍获取其一半
     * 方式2: O(1)两个指针, p1,p2, p1逐个遍历, p2遍历的速度是p1的2倍, 当p2遍历到尾部的时候, p1就是中间位置
     */
    @Test
    public void test_10_middle() {
        SingleNode<Integer> head = initData(9);
        SingleNode<Integer> p1 = head, p2 = head;

        int middle = 0;
        while (p2 != null && p2.next != null) {
            middle++;
            p1 = p1.next;
            p2 = p2.next.next;
        }
        System.out.println(middle);
    }

    /**
     * 单链表交换任意两个元素指针(不包括表头)
     * 思路: 直接更换他们的值?
     */
    @Test
    public void test_11_exchangeNode() {
        SingleNode<Integer> head = initData(9);
        SingleNode<Integer> node1 = head.next, node2 = head.next.next.next;
        Integer node1Val = node1.value;
        node1.value = node2.value;
        node2.value = node1Val;

        ListPrinter.printSingleList(head, true);
    }

}
