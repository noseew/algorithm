package org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;

public class Heap_test03 {

    /**
     * 3种方式 原地建堆
     */
    @Test
    public void test_start_01() {
        int size = 100;
        for (int i = 0; i < size; i++) {
            Comparable[] build = AbstractSort.build(size);
            Heap_base_03<Comparable> heap = new Heap_base_03<>(build);
            heap.heapify1();
            isLittleHeap(heap);
            
            Comparable[] build2 = AbstractSort.build(size);
            Heap_base_03<Comparable> heap2 = new Heap_base_03<>(build2);
            heap2.heapify2();
            isLittleHeap(heap2);
            
            Comparable[] build3 = AbstractSort.build(size);
            Heap_base_03<Comparable> heap3 = new Heap_base_03<>(build3);
            heap3.heapify3();
            isLittleHeap(heap3);
        }

    }

    public static void isLittleHeap(Heap_base_03<Comparable> heap) {
        int size = heap.size;
        int last = -1;
        for (int i = 0; i < size; i++) {
            Integer pop = (int) heap.pop();
            boolean b = pop >= last;
            if (!b) {
                assert b;
            }
            last = pop;
            assert heap.size == size - i - 1;
        }
    }
}
