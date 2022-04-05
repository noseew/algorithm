package org.song.algorithm.base._01datatype._01base._01linear.bit.bitmap.test;

import org.junit.Test;
import org.song.algorithm.base._01datatype._01base._01linear.bit.bitmap.BitMap01;

import java.util.Arrays;

/**
 * 基于 int 32位, 大端序存储(也是int默认存储方式) 实现的bitmap
 */
public class BitMap_base_01 {

    @Test
    public void test_01_start() {
        BitMap01 BitMap01 = new BitMap01(1);

        BitMap01.setBit(0);
        System.out.println(BitMap01.getBit(0));
        BitMap01.setBit(1);
        System.out.println(BitMap01.getBit(1));
        BitMap01.setBit(31);
        System.out.println(BitMap01.getBit(31));
        BitMap01.setBit(32);
        System.out.println(BitMap01.getBit(32));

        System.out.println(BitMap01.getBit(4)); // 1
        System.out.println(BitMap01.getBit(5)); // 0
    }

    @Test
    public void test_01_bitCount() {
        BitMap01 BitMap01 = new BitMap01(32);

        BitMap01.setBit(0);
        BitMap01.setBit(3);
        BitMap01.setBit(31);

        BitMap01.setBit(32);
        BitMap01.setBit(35);
        BitMap01.setBit(63);

        BitMap01.setBit(64);
        BitMap01.setBit(65);

        System.out.println(BitMap01.bitCount()); // 8
        System.out.println(BitMap01.bitCount(0, 3)); // 1
        System.out.println(BitMap01.bitCount(0, 36)); // 5

        System.out.println(BitMap01.bitCount(3, 31)); // 1
        System.out.println(BitMap01.bitCount(3, 63)); // 4
        System.out.println(BitMap01.bitCount(3, 95)); // 7

        System.out.println(BitMap01.bitCount(3, 65)); // 6

    }

    @Test
    public void test_01_leftRight() {
        BitMap01 BitMap01 = new BitMap01(1);

        BitMap01.setBit(1);
        BitMap01.setBit(2);
        BitMap01.setBit(31);

        BitMap01.setBit(32);
        BitMap01.setBit(35);
        BitMap01.setBit(63);

        BitMap01.rightShift(2);
        BitMap01.leftShift(2);
    }

    @Test
    public void test_01_bit() {
        BitMap01 BitMap01 = new BitMap01(1);

        BitMap01.setBit(1);
        BitMap01.setBit(2);
        BitMap01.setBit(31);

        BitMap01.setBit(32);
        BitMap01.setBit(35);
        BitMap01.setBit(63);

//        BitMapBase.and(new int[]{14});
//        BitMapBase.or(new int[]{3});
//        BitMapBase.not();

        BitMap01 bitMap1 = BitMap01.notNew();
        System.out.println();
    }

    @Test
    public void test_01_consecutive() {
        BitMap01 BitMap01 = new BitMap01(1);

        BitMap01.setBit(1);
        BitMap01.setBit(2);
        BitMap01.setBit(3);
        BitMap01.setBit(31);

        for (int i = 0; i < 32; i++) {
            BitMap01.setBit(32 + i);
        }
        BitMap01.setBit(64);
        BitMap01.setBit(65);

        BitMap01.setBit(67);

        System.out.println(BitMap01.consecutive());
    }

    @Test
    public void test_check() {
//        MD5:key=3, code=46372
//        SHA:key=3, code=1055
        BitMap01 BitMap01 = new BitMap01();
        BitMap01.setBit(46372);
        System.out.println(BitMap01.getBit(46372));
        BitMap01.setBit(1055);
        System.out.println(BitMap01.getBit(1055));
    }

    @Test
    public void test_getValue() {
        BitMap01 BitMap01 = new BitMap01();
        BitMap01.setBit(0);
        System.out.println(Arrays.toString(BitMap01.getValues()));
        BitMap01.setBit(1);
        System.out.println(Arrays.toString(BitMap01.getValues()));
        BitMap01.setBit(31);
        System.out.println(Arrays.toString(BitMap01.getValues()));
        BitMap01.setBit(32);
        System.out.println(Arrays.toString(BitMap01.getValues()));
        BitMap01.setBit(63);
        System.out.println(Arrays.toString(BitMap01.getValues()));
        BitMap01.setBit(64);
        System.out.println(Arrays.toString(BitMap01.getValues()));
    }


}
