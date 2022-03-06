package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.DuplexNode;

/**
 * 双向链表
 *
 * @param <T>
 */
public class LinkedDouble01<T> extends AbsLine<T> {

    protected DuplexNode<T> head;
    protected DuplexNode<T> tail;
    protected int size;

    public void rpush(T val) {
        if (tail != null) {
            tail.next = new DuplexNode<>(tail, null, val);
            tail = (DuplexNode<T>) tail.next;
        } else {
            head = new DuplexNode<>(null, null, val);
            tail = head;
        }
        size++;
    }

    public void lpush(T val) {
        if (head != null) {
            head.prev = new DuplexNode<>(null, head, val);
            head = (DuplexNode<T>) head.prev;
        } else {
            head = new DuplexNode<>(null, null, val);
            tail = head;
        }
        size++;
    }

    public T rpop() {
        if (tail != null) {
            T value = tail.value;
            tail.prev.next = null;
            tail = (DuplexNode<T>) tail.prev;
            tail.next = null;
            size--;
            return value;
        } else {
            return null;
        }
    }

    public T lpop() {
        if (head != null) {
            T value = head.value;
            ((DuplexNode<T>) head.next).prev = null;
            head = (DuplexNode<T>) head.next;
            head.prev = null;
            size--;
            return value;
        } else {
            return null;
        }
    }

    @Override
    public void clean() {
        /*
        这里仅仅是断开 GC root 指针, 
        JDK中不仅清空了这些, 还清空了每一个节点的指针, 
        目的: 1. 尽快的GC, 2. 节点可能会被迭代器对象引用, 清空他们可以尽快的释放迭代器
         */
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public T get(int index) {
        return getByIndex(index).value;
    }

    @Override
    public void set(T v, int index) {
        getByIndex(index).value = v;
    }

    @Override
    public int indexOf(T v) {
        return -1;
    }

    @Override
    public void add(T v) {
        this.rpush(v);
    }

    @Override
    public void insert(T v, int index) {
        checkIndexBound(index);
        if (index == 0) {
            lpush(v);
            return;
        }
        if (size - 1 == index) {
            rpush(v);
            return;
        }

        DuplexNode<T> n = getByIndex(index);
        /*
        优先将新节点指向下一个指针(原链表此时不变)
        然后将原链表前驱节点指向新节点
         */
        n.prev.next = new DuplexNode<>(n.prev, n, v);
        size++;
    }

    @Override
    public T delete(int index) {
        checkIndexBound(index);
        if (index == 0) {
            return lpop();
        }
        if (size - 1 == index) {
            return rpop();
        }

        DuplexNode<T> n = getByIndex(index);
        // 不考虑边界值, null, 因为特殊值已经在上边处理了
        n.prev.next = n.next;
        ((DuplexNode<T>) n.next).prev = n.prev;
        size--;
        return n.value;
    }

    @Override
    public T delete(T v) {
        if (v == null || size == 0) {
            return null;
        }

        DuplexNode<T> node = getByVal(v);
        if (node != null) {
            deleteNode(node);
            return node.value;
        }
        return null;
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
    
    protected void deleteNode(DuplexNode<T> node) {
        if (node != null) {
            if (node.prev != null) {
                node.prev.next = node.next;
            }
            if (node.next != null) {
                ((DuplexNode<T>) node.next).prev = node.prev;
            }
            size--;
        }
    }

    private void checkIndexBound(int index) {
        if (index + 1 > size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
    }

    private DuplexNode<T> getByIndex(int index) {
        if (index + 1 > size) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
        if (index == 0) {
            // 如果是头结点, 直接返回头
            return head;
        }
        if (index == size - 1) {
            // 如果是尾结点, 直接返回尾
            return tail;
        }
        DuplexNode<T> n = head;
        while (n != null && index-- > 0) {
            n = (DuplexNode<T>) n.next;
        }
        return n;
    }

    private DuplexNode<T> getByVal(T v) {
        if (v == null) {
            return null;
        }
        DuplexNode<T> n = head;
        while (n != null && n.value != v) {
            n = (DuplexNode<T>) n.next;
        }
        return n.value == v ? n : null;
    }
}
