package org.song.algorithm.algorithmbase._01datatype._01base._01linear.bit._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

public class BitSimple {

    @Test
    public void reverseTest() {
        int i = 77;
        System.out.println(BinaryUtils.binaryPretty(i));
        System.out.println(BinaryUtils.binaryPretty(reverse(i)));
    }

    /**
     * 翻转二进制数
     *
     * @param i
     */
    private int reverse(int i) {
        i = (i & 0x55555555) << 1 | (i >>> 1) & 0x55555555;
        i = (i & 0x33333333) << 2 | (i >>> 2) & 0x33333333;
        i = (i & 0x0f0f0f0f) << 4 | (i >>> 4) & 0x0f0f0f0f;
        i = (i << 24) | ((i & 0xff00) << 8) |
                ((i >>> 8) & 0xff00) | (i >>> 24);
        return i;
    }

    @Test
    public void upPowerTest() {
        int i = 5;
        System.out.println(BinaryUtils.binaryPretty(i));
        System.out.println(BinaryUtils.binaryPretty(upPower(i)));
    }

    /**
     * 取 >= i的最小2次幂数
     *
     * @param n
     * @return
     */
    private int upPower(int n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n <= 0 ? 16 : n + 1;
    }

    @Test
    public void test() {

        int i = 5;
        System.out.println(BinaryUtils.binaryPretty(i));
        System.out.println(isPowerTwo01(i));
        System.out.println(isPowerTwo02(i));

        i = 4;
        System.out.println(BinaryUtils.binaryPretty(i));
        System.out.println(isPowerTwo01(i));
        System.out.println(isPowerTwo02(i));

        i = 0;
        System.out.println(BinaryUtils.binaryPretty(i));
        System.out.println(isPowerTwo01(i));
        System.out.println(isPowerTwo02(i));
    }

    /**
     * 判断一个数是否是2次方数
     */
    private boolean isPowerTwo01(int n) {
        return (n & (n - 1)) == 0;
    }

    /**
     * 判断一个数是否是2次方数
     */
    private boolean isPowerTwo02(int n) {
        return (n & -n) == n;
    }
}
