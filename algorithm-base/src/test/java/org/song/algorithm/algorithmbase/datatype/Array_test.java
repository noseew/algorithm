package org.song.algorithm.algorithmbase.datatype;

import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * -Xint
     * 效率接近
     */
    @Test
    public void test_02_perf_add() {
        int num = 10_0000;
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("Array_base_01");
        Array_base_01<String> list = new Array_base_01<>();
        for (int i = 0; i < num; i++) {
            list.add(UUID.randomUUID().toString());
        }
        stopWatch.stop();

        stopWatch.start("ArrayList");
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list2.add(UUID.randomUUID().toString());
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
