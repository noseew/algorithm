package org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._02queue_stack._01model.queue.*;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueCycleTest {

    private int maxVal = 100;
    private int maxSize = 10;

    private Random r = new Random();
    
    @Test
    public void queueCycleArray_test01() throws InterruptedException {

        ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<>(maxSize);
        Queue_CycleArray_01<Integer> q1 = new Queue_CycleArray_01<>(maxSize);
        Queue_CycleArray_01<Integer> q1_r = new Queue_CycleArray_01<>(maxSize);
        Queue_CycleArray_02<Integer> q2 = new Queue_CycleArray_02<>(maxSize);
        Queue_CycleArray_02<Integer> q2_r = new Queue_CycleArray_02<>(maxSize);

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q1_r.lpush(val);
            q2.rpush(val);
            q2_r.lpush(val);
            q.put(val);
        }
        assert q1.length() == q.size();
        assert q1_r.length() == q2_r.length();
        assert q1.length() == q1_r.length();
        for (int i = 0; i < q1.length(); i++) {
            Integer poll = q.poll();
            assert q1.lpop() == poll;
            assert q1_r.rpop() == poll;
            assert q2.lpop() == poll;
            assert q2_r.rpop() == poll;
        }
        
        
        for (int i = 0; i < maxSize / 2; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q1_r.lpush(val);
            q2.rpush(val);
            q2_r.lpush(val);
            q.put(val);
        }
        for (int i = 0; i < q1.length(); i++) {
            Integer poll = q.poll();
            assert q1.lpop() == poll;
            assert q1_r.rpop() == poll;
            assert q2.lpop() == poll;
            assert q2_r.rpop() == poll;
        }
        assert q1.length() == q.size();
        assert q1_r.length() == q2_r.length();
        assert q1.length() == q1_r.length();
    }
    
    @Test
    public void queueCycleArray_test02() {

        Queue_CycleArray_01<Integer> q1 = new Queue_CycleArray_01<>(maxSize);
        Queue_CycleArray_02<Integer> q2 = new Queue_CycleArray_02<>(maxSize);

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.rpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.lpop();
        }
        
        
        for (int i = 0; i < maxSize / 2; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.rpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.lpop();
        }
    }
    
    @Test
    public void queueCycleArray_test03() {

        Queue_CycleArray_01<Integer> q1 = new Queue_CycleArray_01<>(maxSize);
        Queue_CycleArray_02<Integer> q2 = new Queue_CycleArray_02<>(maxSize);

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.lpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.rpop();
        }
        
        
        for (int i = 0; i < maxSize / 2; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.lpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.rpop();
        }
    }
    
    @Test
    public void queueCycle_test02() {

        Queue_CycleArray_01<Integer> q1 = new Queue_CycleArray_01<>(maxSize);
        Queue_CycleLink_01<Integer> q2 = new Queue_CycleLink_01<>(maxSize);

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.rpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.lpop();
        }
        
        
        for (int i = 0; i < maxSize / 2; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.rpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.lpop();
        }
    }
    
    @Test
    public void queueCycleLink_test() {

        Queue_CycleLink_01<Integer> q1 = new Queue_CycleLink_01<>(maxSize);
        Queue_CycleLink_01<Integer> q2 = new Queue_CycleLink_01<>(maxSize);

        for (int i = 0; i < maxSize; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.lpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.rpop();
        }
        
        
        for (int i = 0; i < maxSize / 2; i++) {
            int val = r.nextInt(maxVal);
            q1.rpush(val);
            q2.lpush(val);
        }
        assert q1.length() == q2.length();
        for (int i = 0; i < q1.length(); i++) {
            assert q1.lpop() == q2.rpop();
        }
    }
}
