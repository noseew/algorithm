package org.song.algorithm.base._01datatype._01base._02queue_stack._01model.queue.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._01base._02queue_stack._01model.queue.Queue_Array_01;
import org.song.algorithm.base._01datatype._01base._02queue_stack._01model.queue.Queue_Link_01;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueTest {

    private final int maxVal = 100;
    private final int maxSize = 10;

    private Random r = new Random();
    
    @Test
    public void queueList_test01() throws InterruptedException {

        Queue_Link_01<Integer> queue = new Queue_Link_01<>();
        Queue<Integer> q = new LinkedList<>();

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            queue.lpush(val);
            q.offer(val);
        }

        assert queue.length() == q.size();

        for (int i = 0; i < queue.length(); i++) {
            assert queue.rpop() == q.poll();
        }
    }
    
    @Test
    public void queueArray_test01() throws InterruptedException {

        Queue_Array_01<Integer> queue = new Queue_Array_01<>();
        Queue<Integer> q = new LinkedList<>();

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            queue.lpush(val);
            q.offer(val);
        }

        assert queue.length() == q.size();

        for (int i = 0; i < queue.length(); i++) {
            assert queue.rpop() == q.poll();
        }
    }
}
