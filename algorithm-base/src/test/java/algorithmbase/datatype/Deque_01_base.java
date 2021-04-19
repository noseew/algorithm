package algorithmbase.datatype;

public class Deque_01_base<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void rpush(T val) {
        if (tail != null) {
            tail.next = new Node<>(tail, null, val);
            tail = tail.next;
        } else {
            head = new Node<>(null, null, val);
            tail = head;
        }
        size++;
    }

    public T rpop() {
        if (tail != null) {
            T value = tail.value;
            Node prev = tail.prev;
            prev.next = null;
            tail.prev = null;
            tail = prev;
            size--;
            return value;
        } else {
            return null;
        }
    }

    public T lpop() {
        if (head != null) {
            T value = head.value;
            Node next = head.next;
            next.prev = null;
            head.next = null;
            head = next;
            size--;
            return value;
        } else {
            return null;
        }
    }

    public void lpush(T val) {
        if (head != null) {
            head.prev = new Node<>(null, head, val);
            head = head.prev;
        } else {
            head = new Node<>(null, null, val);
            tail = head;
        }
        size++;
    }


    class Node<T> {
        Node prev;
        Node next;
        T value;

        public Node(Node prev, Node next, T value) {
            this.prev = prev;
            this.next = next;
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return "head.value=" + head.value +
                ", tail.value=" + tail.value +
                ", size=" + size;
    }
}
