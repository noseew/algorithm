package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.DuplexNode;

/**
 * 双向链表, 无界
 *
 * @param <T>
 */
public class LinkedDoubleCycle01<T> extends LinkedDouble01<T> {

    public void rpush(T val) {
        if (tail != null) {
            tail.next = new DuplexNode<>(tail, head, val);
            tail = (DuplexNode<T>) tail.next;
            head.prev = tail;
        } else {
            head = new DuplexNode<>(null, null, val);
            tail = head;
        }
        size++;
    }

    public void lpush(T val) {
        if (head != null) {
            head.prev = new DuplexNode<>(tail, head, val);
            head = (DuplexNode<T>) head.prev;
            tail.next = head;
        } else {
            head = new DuplexNode<>(null, null, val);
            tail = head;
        }
        size++;
    }

    public T rpop() {
        if (tail != null) {
            T value = tail.value;
            if (tail.next != null) {
                ((DuplexNode<T>) tail.next).prev = tail.prev;
            }
            if (tail.prev != null) {
                tail.prev.next = tail.next;
            }
            tail = (DuplexNode<T>) tail.prev;
            size--;
            return value;
        } else {
            return null;
        }
    }

    public T lpop() {
        if (head != null) {
            T value = head.value;
            if (head.next != null) {
                ((DuplexNode<T>) head.next).prev = head.prev;
            }
            if (head.prev != null) {
                head.prev.next = head.next;
            }
            head = (DuplexNode<T>) head.next;
            size--;
            return value;
        } else {
            return null;
        }
    }

    @Deprecated
    @Override
    public T get(int index) {
        throw new RuntimeException("暂不支持该操作");
    }

    @Deprecated
    @Override
    public int indexOf(T v) {
        throw new RuntimeException("暂不支持该操作");
    }

    @Override
    @Deprecated
    public void add(T v) {
        throw new RuntimeException("暂不支持该操作");
    }

    @Override
    @Deprecated
    public void insert(T v, int index) {
        throw new RuntimeException("暂不支持该操作");
    }

    @Override
    @Deprecated
    public T delete(int index) {
        throw new RuntimeException("暂不支持该操作");
    }

    @Override
    @Deprecated
    public T delete(T v) {
        throw new RuntimeException("暂不支持该操作");
    }

    public DuplexNode<T> getHead() {
        return head;
    }

    public DuplexNode<T> getTail() {
        return tail;
    }

    @Override
    public String toString() {
        return ListPrinter.printSingleList(head, false);
    }
}
