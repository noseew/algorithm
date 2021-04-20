package org.song.algorithm.algorithmbase.datatype.list;

public class Linked_single_02<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void add(T val) {
        if (tail != null) {
            tail.next = new Node<>(null, val);
            tail = tail.next;
        } else {
            head = new Node<>(null, val);
            tail = head;
        }
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
            Node<T> next = e.next;
            if (e.value.equals(val)) {
                size--;
                if (prev == e) {
                    head = next;
                } else {
                    prev.next = next;
                }
                return e.value;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    @Override
    public String toString() {
        return "head.value=" + head.value +
                ", tail.value=" + tail.value +
                ", size=" + size;
    }

    class Node<T> {
        Node next;
        T value;

        public Node(Node next, T value) {
            this.next = next;
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
