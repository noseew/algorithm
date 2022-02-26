package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

public class Linked_single_01<T> {

    public Node<T> head;
    public Node<T> tail;
    public int size;

    public void add(T val) {
        if (tail != null) {
            // 直接在尾指针后插入
            tail.next = new Node<>(null, val);
            tail = tail.next;
        } else {
            head = new Node<>(null, val);
            tail = head;
        }
        size++;
    }

    /**
     * 由于链表没有下标, 所以要遍历到指定的位置
     * 
     * @param val
     * @param index
     */
    public void insert(T val, int index) {
        if (size < index) {
            return;
        }
        if (index == 0) {
            if (head != null) {
                head = new Node<>(head.next, val);
            } else {
                head = new Node<>(null, val);
            }
        } else if (size == index) {
            tail.next = new Node<>(null, val);
        }

        Node n = null;
        for (int i = 0; i < index; i++) {
            if (n == null) {
                n = head;
                continue;
            }
            n = n.next;
        }

        /*
        优先将新节点指向下一个指针(原链表此时不变)
        然后将原链表前驱节点指向新节点
         */
        n.next = new Node<>(n.next, val);
        size++;
    }

    public T remove(T val) {
        if (head == null) {
            return null;
        }
        /*
         单向链表遍历删除问题
         1. 如果匹配到当前元素e, 并需要删除e, 则需要将e.prev连接到e.next,
            问题: 由于是单向链表, 没有e.prev,
            解决: 所以需要两个变量 prev和n
         2. 如果e需要删除, 则新的链接为 prev.next = e.next
            问题: 如果e是第一个元素, 此时没有prev如何解决
            解决:
                1. 第一个元素单独处理, 处理完之后在进入遍历代码
                2. 第一个元素在遍历代码中处理,
                    如果 e = prev, 则说明是第一个元素, 直接将第一个元素设置成 e.next
                    否则 prev 向后移动一步 prev = e, e 向后移动一步 e = e.next
         */
        Node<T> prev = head;
        Node<T> e = prev;
        while (e != null) {
            prev = e;

            if (e.value.equals(val)) {
                size--;
                if (prev == e) {
                    head = e.next;
                } else {
                    // 直接更改next指针, 即可完成删除
                    prev.next = e.next;
                }
                return e.value;
            }
            e = e.next;
        }

        return null;
    }

    @Override
    public String toString() {
        return "head.value=" + head.value +
                ", tail.value=" + tail.value +
                ", size=" + size;
    }

    public static class Node<T> {
        public Node<T> next;
        public T value;

        public Node(Node<T> next, T value) {
            this.next = next;
            this.value = value;
        }
    }
}
