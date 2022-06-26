package org.song.algorithm.base._01datatype._01base._04tree.heap;

import org.junit.Test;

import java.util.*;

public class Heap_test01 {

    private Comparator<Integer> comparator = Comparator.comparing(Integer::intValue);

    @Test
    public void test_start_01() {
        Heap_base_01<Integer> heap = new Heap_base_01<>(true, comparator);
        heap.push(2);
        heap.push(1);
        heap.push(4);
        heap.push(5);
        heap.push(0);
        heap.push(3);
        System.out.println(heap.toPretty());
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
        Heap_base_01<Integer> heap = new Heap_base_01<>(true, comparator);
        for (int i = 0; i < 20; i++) {
            heap.push(random.nextInt(30));
        }
        for (int i = 0; i < 20; i++) {
            System.out.println(heap.pop());
        }
    }

    @Test
    public void test_03_auto_little() {
        int size = 1_0000;
        int maxValue = 100_0000;
        Random random = new Random();
        Heap_base_01<Integer> heap = new Heap_base_01<>(true, comparator);
        for (int i = 0; i < size; i++) {
            heap.push(random.nextInt(maxValue));
            assert heap.size == i + 1;
        }
        System.out.println("push");
        isLittleHeap(heap);
        System.out.println("OK");
    }

    @Test
    public void test_03_auto_data() {
        int size = 1_0000;
        int maxValue = 100_0000;
        Random random = new Random();
        Heap_base_01<Integer> heap = new Heap_base_01<>(true, comparator);
        List<Integer> con = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int data = random.nextInt(maxValue);
            con.add(data);
            heap.push(data);
            assert heap.size == i + 1;
        }
        System.out.println("push");
        isValidDataHeap(heap, con);
        System.out.println("OK");
    }

    @Test
    public void test_04_auto_little() {
        int size = 1_0000;
        int maxValue = 100_0000;
        Random random = new Random();
        Heap_base_02<Integer> heap = new Heap_base_02<>(true, comparator);
        for (int i = 0; i < size; i++) {
            heap.push(random.nextInt(maxValue));
            assert heap.size == i + 1;
        }
        System.out.println("push");
        isLittleHeap(heap);
        System.out.println("OK");
    }

    @Test
    public void test_04_auto_data() {
        int size = 1_0000;
        int maxValue = 100_0000;
        Random random = new Random();
        Heap_base_02<Integer> heap = new Heap_base_02<>(true, comparator);
        List<Integer> con = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int data = random.nextInt(maxValue);
            con.add(data);
            heap.push(data);
            assert heap.size == i + 1;
        }
        System.out.println("push");
        isValidDataHeap(heap, con);
        System.out.println("OK");
    }
    
    public static void isLittleHeap(Heap_base_01<Integer> heap) {
        int size = heap.size;
        int last = -1;
        for (int i = 0; i < size; i++) {
            Integer pop = heap.pop();
            assert pop >= last;
            last = pop;
            assert heap.size == size - i - 1;
        }
    }
    
    public static void isValidDataHeap(Heap_base_01<Integer> heap, Collection<Integer> collection) {
        while (!heap.isEmpty()) {
            assert collection.remove(heap.pop());
        }
        assert collection.isEmpty();
    }
}
