package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.test;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.Linked_double_01;
import org.springframework.util.StopWatch;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Linked_double_test {

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
