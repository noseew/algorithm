package org.song.algorithm.base._01datatype._01base._04tree.heap;

import java.util.Collection;
import java.util.Comparator;

/**
 * @param <T>
 */
public class BinaryHeap_base_03<T> extends BinaryHeap_base_02<T> {
    
    /**
     * 建堆
     * @param little
     * @param comparator
     * @param collection
     */
    public BinaryHeap_base_03(boolean little, Comparator<T> comparator, Collection<T> collection) {
        super(little, comparator);
        this.comparator = comparator;
        addAll(collection);
    }
    
    public void addAll(Collection<T> collection) {
        for (T data : collection) {
            push(data);
        }
        heapify();
    }

    /**
     * 原地建堆, 自下而上下滤 (推荐)
     */
    public void heapify() {
        for (int i = (size >> 1) - 1; i >= 0; i--) {
            shiftDown(i);
        }
    }

}
