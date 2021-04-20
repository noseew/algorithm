package org.song.algorithm.algorithmbase.datatype;

import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Linked_single_test {

    @Test
    public void test_01_start() {
        Linked_single_01<Integer> linked = new Linked_single_01<>();
        linked.add(1);
        linked.add(2);
        linked.add(3);
        linked.add(4);
        linked.add(5);
        System.out.println(linked.toString());
        linked.remove(3);
        System.out.println(linked.toString());
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

        stopWatch.start("Linked_single_01");
        Linked_single_01<String> linked = new Linked_single_01<>();
        for (int i = 0; i < num; i++) {
            linked.add(UUID.randomUUID().toString());
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
