package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase02;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ArrayTest {
    
    private int maxVal = 100;
    private int maxSize = 1000;

    private Random r = new Random();

    @Test
    public void test_01_start() {
        ArrayBase01<Integer> list = new ArrayBase01<>(5);
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
        ArrayBase01<Integer> array = new ArrayBase01<>(5);
        List<Integer> list = new ArrayList<>(5);

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array.add(val);
            list.add(val);
        }
        // test get(index)
        for (int i = 0; i < maxSize; i++) {
            assert array.get(i) == list.get(i);
        }


        // test delete(index)
        for (int i = 0; i < maxVal; i++) {
            array.delete(i);
            list.remove(i);
        }
        for (int i = 0; i < list.size(); i++) {
            assert array.get(i) == list.get(i);
        }

        // test insert(index)
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

    @Test
    public void test_share_test() {
        ArrayBase02<Integer> array2 = new ArrayBase02<>(4);
        maxSize = 10;

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array2.add(val);
        }

        array2.clean();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array2.add(val);
        }
    }


    @Test
    public void test_auto_share02() {
        ArrayBase01<Integer> array = new ArrayBase01<>(1);
        ArrayBase02<Integer> array2 = new ArrayBase02<>(1);
        maxSize = 10_0000;
        
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array.add(val);
            array2.add(val);
        }
        // test get(index)
        for (int i = 0; i < maxSize; i++) {
            assert array.get(i) == array2.get(i);
        }

        // test insert(index)
        array.clean();
        array2.clean();
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array.add(val);
            array2.add(val);
        }
        for (int i = 0; i < maxSize; i++) {
            assert array.get(i) == array2.get(i);
        }
    }

    /**
     * 这里显示, 这个均摊复杂度并没有提高效率
     */
    @Test
    public void test_share_perf02() {
        ArrayBase01<Integer> array1 = new ArrayBase01<>(4);
        ArrayBase02<Integer> array2 = new ArrayBase02<>(4);

        maxSize = 1_000_0000;

        StopWatch sw = new StopWatch();

        sw.start("array1");
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array1.add(val);
        }
        for (int i = 0; i < maxSize; i++) {
            array1.get(i);
        }
        sw.stop();

        sw.start("array2");
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            array2.add(val);
        }
        for (int i = 0; i < maxSize; i++) {
            array2.get(i);
        }
        sw.stop();

        System.out.println(sw.prettyPrint());

        for (int i = 0; i < maxSize; i++) {
            assert array1.get(i) == array2.get(i);
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
        ArrayBase01<String> list = new ArrayBase01<>();
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
