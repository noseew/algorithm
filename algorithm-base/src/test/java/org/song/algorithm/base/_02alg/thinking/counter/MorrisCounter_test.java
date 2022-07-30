package org.song.algorithm.base._02alg.thinking.counter;

import java.util.Random;

/**
 * 用8个bit表示大量的计数信息, 
 * 最多表示 factor * factor * counter 约等于 256_0000
 * 可以存储次数, 但是不能取出次数, 取出的次数就只能是 0-256 之间的数, 
 * 比较两个数的大小就直接拿 counter(0-256) 比较就行, 所以是不精确的, 
 *
 * 作用本身就是近似计数, 不需要精确信息, 能实现较小空间存储较大计数的功能
 */
public class MorrisCounter_test {

    /**
     * 用8个bit表示大量的计数信息, 
     * 最多表示 factor * factor * counter 约等于 256_0000
     * 可以存储次数, 但是不能取出次数, 取出的次数就只能是 0-256 之间的数, 
     * 比较两个数的大小就直接拿 counter(0-256) 比较就行, 所以是不精确的, 
     *
     * 作用本身就是近似计数, 不需要精确信息, 能实现较小空间存储较大计数的功能
     */
    public static class MorrisCounter {
        /**
         *
         */
        private int counter;
        private int factor = 0;
        private final Random r = new Random();

        public MorrisCounter(int factor) {
            this.factor = Math.max(factor, 0);
            this.factor = Math.min(this.factor, 100);
        }

        public void add() {
            double p = 1.0 / (counter * factor + 1);
            if (r.nextDouble() < p) {
                counter = 0B1111_1111 & ++counter;
            }
        }

        public int getCounter() {
            return counter;
        }

    }
}
