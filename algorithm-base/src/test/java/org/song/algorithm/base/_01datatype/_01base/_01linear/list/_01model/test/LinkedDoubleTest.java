package org.song.algorithm.base._01datatype._01base._01linear.list._01model.test;

import org.junit.Test;
import org.song.algorithm.base._01datatype._01base._01linear.list._01model.LinkedDouble01;
import org.song.algorithm.base._01datatype._01base._01linear.list._01model.LinkedDoubleCycle01;
import org.springframework.util.StopWatch;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class LinkedDoubleTest {

    private int maxVal = 10;
    private int maxSize = 20;

    private Random r = new Random();

    @Test
    public void test_01_start() {
        LinkedDouble01<Integer> deque = new LinkedDouble01<>();
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
        LinkedDoubleCycle01<Integer> l1 = new LinkedDoubleCycle01<>();
        
        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            l1.rpush(val);
        }
        System.out.println(l1.toString());
    }

    @Test
    public void test_cycleDouble_start2() {
        LinkedDoubleCycle01<Integer> l1 = new LinkedDoubleCycle01<>();
        LinkedDouble01<Integer> l2 = new LinkedDouble01<>();
        
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
        LinkedDouble01<String> linked = new LinkedDouble01<>();
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
