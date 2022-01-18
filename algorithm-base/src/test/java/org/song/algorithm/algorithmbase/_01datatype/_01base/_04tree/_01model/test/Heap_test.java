package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Heap_base_01;

import java.util.Random;

public class Heap_test {

    @Test
    public void test_start_01() {
        Heap_base_01<Integer> heap = new Heap_base_01<>();
        heap.push(2);
        heap.push(1);
        heap.push(4);
        heap.push(5);
        heap.push(0);
        heap.push(3);
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.toPretty());

    }

    @Test
    public void test_start_02() {
        Random random = new Random();
        Heap_base_01<Integer> heap = new Heap_base_01<>();
        for (int i = 0; i < 20; i++) {
            heap.push(random.nextInt(30));
        }
        for (int i = 0; i < 20; i++) {
            System.out.println(heap.pop());
        }
    }
}
