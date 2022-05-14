package org.song.algorithm.base._01datatype._01base._01linear.bit.bitmap.test;

import org.junit.jupiter.api.Test;

import java.util.BitSet;

public class BitSetTest {

    /**
     * DK 自带的位图工具
     */
    @Test
    public void test01() {
        // JDK 自带的位图工具
        // 初始化位图 
        BitSet bitSet = new BitSet(10);
        bitSet = BitSet.valueOf(new long[]{1L});

        // 设置值
        bitSet.set(1);
        // 获取值
        bitSet.get(1);
        // 位图与运算
        bitSet.and(bitSet);
        // 位图或运算
        bitSet.or(bitSet);
        // 位图异或运算
        bitSet.xor(bitSet);
        // 反转指定位置?
        bitSet.flip(1);

        // 下一个设置了值的位置的下标?
        bitSet.nextSetBit(1);
        // 上一个未设置值的位置的下标?
        bitSet.previousClearBit(1);
        // 获取子集
        bitSet.get(0, 1);
        // 获取数组
        bitSet.toLongArray();

    }
}
