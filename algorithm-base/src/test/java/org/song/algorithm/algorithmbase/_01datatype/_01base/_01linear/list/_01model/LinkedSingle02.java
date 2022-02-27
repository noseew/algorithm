package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

public class LinkedSingle02<T> extends LinkedSingle01<T> {

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
        }
        size++;
    }
}
