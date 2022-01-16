package org.song.algorithm.algorithmbase.datatype._01base._01linear_list.bit.binarybase;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

public class BinaryBase_demo {

    @Test
    public void reverseTest() {
        int i = 77;
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");
        System.out.println(BinaryUtils.binaryPretty(i & 0x55555555) + "\t i & 0x55555555");
        System.out.println(BinaryUtils.binaryPretty((i & 0x55555555) << 1) + "\t (i & 0x55555555) << 1");
        System.out.println(BinaryUtils.binaryPretty(i >>> 1) + "\t i >>> 1");
        System.out.println(BinaryUtils.binaryPretty((i >>> 1) & 0x55555555) + "\t (i >>> 1) & 0x55555555");
        System.out.println(BinaryUtils.binaryPretty((i & 0x55555555) << 1 | (i >>> 1) & 0x55555555) + "\t (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555");
        i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;
        System.out.println("======================");

        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");
        System.out.println(BinaryUtils.binaryPretty(0x33333333) + "\t 0x33333333");
        System.out.println(BinaryUtils.binaryPretty(i & 0x33333333) + "\t i & 0x33333333");
        System.out.println(BinaryUtils.binaryPretty((i & 0x33333333) << 2) + "\t (i & 0x33333333) << 2");
        System.out.println(BinaryUtils.binaryPretty(i >>> 2) + "\t i >>> 2");
        System.out.println(BinaryUtils.binaryPretty((i >>> 2) & 0x33333333) + "\t (i >>> 2) & 0x33333333");
        System.out.println(BinaryUtils.binaryPretty((i & 0x33333333) << 2 | (i >>> 2) & 0x33333333) + "\t (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333");
        i = (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333;
        System.out.println("======================");

        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");
        System.out.println(BinaryUtils.binaryPretty(0x0f0f0f0f) + "\t 0x0f0f0f0f");
        System.out.println(BinaryUtils.binaryPretty((i & 0x0f0f0f0f)) + "\t (i & 0x0f0f0f0f)");
        System.out.println(BinaryUtils.binaryPretty((i & 0x0f0f0f0f) << 4) + "\t (i & 0x0f0f0f0f) << 4");
        System.out.println(BinaryUtils.binaryPretty(i >>> 4) + "\t i >>> 4");
        System.out.println(BinaryUtils.binaryPretty((i >>> 4) & 0x0f0f0f0f) + "\t (i >>> 4) & 0x0f0f0f0f");
        System.out.println(BinaryUtils.binaryPretty((i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f) + "\t (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f");
        i = (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f;
        System.out.println("======================");

        i = (i << 24) | ((i & 0xff00) << 8) |
                ((i >>> 8) & 0xff00) | (i >>> 24);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");
    }

    @Test
    public void bitcountTest() {
        System.out.println(Integer.bitCount(0B0110_1111_0000_1010));
        System.out.println(0B0110_1111_0000_1010);
        // 原始代码
        int i = 195;
        System.out.println(BinaryUtils.binaryPretty(i) + "\t 原始i : ");

        System.out.println(BinaryUtils.binaryPretty(i >>> 1) + "\t i >>> 1");
        System.out.println(BinaryUtils.binaryPretty(0x55555555) + "\t 0x55555555");
        System.out.println(BinaryUtils.binaryPretty((i >>> 1) & 0x55555555) + "\t (i >>> 1) & 0x55555555)");
        i = i - ((i >>> 1) & 0x55555555);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");

        System.out.println(BinaryUtils.binaryPretty(0x33333333) + "\t 0x33333333");
        System.out.println(BinaryUtils.binaryPretty(i & 0x33333333) + "\t i & 0x33333333");
        System.out.println(BinaryUtils.binaryPretty(i >>> 2) + "\t i >>> 2");
        System.out.println(BinaryUtils.binaryPretty((i >>> 2) & 0x33333333) + "\t (i >>> 2) & 0x33333333");
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");

        System.out.println(BinaryUtils.binaryPretty(i >>> 4) + "\t i >>> 4");
        System.out.println(BinaryUtils.binaryPretty(i + (i >>> 4)) + "\t i + (i >>> 4)");
        System.out.println(BinaryUtils.binaryPretty(0x0f0f0f0f) + "\t 0x0f0f0f0f");
        i = (i + (i >>> 4)) & 0x0f0f0f0f;
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");

        System.out.println(BinaryUtils.binaryPretty(i >>> 8) + "\t i >>> 8");
        i = i + (i >>> 8);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");

        System.out.println(BinaryUtils.binaryPretty(i >>> 16) + "\t i >>> 16");
        i = i + (i >>> 16);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");

        System.out.println(BinaryUtils.binaryPretty(0x3f) + "\t 0x3f");
        System.out.println(BinaryUtils.binaryPretty(i & 0x3f) + "\t i & 0x3f");
        int count = i & 0x3f;
        System.out.println(count);
    }

    /**
     * 计算二进制数中1的数量
     * 参考 https://segmentfault.com/a/1190000015481454
     */
    @Test
    public void bitcountTest02() {
        int i = 0B0110_1111_0000_1010;
        System.out.println(Integer.bitCount(i));
        System.out.println(i);

        System.out.println(BinaryUtils.binaryPretty(i) + "\t i");
        System.out.println(BinaryUtils.binaryPretty(i & 0x55555555) + "\t i & 0x55555555");
        System.out.println(BinaryUtils.binaryPretty((i >> 1) & 0x55555555) + "\t (i >> 1) & 0x55555555");
        // 计算每两位二进制数中1的个数
        i = (i & 0x55555555) + ((i >> 1) & 0x55555555);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i 上两数相加");
        System.out.println(BinaryUtils.binaryPretty(i & 0x33333333) + "\t i & 0x33333333");
        System.out.println(BinaryUtils.binaryPretty((i >> 2) & 0x33333333) + "\t (i >> 2) & 0x33333333");
        // 计算每四位二进制数中1的个数
        i = (i & 0x33333333) + ((i >> 2) & 0x33333333);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i 上两数相加");
        System.out.println(BinaryUtils.binaryPretty(i & 0x0F0F0F0F) + "\t i & 0x0F0F0F0F");
        System.out.println(BinaryUtils.binaryPretty((i >> 4) & 0x0F0F0F0F) + "\t (i >> 4) & 0x0F0F0F0F");
        // 计算每八位二进制数中1的个数
        i = (i & 0x0F0F0F0F) + ((i >> 4) & 0x0F0F0F0F);
        System.out.println(BinaryUtils.binaryPretty(i) + "\t i 上两数相加");
        System.out.println(BinaryUtils.binaryPretty((i * 0x01010101) >> 24) + "\t (i * 0x01010101) >> 24");
        // 将每八位二进制数中1的个数和相加, 并移至最低位八位
        i = (i * 0x01010101) >> 24;
        System.out.println(i);
    }

    @Test
    public void threadPoolStatus() {
        int COUNT_BITS = Integer.SIZE - 3;
        // COUNT_BITS = 29
        System.out.println(COUNT_BITS);
        // RUNNING = -1 << 29 = 1110_0000 0000_0000 0000_0000 0000_0000
        System.out.println(Integer.toBinaryString(-1 << COUNT_BITS));
        // SHUTDOWN = 0 << 29 = 0000_0000 0000_0000 0000_0000 0000_0000
        System.out.println(Integer.toBinaryString(0 << COUNT_BITS));
        // STOP = 1 << 29 = 0010_0000 0000_0000 0000_0000 0000_0000
        System.out.println(Integer.toBinaryString(1 << COUNT_BITS));
        // TIDYING = 2 << 29 = 0100_0000 0000_0000 0000_0000 0000_0000
        System.out.println(Integer.toBinaryString(2 << COUNT_BITS));
        // TERMINATED = 3 << 29 = 0110_0000 0000_0000 0000_0000 0000_0000
        System.out.println(Integer.toBinaryString(3 << COUNT_BITS));

        // 如果只看高3位, 他们分别是
        // RUNNING = 7
        // SHUTDOWN = 0
        // STOP = 1
        // TIDYING = 2
        // TERMINATED = 3

        // 536870911 = 1_1111 1111_1111 1111_1111 1111_1111
        System.out.println(Integer.toBinaryString((1 << COUNT_BITS) - 1));

    }

}