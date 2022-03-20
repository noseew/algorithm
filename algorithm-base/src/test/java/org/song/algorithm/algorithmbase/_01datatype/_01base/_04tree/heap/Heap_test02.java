package org.song.algorithm.algorithmbase._01datatype._01base._04tree.heap;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;

import java.util.Arrays;
import java.util.Random;

public class Heap_test02 {

    @Test
    public void test_start_01() {
        int size = 100;
        for (int i = 0; i < size; i++) {
            Comparable[] build = AbstractSort.build(size);
            System.out.println(Arrays.toString(build));
            Heap_base_02<Comparable> heap = new Heap_base_02<>(build);
            System.out.println(heap.toPretty());
            heap.start();
            System.out.println(Arrays.toString(build));

            isLittleHeap(heap);
        }

    }

    public static void isLittleHeap(Heap_base_02<Comparable> heap) {
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
