package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.stack;

import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.LinkedSingle01;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.node.SingleNode;

/**
 * 链栈, 链表实现的栈
 * 
 * @param <T>
 */
public class Stack_Link_01<T> extends AbsStack<T> {

    private LinkedSingle01<T> linked = new LinkedSingle01<>();

    public void push(T val) {
        if (linked.isEmpty()) {
            linked.add(val);
        } else {
            linked.insert(val, 0);
        } 
    }

    public T pop() {
        if (linked.isEmpty()) {
            return null;
        }
        return linked.delete(0);
    }

    @Override
    public void clean() {
        linked.clean();
    }

    public boolean isEmpty() {
        return linked.isEmpty();
    }

    @Override
    public int length() {
        return linked.length();
    }

    @Override
    public T getTop() {
        SingleNode<T> top = linked.getHead();
        return top != null ? top.value : null;
    }

    @Override
    public String toString() {
        return linked.toString();
    }
}
