package org.song.algorithm.algorithmbase.datatype.bit.bitmap;

import org.junit.Test;

public class BitMap_base_01 {

    /*
    bitmap
    1. 数据结构: 一维数组长度n, 子数组长度32
        1. 如果按照bit位来看的话, 是二维数组, 数组中的最小元素是一个bit位,
            [
              [0,0,0,0,..., 0,0,0,0],
              [0,0,0,0,..., 0,0,0,0]
            ]
        2. 其中, 每个子数组的长度是32位, 正好是一个int的范围, 所以也可以使用int类型的一维数组表示
            [1,4932,5928,...,1921892]
        3. 采用大端序存储, 第1位表示数组第0位, 第2位表示数组第1位

    2. bitmap的基本计算, 时间复杂度O(1)
        1. 获取指定bit位x的值y
            int y = (1 << (x%32)) & (bitMap[x/32])
        2. 将指定bit位x设置成1
            (1 << (x%32)) | (bitMap[x/32])
        3. 将指定bit位x设置成0
            (~(1 << (x%32))) & (bitMap[x/32])
        4.
    3. bitmap其他计算
        1. 计算bitmap中1的数量, 时间复杂度O(1)
            1. 思路, 单独计算每个int元素, 单个元素采用汉明重量计算
        2. 计算bitmap区间start和end中1的数量,
            1. 思路, 头尾非完整数据单独计算, 中间完整元素采用汉明重量计算
     */
    private int[] bitMap;

    public BitMap_base_01() {

    }

    private BitMap_base_01(int totalLen) {
        int initLen = 1;
        if (totalLen < 32) {
            bitMap = new int[1];
        } else {
            while ((totalLen = (totalLen / 32)) > 0) {
                initLen++;
            }
            bitMap = new int[initLen];
        }
    }

    @Test
    public void test_01_start() {
        BitMap_base_01 bitMap_base_01 = new BitMap_base_01(33);
        System.out.println();
    }


}
