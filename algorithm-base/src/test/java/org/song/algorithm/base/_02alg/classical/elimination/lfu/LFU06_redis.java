package org.song.algorithm.base._02alg.classical.elimination.lfu;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._02alg.classical.counter.MorrisCounter_test;

/**
 * Redis 4.0+ 中的LFU 计数器, MorrisCounter
 */
public class LFU06_redis {
    /*
    https://blog.csdn.net/jh0218/article/details/95389361
     */
    
    @Test
    public void test01() {
        MorrisCounter_test.MorrisCounter morrisCounter = new MorrisCounter_test.MorrisCounter(1);

        for (int i = 0; i < 200000; i++) {
            morrisCounter.add();
        }
        System.out.println(morrisCounter.getCounter());
    }

}
