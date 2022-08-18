package org.song.algorithm.base._01datatype._02high.hyperloglog;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

/**
 * hyperloglog
 */
public class HLL_base_01 {

    @Test
    public void main() {
        for (int i = 0; i < 100000; i++) {
            Experiment experiment = new Experiment(i);
            experiment.work();
            experiment.debug();
        }
    }

    /**
     * 伯努利试验 中基数n与kmax之间的关系  n = 2^kmax
     */
    static class BitKeeper {

        /**
         * 记录最大的低位0的长度
         */
        private int kmax;

        public void random() {
            // 生成随机数
            long value = ThreadLocalRandom.current().nextLong(2L << 32);
            int len = this.lowZerosMaxLength(value);
            if (len > kmax) {
                kmax = len;
            }
        }

        /**
         * 计算低位0的长度
         * 这里如果不理解看下我的注释
         * value >> i 表示将value右移i,  1<= i <32 ， 低位会被移出
         * value << i 表示将value左移i,  1<= i <32 ， 低位补0
         * 看似一左一右相互抵消，但是如果value低位是0右移被移出后，左移又补回来，这样是不会变的，但是如果移除的是1，补回的是0，那么value的值就会发生改变
         * 综合上面的方法，就能比较巧妙的计算低位0的最大长度
         *
         * @param value
         * @return
         */
        private int lowZerosMaxLength(long value) {
            int i = 1;
            for (; i < 32; i++) {
                if (value >> i << i != value) {
                    break;
                }
            }
            return i - 1;
        }

    }

    static class Experiment {
        /**
         * 测试次数n
         */
        private int n;
        private BitKeeper bitKeeper;

        public Experiment(int n) {
            this.n = n;
            this.bitKeeper = new BitKeeper();
        }

        public void work() {
            for (int i = 0; i < n; i++) {
                this.bitKeeper.random();
            }
        }

        /**
         * 输出每一轮测试次数n
         * 输出 logn / log2 = k 得 2^k = n，这里的k即我们估计的kmax
         * 输出 kmax，低位最大0位长度值
         */
        public void debug() {
            System.out.printf("测试次数 %d, 估计的kmax=%.2f, 实际的kmax=%d (低位最大0位长度值)\n", this.n, Math.log(this.n) / Math.log(2), this.bitKeeper.kmax);
        }
    }

}
