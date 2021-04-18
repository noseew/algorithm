package algorithmbase.datatype;

public class Linked_01_base<T> {

    private Node<T> head;
    private Node<T> tail;

    public void add(T val) {
        if (tail != null) {
            tail.next = new Node<>(null, val);
            tail = tail.next;
        } else {
            head = new Node<>(null, val);
            tail = head;
        }
    }

    private T remove(T val) {
        if (head == null) {
            return null;
        }
        Node<T> h = head;
        while (h != null) {
            T oldVal = null;
            if (h.value.equals(val)) {
                oldVal = h.value;

            }
        }
        return null;
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
