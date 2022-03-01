package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

public class LinkedSingleCycle<T> extends LinkedSingle01<T> {

    /**
     * 链表的构建-尾插法, 正序
     *
     * @param v
     */
    @Override
    public void add(T v) {
        if (tail != null) {
            // 直接在尾指针后插入
            tail.next = new SingleNode<>(head, v);
            tail = tail.next;
        } else {
            head = new SingleNode<>(null, v);
            tail = head;
        }
        size++;
    }

    /**
     * 链表的构建-头插法, 倒序
     *
     * @param val
     */
    public void addHead(T val) {
        // 尾插法, 新节点是头结点
        head = new SingleNode<>(head, val);
        if (tail == null) {
            tail = head;
        } else {
            tail.next = head;
        }
        size++;
    }

    @Override
    public T delete(T v) {
        if (head == null) {
            return null;
        }

        if (head.value == v) {
            SingleNode<T> old = head;
            head = head.next;
            tail.next = head;
            size--;
            return old.value;
        }

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
}
