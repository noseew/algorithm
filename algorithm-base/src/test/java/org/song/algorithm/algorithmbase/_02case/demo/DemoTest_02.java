package org.song.algorithm.algorithmbase._02case.demo;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

public class DemoTest_02 {

    /**
     * 给定一个数字, 转成二进制, 计算其中最长的连续为1的数量
     */
    @Test
    public void test_01() {
        System.out.println(demo_01(-1));
    }

    /**
     *
     */
    public int demo_01(int number) {
        System.out.println(Integer.toBinaryString(number));
        int max = 0;
        int current = 0;

        for (int i = 0; i < 32; i++) {
            if ((number & 1) == 1) {
                current++;
            } else {
                max = Math.max(current, max);
                current = 0;
            }
            number = (number >>> 1);
        }
        return Math.max(current, max);
    }
}
