package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.stack;

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
    
    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
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
