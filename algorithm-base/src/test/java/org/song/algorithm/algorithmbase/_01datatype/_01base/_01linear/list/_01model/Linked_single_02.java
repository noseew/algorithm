package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

public class Linked_single_02<T> extends Linked_single_01<T> {

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
