package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

public class LinkedSingle02<T> extends AbsLine<T> {

    /**
     * 采用虚拟头结点, 可以减少空处理的情况
     */
    public SingleNode<T> head = new SingleNode<>(null, null);
    public SingleNode<T> tail;
    public int size;

    @Override
    public void clean() {
        this.head.next = null;
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
        checkIndexBound(index);
        SingleNode<T> prev = getPrevByIndex(index);
        return prev.next.value;
    }

    @Override
    public void set(T v, int index) {
        checkIndexBound(index);
        SingleNode<T> prev = getPrevByIndex(index);
        prev.next.value = v;
    }

    @Override
    public int indexOf(T v) {
        return -1;
    }

    /**
     * 链表的构建-尾插法, 正序
     *
     * @param v
     */
    @Override
    public void add(T v) {
        if (tail != null) {
            // 直接在尾指针后插入
            tail.next = new SingleNode<>(null, v);
            tail = tail.next;
        } else {
            head.next = new SingleNode<>(null, v);
            tail = head.next;
        }
        size++;
    }

    /**
     * 链表的构建-头插法, 倒序
     *
     * @param val
     */
    public void addHead(T val) {
        // 头插法, 新节点是头结点
        head = new SingleNode<>(head, val);
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    /**
     * 由于链表没有下标, 所以要遍历到指定的位置
     *
     * @param v
     * @param index
     */
    @Override
    public void insert(T v, int index) {
        checkIndexBound(index);
        SingleNode<T> prev = getPrevByIndex(index);
        /*
        优先将新节点指向下一个指针(原链表此时不变)
        然后将原链表前驱节点指向新节点
         */
        prev.next = new SingleNode<>(prev.next, v);
        size++;

    }

    @Override
    public T delete(int index) {
        checkIndexBound(index);
        SingleNode<T> prevNode = getPrevByIndex(index);
        SingleNode<T> delNode = prevNode.next;
        prevNode.next = delNode.next;
        size--;
        return delNode.value;
    }

    @Override
    public T delete(T v) {
        SingleNode<T> prevNode = getPrevByVal(v);
        if (prevNode == null) {
            return null;
        }
        SingleNode<T> delNode = prevNode.next;
        // 直接更改next指针, 即可完成删除
        prevNode.next = delNode.next;
        size--;
        return delNode.value;
    }

    public SingleNode<T> getHead() {
        return head;
    }

    @Override
    public String toString() {
        return ListPrinter.printSingleList(head, false);
    }

    private void checkIndexBound(int index) {
        if (index + 1 > size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
    }

    /**
     * @param index
     * @return
     */
    private SingleNode<T> getPrevByIndex(int index) {
        if (index + 1 > size) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
        SingleNode<T> prev = head;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        return prev;
    }

    /**
     * @param val
     * @return
     */
    protected SingleNode<T> getPrevByVal(T val) {
        SingleNode<T> prev = head, n = prev.next;
        while (n != null) {
            if (n.value == val) {
                return prev;
            }
            prev = n;
            n = n.next;
        }
        // 遍历到最后还未找到 null
        return null;
    }
}
