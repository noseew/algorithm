package org.song.algorithm.algorithmbase._02alg._03app.pending;

import org.junit.jupiter.api.Test;

public class DemoTest_02 {

    /**
     * 最大连续1
     * 给定一个数字, 转成二进制, 计算其中最长的连续为1的数量
     */
    @Test
    public void test_01() {
        System.out.println(demo_01(-1));
    }

    @Test
    public void test_02() {
        int[] ints = maxConsecutive(7, (byte) 1);
        System.out.println(ints);
    }

    /**
     * 最大连续1
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

    /**
     * 最大连续的bit, 的起止下标, 左开右闭, 大端序
     *
     * @param number
     * @param bit
     * @return
     */
    private int[] maxConsecutive(int number, byte bit) {
        int end = 0;
        int max = 0;
        int current = 0;
        for (int i = 1; i <= Integer.SIZE; i++) {
            if ((number & 1) == bit) {
                current++;
            } else {
                current = 0;
            }
            if (current > max) {
                max = current;
                end = i;
            }
            number = (number >>> 1);
        }
        int[] indexes = {end - Math.max(current, max), end};
        return indexes;
    }
}
