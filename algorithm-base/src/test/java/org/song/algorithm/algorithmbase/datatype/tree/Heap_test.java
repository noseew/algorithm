package org.song.algorithm.algorithmbase.datatype.tree;

import org.junit.Test;

public class Heap_test {

    @Test
    public void test() {
        Heap_base_01<Integer> heap = new Heap_base_01<>();
        heap.push(5);
        heap.push(4);
        heap.push(3);
        heap.push(2);
        heap.push(1);
        heap.push(0);
        System.out.println();
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());

    }
}
