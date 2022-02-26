package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.Array_base_01;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Array_test {
    
    private int maxVal = 100;
    private int maxSize = 1000;

    private Random r = new Random();

    @Test
    public void test_01_start() {
        Array_base_01<Integer> list = new Array_base_01<>(5);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(list.toString());
        list.insert(3, 4);
        System.out.println(list.toString());
        list.delete(2);
        System.out.println(list.toString());
        list.clean();
        System.out.println(list.toString());

    }

    @Test
    public void test_auto_test01() {
        Array_base_01<Integer> array = new Array_base_01<>(5);
        List<Integer> list = new ArrayList<>(5);

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array.add(val);
            list.add(val);
        }
        for (int i = 0; i < maxSize; i++) {
            assert array.get(i) == list.get(i);
        }

        
        for (int i = 0; i < maxVal; i++) {
            array.delete(i);
            list.remove(i);
        }
        for (int i = 0; i < list.size(); i++) {
            assert array.get(i) == list.get(i);
        }

        array.clean();
        list.clear();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array.add(val);
            list.add(val);
        }
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal - 1);
            int index = r.nextInt(maxSize - 1);
            array.insert(val, index);
            list.add(index, val);
        }
        for (int i = 0; i < maxSize; i++) {
            assert array.get(i) == list.get(i);
        }
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
