package org.song.algorithm.algorithmbase.datatype.list;

public class Stack_base_01<T> {

    private Node<T> head;

    private int size;

    public void push(T val) {
        if (head == null) {
            head = new Node<>(null, val);
        } else {
            head = new Node<>(head, val);
        }
        size++;
    }

    public T pop() {
        T val = null;
        if (head != null) {
            val = head.value;
            head = head.next;
        }
        size--;
        return val;
    }


    class Node<T> {
        Node<T> next;
        T value;

        public Node(Node<T> next, T value) {
            this.next = next;
            this.value = value;
        }

    }
}
