package org.song.algorithm.base._01datatype._01base._01linear.bit._03app.bitmap._01model.test;

import org.junit.Test;
import org.song.algorithm.base._01datatype._01base._01linear.bit._03app.bitmap._01model.BitMap02;
import org.song.algorithm.base.utils.BinaryUtils;

/**
 * 基于 long 64位, 
 * 小端序实现的bitmap
 */
public class BitMap_base_02 {


    @Test
    public void test_01_start() {
        System.out.println(0x8000_0000_0000_0000L);
        System.out.println(BinaryUtils.binaryPretty(0x8000_0000_0000_0000L));
        System.out.println(BinaryUtils.binaryPretty(-1L));

        BitMap02 BitMap02 = new BitMap02(32);

        BitMap02.setBit(2);
        BitMap02.setBit(4);

        System.out.println(BitMap02.getBit(4));
        System.out.println(BitMap02.getBit(5));
    }

    @Test
    public void test_01_bitCount() {
        BitMap02 BitMap02 = new BitMap02(32);

        BitMap02.setBit(0);
        BitMap02.setBit(3);
        BitMap02.setBit(63);

        BitMap02.setBit(64);
        BitMap02.setBit(65);
        BitMap02.setBit(127);

        BitMap02.setBit(128);
        BitMap02.setBit(129);

        System.out.println(BitMap02.bitCount()); // 8
        System.out.println(BitMap02.bitCount(0, 3)); // 1
        System.out.println(BitMap02.bitCount(0, 127)); // 5

        System.out.println(BitMap02.bitCount(3, 63)); // 1
        System.out.println(BitMap02.bitCount(3, 127)); // 4
        System.out.println(BitMap02.bitCount(3, 130)); // 7

        System.out.println(BitMap02.bitCount(3, 129)); // 6

    }

    @Test
    public void test_01_leftRight() {
        BitMap02 BitMap02 = new BitMap02(1);

        BitMap02.setBit(1);
        BitMap02.setBit(2);
        BitMap02.setBit(31);

        BitMap02.setBit(32);
        BitMap02.setBit(35);
        BitMap02.setBit(63);

        BitMap02.rightShift(2);
        BitMap02.leftShift(2);
    }

    @Test
    public void test_01_bit() {
        BitMap02 BitMap02 = new BitMap02(1);

        BitMap02.setBit(1);
        BitMap02.setBit(2);
        BitMap02.setBit(31);

        BitMap02.setBit(32);
        BitMap02.setBit(35);
        BitMap02.setBit(63);

//        BitMapBase02.and(new int[]{14});
//        BitMapBase02.or(new int[]{3});
//        BitMapBase02.not();

        BitMap02 bitMap1 = BitMap02.notNew();
        System.out.println();
    }

}
