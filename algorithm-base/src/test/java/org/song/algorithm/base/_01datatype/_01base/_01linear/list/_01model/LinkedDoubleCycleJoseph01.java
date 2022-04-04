package org.song.algorithm.base._01datatype._01base._01linear.list._01model;

import org.song.algorithm.base._01datatype._01base._01linear.list._01model.node.DuplexNode;

/**
 * 约瑟夫问题
 * 一圈人, 从第1个开始, 每3个杀死1个, 死者剔除, 杀死后继续往下数3个, 请问谁是最后一个活着的 
 * 这里采用双向循环链表的方式来求解
 *
 * @param <T>
 */
public class LinkedDoubleCycleJoseph01<T> extends LinkedDoubleCycle01<T> {

    // 当前数到谁了
    protected DuplexNode<T> current;
    
    // 重置
    public void reset() {
        current = head;
    }
    
    // 向后移动一个
    public void next() {
        current = (DuplexNode<T>) current.next;
    }
    
    // 杀掉当前, 并向后移动一个
    public T remove() {
        T old = current.value;
        DuplexNode<T> next = (DuplexNode<T>) current.next;
        deleteNode(current);
        current = next;
        return old;
    }
}
