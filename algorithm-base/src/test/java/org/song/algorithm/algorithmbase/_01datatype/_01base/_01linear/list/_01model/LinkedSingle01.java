package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

public class LinkedSingle01<T> extends AbsLine<T> {

    public SingleNode<T> head;
    public SingleNode<T> tail;
    public int size;

    @Override
    public void clean() {
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
        checkIndexBound(index);
        
        if (index == 0) {
            return head.value;
        }
        if (index == size - 1) {
            return tail.value;
        }
        
        SingleNode<T> prev = getPrevByIndex(index);
        return prev.next.value;
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
        
        if (index == 0) {
            head = new SingleNode<>(head, v);
            size++;
            return;
        }
        if (size == index - 1) {
            tail.next = new SingleNode<>(null, v);
            size++;
            return;
        }

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
        
        if (index == 0) {
            SingleNode<T> old = head;
            head = head.next;
            size--;
            return old.value;
        }

        SingleNode<T> prevNode = getPrevByIndex(index);
        SingleNode<T> delNode = prevNode.next;
        prevNode.next = delNode.next;
        size--;
        return delNode.value;
    }

    @Override
    public T delete(T v) {
        if (head == null) {
            return null;
        }

        if (head.value == v) {
            SingleNode<T> old = head;
            head = head.next;
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
    protected SingleNode<T> getPrevByVal(T val) {
        if (head != null && head.value == val) {
            // 如果是头结点, 直接返回空
            return null;
        }
        SingleNode<T> prev = null, n = head;
        int cycle = 0; // 如果是是循环链表, 防止无限循环
        while (n != null && n.value != val && cycle < 2) {
            if (n == head) {
                cycle++;
            }
            prev = n;
            n = n.next;
        }
        // 遍历到最后还未找到, 此时 prev 应该置为null
        return prev.next != null ? prev : null;
    }
}
