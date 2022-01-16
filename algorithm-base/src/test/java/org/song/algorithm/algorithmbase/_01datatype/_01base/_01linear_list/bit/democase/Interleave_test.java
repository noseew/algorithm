package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.bit.democase;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

import java.util.Random;

public class Interleave_test {

    @Test
    public void test() {
        System.out.println(BinaryUtils.binaryPretty(0xaaaaaaaaaaaaaaaaL));
    }

    @Test
    public void test01() {
        System.out.println(BinaryUtils.binaryPretty(-1));
        System.out.println(BinaryUtils.binaryPretty(0));
        System.out.println("start");
        long interleave = interleave(-1, 0);
        System.out.println("end");
        System.out.println(BinaryUtils.binaryPretty(interleave));
    }

    @Test
    public void test02() {
        System.out.println(BinaryUtils.binaryPretty(0x5555_5555_5555_5555L>>>1));
        System.out.println("start");
        long deinterleave = deinterleave(0x5555_5555_5555_5555L>>>1);
        System.out.println("end");
        System.out.println(BinaryUtils.binaryPretty(deinterleave));

    }
    @Test
    public void test03() {
        Random random = new Random(Integer.MAX_VALUE);
        int x = random.nextInt();
        int y = random.nextInt();
        System.out.println(BinaryUtils.binaryPretty(x));
        System.out.println(BinaryUtils.binaryPretty(y));
        System.out.println("start");
        long interleave = interleave(x, y);
        System.out.println("end");
        System.out.println(BinaryUtils.binaryPretty(interleave));


        System.out.println("start");
        long deinterleave = deinterleave(interleave);
        System.out.println("end");
        System.out.println(BinaryUtils.binaryPretty(deinterleave));

        System.out.println(BinaryUtils.binaryPretty(0x0000_0000_ffff_ffffL & deinterleave));
        System.out.println(BinaryUtils.binaryPretty(0x0000_0000_ffff_ffffL & (deinterleave >>> 32)));
        
    }

    /**
     * 将两个32位的数, 按照bit位交叉成64bit的数
     * 效率O(logn), n = 总bit位数, 由于固定是64位, 所以可以理解为O(1)
     * y在高位, x在低位
     *
     * @param xOlg
     * @param yOlg
     * @return
     */
    public long interleave(int xOlg, int yOlg) {
        long _32 = 0x0000_0000_FFFF_FFFFL; // 00000000_00000000 00000000_00000000 11111111_11111111 11111111_11111111
        long _16 = 0x0000_FFFF_0000_FFFFL; // 00000000_00000000 11111111_11111111 00000000_00000000 11111111_11111111
        long _8 = 0x00FF_00FF_00FF_00FFL; // 00000000_11111111 00000000_11111111 00000000_11111111 00000000_11111111
        long _4 = 0x0F0F_0F0F_0F0F_0F0FL; // 00001111_00001111 00001111_00001111 00001111_00001111 00001111_00001111
        long _2 = 0x3333_3333_3333_3333L; // 00110011_00110011 00110011_00110011 00110011_00110011 00110011_00110011
        long _1 = 0x5555_5555_5555_5555L; // 01010101_01010101 01010101_01010101 01010101_01010101 01010101_01010101

        long x = xOlg;
        x &= _32;
        long y = yOlg;
        y &= _32;

        /*
        原始数据从每16位开始二分, 左边左移到32位开始(从低位计算)
        示例:
            原始数据 x  = 0000_0000 0000_0000 1111_1111 1111_1111
            (x << 16) = 1111_1111 1111_1111 0000_0000 0000_0000
            | x       = 1111_1111 1111_1111 1111_1111 1111_1111
            & _16     = 0000_0000 0000_0000 1111_1111 1111_1111
         */
        //        System.out.println(BinaryUtils.binaryPretty(x);
        x = ((x << 16) | x) & _16;
        //        System.out.println(BinaryUtils.binaryPretty(x);
        y = ((y << 16) | y) & _16;

        /*
        原始数据从每8位开始二分, 左边左移到16位开始(从低位计算)
        示例:
            原始数据 x  = 0000_0000 0000_0000 1111_1111 1111_1111
            (x << 8)  = 0000_0000 1111_1111 1111_1111 0000_0000
            | x       = 0000_0000 1111_1111 1111_1111 1111_1111
            & _8      = 0000_0000 1111_1111 0000_0000 1111_1111
        
         */
        x = ((x << 8) | x) & _8;
        //        System.out.println(BinaryUtils.binaryPretty(x);
        y = ((y << 8) | y) & _8;

        /*
        原始数据从每4位开始二分, 左边左移到8位开始(从低位计算)
        示例:
            原始数据 x  = 0000_0000 1111_1111 0000_0000 1111_1111
            (x << 4)  = 0000_1111 1111_0000 0000_1111 1111_0000
            | x       = 0000_1111 1111_1111 1111_1111 1111_1111
            & _4      = 0000_1111 0000_1111 0000_1111 0000_1111
         */
        x = ((x << 4) | x) & _4;
        //        System.out.println(BinaryUtils.binaryPretty(x);
        y = ((y << 4) | y) & _4;

        x = ((x << 2) | x) & _2;
        //        System.out.println(BinaryUtils.binaryPretty(x);
        y = ((y << 2) | y) & _2;

        x = ((x << 1) | x) & _1;
        //        System.out.println(BinaryUtils.binaryPretty(x);
        y = ((y << 1) | y) & _1;
        /*
        以此类推, 最终将原始32bit均匀的分在64bit中, 同时间隔补0
        其中一个数, 左移一位然后拼接
         */
        return x | (y << 1);
    }

    /**
     * 将一个64位的数, 按照bit位交叉换成两个32位的数, 并前后拼成64bit的数
     * y在高位, x在低位
     *
     * @param xOlg
     * @return
     */
    public long deinterleave(long xOlg) {
        long _32 = 0x0000_0000_FFFF_FFFFL; // 00000000_00000000 00000000_00000000 11111111_11111111 11111111_11111111
        long _16 = 0x0000_FFFF_0000_FFFFL; // 00000000_00000000 11111111_11111111 00000000_00000000 11111111_11111111
        long _8 = 0x00FF_00FF_00FF_00FFL; // 00000000_11111111 00000000_11111111 00000000_11111111 00000000_11111111
        long _4 = 0x0F0F_0F0F_0F0F_0F0FL; // 00001111_00001111 00001111_00001111 00001111_00001111 00001111_00001111
        long _2 = 0x3333_3333_3333_3333L; // 00110011_00110011 00110011_00110011 00110011_00110011 00110011_00110011
        long _1 = 0x5555_5555_5555_5555L; // 01010101_01010101 01010101_01010101 01010101_01010101 01010101_01010101

        long x = xOlg;
        long y = xOlg >>> 1;
        System.out.println(BinaryUtils.binaryPretty(x));

        x = (x | (x >>> 0)) & _1;
//        System.out.println(BinaryUtils.binaryPretty(x));
        y = (y | (y >>> 0)) & _1;

        x = (x | (x >>> 1)) & _2;
//        System.out.println(BinaryUtils.binaryPretty(x));
        y = (y | (y >>> 1)) & _2;

        x = (x | (x >>> 2)) & _4;
//        System.out.println(BinaryUtils.binaryPretty(x));
        y = (y | (y >>> 2)) & _4;

        x = (x | (x >>> 4)) & _8;
//        System.out.println(BinaryUtils.binaryPretty(x));
        y = (y | (y >>> 4)) & _8;

        x = (x | (x >>> 8)) & _16;
//        System.out.println(BinaryUtils.binaryPretty(x));
        y = (y | (y >>> 8)) & _16;

        x = (x | (x >>> 16)) & _32;
//        System.out.println(BinaryUtils.binaryPretty(x));
        y = (y | (y >>> 16)) & _32;

        return x | (y << 32);
    }
}
