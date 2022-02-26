package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.Linked_single_01;
import org.springframework.util.StopWatch;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Linked_single_test {

    private int maxVal = 10;
    private int maxSize = 20;

    private Random r = new Random();

    @Test
    public void test_01_start() {
        Linked_single_01<Integer> linked = new Linked_single_01<>();
        linked.addTail(1);
        linked.addTail(2);
        linked.addTail(3);
        linked.addTail(4);
        linked.addTail(5);
        System.out.println(linked.toString());
        linked.removeVal(3);
        System.out.println(linked.toString());
    }

    @Test
    public void test_auto_test01() {
        Linked_single_01<Integer> linked = new Linked_single_01<>();
        List<Integer> list = new LinkedList<>();
        
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            linked.addTail(val);
            list.add(val);
        }
        for (int i = 0; i < maxSize; i++) {
            assert linked.getByIndex(i) == list.get(i);
        }
        
        for (int i = 0; i < list.size(); i++) {
            int val = r.nextInt(list.size() - 1);
            linked.removeIndex(val);
            list.remove(val);
        }
        for (int i = 0; i < list.size(); i++) {
            assert linked.getByIndex(i) == list.get(i);
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

        stopWatch.start("LinkedList");
        List<String> liked2 = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            liked2.add(UUID.randomUUID().toString());
        }
        stopWatch.stop();

        stopWatch.start("Linked_single_02");
        Linked_single_01<String> linked = new Linked_single_01<>();
        for (int i = 0; i < num; i++) {
            linked.addTail(UUID.randomUUID().toString());
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
