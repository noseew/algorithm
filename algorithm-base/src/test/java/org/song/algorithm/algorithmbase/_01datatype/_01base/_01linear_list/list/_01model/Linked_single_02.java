package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

public class Linked_single_02<T> extends Linked_single_01<T> {

    public Node<T> head;
    public Node<T> tail;
    public int size;

    /**
     * 链表的构建-头插法, 倒序
     * 
     * @param val
     */
    public void addHead(T val) {
        // 尾插法, 新节点是头结点
        head = new Node<>(head, val);
        if (tail == null) {
            tail = head;
        }
        size++;
    }
}
