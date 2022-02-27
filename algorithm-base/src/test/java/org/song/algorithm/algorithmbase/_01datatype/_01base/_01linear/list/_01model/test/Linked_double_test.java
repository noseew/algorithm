package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.Linked_CycleDouble_01;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.Linked_double_01;
import org.springframework.util.StopWatch;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Linked_double_test {

    private int maxVal = 10;
    private int maxSize = 20;

    private Random r = new Random();

    @Test
    public void test_01_start() {
        Linked_double_01<Integer> deque = new Linked_double_01<>();
        deque.rpush(0);
        deque.lpush(-1);
        deque.lpush(-2);
        deque.rpush(1);
        deque.rpush(2);
        System.out.println(deque.toString());
        Integer rpop = deque.rpop();
        Integer lpop = deque.lpop();
        System.out.println();
        deque.delete(-1);
        System.out.println(deque.toString());
    }

    @Test
    public void test_cycleDouble_start() {
        Linked_CycleDouble_01<Integer> l1 = new Linked_CycleDouble_01<>();
        
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            l1.rpush(val);
        }
        System.out.println(l1.toString());
    }

    @Test
    public void test_cycleDouble_start2() {
        Linked_CycleDouble_01<Integer> l1 = new Linked_CycleDouble_01<>();
        Linked_double_01<Integer> l2 = new Linked_double_01<>();
        
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            l1.rpush(val);
            l2.rpush(val);
        }
        for (int i = 0; i < l1.length(); i++) {
            assert l1.lpop() == l2.lpop();
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

        stopWatch.start("Linked_double_02");
        Linked_double_01<String> linked = new Linked_double_01<>();
        for (int i = 0; i < num; i++) {
            linked.rpush(UUID.randomUUID().toString());
        }
        stopWatch.stop();

        stopWatch.start("LinkedList");
        List<String> liked2 = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            liked2.add(UUID.randomUUID().toString());
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
