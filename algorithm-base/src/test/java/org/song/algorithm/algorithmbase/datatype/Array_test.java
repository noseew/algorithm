package org.song.algorithm.algorithmbase.datatype;

import org.junit.Test;

import java.util.UUID;

public class Array_test {

    @Test
    public void test_01_start() {
        Array_base_01<Integer> list = new Array_base_01<>(5);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(list.toString());
        list.remove(2);
        System.out.println(list.toString());
        list.clear();
        System.out.println(list.toString());

    }

    @Test
    public void test_02_perf_add() {
        int num = 10;

        for (int i = 0; i < num; i++) {
            UUID.randomUUID();
        }
    }
}
