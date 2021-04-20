package org.song.algorithm.algorithmbase.datatype;

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
        Node<T> pre = head;
        Node<T> e = pre;
        while (e != null) {
            Node<T> next = e.next;
            if (e.value.equals(val)) {
                size--;
                if (pre == e) {
                    head = next;
                } else {
                    pre.next = next;
                }
                return e.value;
            }
            pre = e;
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
