package org.song.algorithm.base._01datatype._01base._04tree.heap;

import org.junit.Test;
import org.song.algorithm.base._02alg._01sort.AbstractSort;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Heap_test03 {

    private Comparator<Integer> comparator = Comparator.comparing(Integer::intValue);

    /**
     * 原地建堆
     */
    @Test
    public void test_start_01_little() {
        int size = 20;
        for (int i = 0; i < size; i++) {
            List<Integer> con = AbstractSort.buildList(size);
            BinaryHeap_base_03<Integer> heap = new BinaryHeap_base_03<>(true, comparator, con);
            isLittleHeap(heap);
        }

    }
    
    @Test
    public void test_start_01_data() {
        int size = 20;
        for (int i = 0; i < size; i++) {
            List<Integer> con = AbstractSort.buildList(size);
            BinaryHeap_base_03<Integer> heap = new BinaryHeap_base_03<>(true, comparator, con);
            isValidDataHeap(heap, con);
        }

    }

    public static void isLittleHeap(BinaryHeap_base_03<Integer> heap) {
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

    public static void isValidDataHeap(BinaryHeap_base_03<Integer> heap, Collection<Integer> collection) {
        while (!heap.isEmpty()) {
            assert collection.remove(heap.pop());
        }
        assert collection.isEmpty();
    }
}
