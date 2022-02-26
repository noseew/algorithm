package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.node.SingleNode;

public class Linked_single_01<T> {

    public SingleNode<T> head;
    public SingleNode<T> tail;
    public int size;

    /**
     * 链表的构建-尾插法, 正序
     *
     * @param val
     */
    public void addTail(T val) {
        if (tail != null) {
            // 直接在尾指针后插入
            tail.next = new SingleNode<>(null, val);
            tail = tail.next;
        } else {
            head = new SingleNode<>(null, val);
            tail = head;
        }
        size++;
    }

    /**
     * 由于链表没有下标, 所以要遍历到指定的位置
     *
     * @param val
     * @param index
     */
    public void insertByIndex(T val, int index) {
        if (size < index) {
            return;
        }
        if (index == 0) {
            if (head != null) {
                head = new SingleNode<>(head.next, val);
            } else {
                head = new SingleNode<>(null, val);
            }
        } else if (size == index) {
            tail.next = new SingleNode<>(null, val);
        }

        SingleNode<T> prev = getPrevByIndex(index);
        /*
        优先将新节点指向下一个指针(原链表此时不变)
        然后将原链表前驱节点指向新节点
         */
        prev.next = new SingleNode<>(prev.next, val);
        size++;
    }

    public T getByIndex(int index) {
        if (index + 1 > size) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
        if (index == 0) {
            return head.value;
        }
        SingleNode<T> prev = getPrevByIndex(index);
        return prev.next.value;
    }

    public T removeVal(T val) {
        if (head == null) {
            return null;
        }

        if (head.value == val) {
            SingleNode<T> old = head;
            head = head.next;
            size--;
            return old.value;
        }

        SingleNode<T> prevNode = getPrevByVal(val);
        if (prevNode == null) {
            return null;
        }
        SingleNode<T> delNode = prevNode.next;
        // 直接更改next指针, 即可完成删除
        prevNode.next = delNode.next;
        size--;
        return delNode.value;
    }

    public T removeIndex(int index) {
        if (index + 1 > size) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
        if (index == 0) {
            SingleNode<T> old = head;
            head = head.next;
            size--;
            return old.value;
        }

        SingleNode<T> prevNode = getPrevByIndex(index);
        if (prevNode == null) {
            return null; 
        }
        SingleNode<T> delNode = prevNode.next;
        prevNode.next = delNode.next;
        size--;
        return delNode.value;
    }

    /**
     * @param index
     * @return 返回null, 说明val是头结点或者没有匹配到对应的节点
     */
    private SingleNode<T> getPrevByIndex(int index) {
        if (index + 1 > size) {
            throw new ArrayIndexOutOfBoundsException("index 超出");
        }
        if (index == 0) {
            // 如果是头结点, 直接返回空
            return null;
        }
        SingleNode<T> prev = null, n = head;
        while (n != null && index-- > 0) {
            prev = n;
            n = n.next;
        }
        // 遍历到最后还未找到, 此时 prev 应该置为null
        return prev.next != null ? prev : null;
    }

    /**
     * @param val
     * @return 返回null, 说明val是头结点或者没有匹配到对应的节点
     */
    private SingleNode<T> getPrevByVal(T val) {
        if (head != null && head.value == val) {
            // 如果是头结点, 直接返回空
            return null;
        }
        SingleNode<T> prev = null, n = head;
        while (n != null && n.value != val) {
            prev = n;
            n = n.next;
        }
        // 遍历到最后还未找到, 此时 prev 应该置为null
        return prev.next != null ? prev : null;
    }

    @Override
    public String toString() {
        return ListPrinter.printSingleList(head, false);
    }

}
