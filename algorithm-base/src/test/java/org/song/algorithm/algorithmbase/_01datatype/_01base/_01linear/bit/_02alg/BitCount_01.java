package org.song.algorithm.algorithmbase._01datatype._01base._01linear.bit._02alg;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BitCount_01 {
    /**
     * 查找表, 采用8个字节, 最多256个数
     * 下标表示对应数字的值
     * 元素表示对应元素值的bitCount
     */
    private static int[] bitCountTable;
    private static final int tableBit = 8;
    private static final int tableBitTag = 0b1111_1111;
    static {
        bitCountTable = new int[tableBitTag + 1];
        for (int i = 0; i < bitCountTable.length; i++) {
            // 下标表示 int 值, value表示 bitCount 数
            bitCountTable[i] = hammingWeight(i);
        }
    }


    @Test
    public void test01() throws InterruptedException {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(Integer.MAX_VALUE);
            System.out.println(bitCountTraverse(n));
            System.out.println(bitCountTraverse2(n));
            System.out.println(bitCountByTable(n));
            System.out.println(hammingWeight(n));
            System.out.println(Integer.bitCount(n));
            System.out.println();
            TimeUnit.SECONDS.sleep(1);
        }
    }

    /**
     * 一位一位的数, 固定32次
     * 
     * @param n
     * @return
     */
    public static int bitCountTraverse(int n) {
        int c = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & 1) == 1) {
                c += 1;
            }
            n = n >> 1;
        }
        return c;
    }

    /**
     * 采用 (n - 1) & n, 循环1的次数, 最少0, 最多32
     * 
     * @param n
     * @return
     */
    public static int bitCountTraverse2(int n) {
        int c = 0;
        while (n != 0) {
            ++c;
            /*
            (n - 1) & n : 相当于将这个数去掉末尾的1, 当去掉所有的1之后 n==0 循环结束
             */
            n = (n - 1) & n;
        }
        return c;
    }

    /**
     * 采用查找表, 空间换时间, 根据表的大小查找次数不定, 这里表的大小8位, 所以循环次数 固定 32/8 = 4次
     * 
     * @param n
     * @return
     */
    public static int bitCountByTable(int n) {
        int c = 0;
        for (int i = 0; i <= 32; i += tableBit) {
            c += bitCountTable[tableBitTag & n];
            n = n >>> tableBit;
        }
        return c;
    }

    /**
     * 计算汉明重量
     * 采用按位与求和, 计算次数固定 5次, log_2(32)
     * 
     * @param n
     * @return
     */
    public static int hammingWeight(int n) {
        int c = 0;
        c = (n & 0B0101_0101_0101_0101_0101_0101_0101_0101)
                + ((n >>> 1) & 0B0101_0101_0101_0101_0101_0101_0101_0101);
        c = (c & 0B0011_0011_0011_0011_0011_0011_0011_0011)
                + ((c >>> 2) & 0B0011_0011_0011_0011_0011_0011_0011_0011);
        c = (c & 0B0000_1111_0000_1111_0000_1111_0000_1111)
                + ((c >>> 4) & 0B0000_1111_0000_1111_0000_1111_0000_1111);
        c = (c & 0B0000_0000_1111_1111_0000_0000_1111_1111)
                + ((c >>> 8) & 0B0000_0000_1111_1111_0000_0000_1111_1111);
        c = (c & 0B0000_0000_0000_0000_1111_1111_1111_1111)
                + ((c >>> 16) & 0B0000_0000_0000_0000_1111_1111_1111_1111);
        return c;
    }
}
