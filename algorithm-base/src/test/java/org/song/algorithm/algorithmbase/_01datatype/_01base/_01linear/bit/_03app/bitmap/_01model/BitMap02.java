package org.song.algorithm.algorithmbase._01datatype._01base._01linear.bit._03app.bitmap._01model;

import org.song.algorithm.algorithmbase.utils.BinaryUtils;


/*
64位存储范围
    long最大值900亿亿, 数组最大长度21亿, 
    1. 能够存储最大的数是, 1300亿
    2. 数组长度21亿, 约16G内存存储
    3. 如果直接采用long存储, 约1T内存存储
 */

/**
 * BitMapBase02 位图
 * 偏移量从0开始
 * 每个数组元素就是一个64位的元素, 采用小端序,
 * 每个数组元素就是一个long元素
 */
public class BitMap02 {

    private final int bit = 64;

    private long[] BitMapBase02;

    public BitMap02() {
        this(1);
    }

    public BitMap02(int offset) {
        this.BitMapBase02 = new long[newSize(offset)];
    }

    private BitMap02(long[] BitMapBase02) {
        this.BitMapBase02 = BitMapBase02;
    }

    /**
     * 设置指定偏移量位置为1
     * 会自动进行数组扩容
     * <p>
     * 时间复杂度 O(1)
     *
     * @param offset 偏移量从0开始
     */
    public void setBit(int offset) {
        ensureCapacity(offset);
            /*
            定位元素: offset / bit
            定位偏移量: 1L << (offset % bit)
             */
        BitMapBase02[offset / bit] = (0x80000000_00000000L >>> (offset % bit)) | (BitMapBase02[offset / bit]);
    }

    /**
     * 获取指定偏移量位置的元素
     * <p>
     * 时间复杂度 O(1)
     *
     * @param offset 偏移量从0开始
     * @return 返回0或者1
     */
    public int getBit(int offset) {
            /*
            定位元素: offset / bit
            定位偏移量: 1L << (offset % bit)
             */
        return ((0x80000000_00000000L >>> (offset % bit)) & (BitMapBase02[offset / bit])) != 0 ? 1 : 0;
    }

    /**
     * 右移位
     * 宏观上, 采用数组序的右移位, bitmap中的数据会被替换
     * 由于底层int采用是小端序存储, 所以采用的是int的右移位操作
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param b
     */
    public void rightShift(int b) {
        rightShift(this.BitMapBase02, b, this.bit);
    }

    /**
     * 右移位
     * 宏观上, 采用数组序的右移位,
     * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
     * 由于底层int采用是小端序存储, 所以采用的是int的左移位操作
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param b
     * @return
     */
    public BitMap02 rightShiftNew(int b) {
        long[] bm = bitMapArrayImage();
        rightShift(bm, b, this.bit);
        return new BitMap02(bm);
    }

    /**
     * 右移位
     * 宏观上, 采用数组序的右移位, bitmap中的数据会被替换
     * 由于底层int采用是小端序存储, 所以采用的是int的右移位操作
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param b
     */
    private static void rightShift(long[] BitMapBase02, int b, int bit) {
        // 移位上个元素多出的数, 需要拼接到下一个元素上
        long lastMore = 0;
        // 移位当前元素多出的数, 用于暂存, 然后交给 lastMore
        long currentMore = 0;
        // 右移位, 正序操作, 只需要使用两个临时变量用来保存多出的数即可
        for (int i = 0; i < BitMapBase02.length; i++) {
            long e = BitMapBase02[i];
            // 获取当前元素后b个数, 因为他们将会被移位掉
            currentMore = e & -1L >>> (bit - b);
            // 当前元素右移b位(小端序), 并拼接上次移位多出的数
            // 多出的数原来在头部, 需要移动到尾部, 然后才能进行拼接
            BitMapBase02[i] = (e >>> b) | (lastMore << (bit - b));
            // 当前多出的数传下去
            lastMore = currentMore;
        }
    }

    /**
     * 左移位
     * 宏观上, 采用数组序的左移位, bitmap中的数据会被替换
     * 由于底层int采用是小端序存储, 所以采用的是int的左移位操作
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param b
     */
    public void leftShift(int b) {
        leftShift(this.BitMapBase02, b, this.bit);
    }

    /**
     * 左移位
     * 宏观上, 采用数组序的左移位,
     * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
     * 由于底层int采用是小端序存储, 所以采用的是int的右移位操作
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param b
     * @return
     */
    public BitMap02 leftShiftNew(int b) {
        long[] bm = bitMapArrayImage();
        leftShift(bm, b, this.bit);
        return new BitMap02(bm);
    }

    /**
     * 左移位
     * 宏观上, 采用数组序的左移位, bitmap中的数据会被替换
     * 由于底层int采用是小端序存储, 所以采用的是int的左移位操作
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param b
     */
    private static void leftShift(long[] BitMapBase02, int b, int bit) {
        // 移位上个元素多出的数, 需要拼接到下一个元素上
        long lastMore = 0;
        // 移位当前元素多出的数, 用于暂存, 然后交给 lastMore
        long currentMore = 0;
        // 左移位, 倒序操作, 只需要使用两个临时变量用来保存多出的数即可
        for (int i = BitMapBase02.length - 1; i >= 0; i--) {
            long e = BitMapBase02[i];
            // 获取当前元素前b个数, 因为他们将会被移位掉
            currentMore = e & -1L << (bit - b);
            // 当前元素左移b位(小端序), 并拼接上次移位多出的数
            // 多出的数原来在头部, 需要移动到尾部, 然后才能进行拼接
            BitMapBase02[i] = (e << b) | (lastMore >>> (bit - b));
            // 当前多出的数传下去
            lastMore = currentMore;
        }
    }

    /**
     * 与运算
     * 将当前bitmap中的数据和传入的参数进行位运算, bitmap中的数据会被替换
     * 循环bitmap数组, 逐个进行位运算, 传入元素的长度必须和bitmap中长度一致
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param BitMapBase02
     */
    public void and(long[] BitMapBase02) {
        if (this.BitMapBase02.length != BitMapBase02.length) {
            throw new IllegalArgumentException("BitMapBase02.length not eq this.BitMapBase02.length");
        }
        for (int i = 0; i < this.BitMapBase02.length; i++) {
            this.BitMapBase02[i] = this.BitMapBase02[i] & BitMapBase02[i];
        }
    }

    /**
     * 与运算
     * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
     * 循环bitmap数组, 逐个进行位运算, 传入元素的长度必须和bitmap中长度一致
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param BitMapBase02
     * @return
     */
    public BitMap02 andNew(int[] BitMapBase02) {
        if (this.BitMapBase02.length != BitMapBase02.length) {
            throw new IllegalArgumentException("BitMapBase02.length not eq this.BitMapBase02.length");
        }
        long[] bm = bitMapArrayImage();
        for (int i = 0; i < bm.length; i++) {
            bm[i] = bm[i] & BitMapBase02[i];
        }
        return new BitMap02(bm);
    }

    /**
     * 或运算
     * 将当前bitmap中的数据和传入的参数进行位运算, bitmap中的数据会被替换
     * 循环bitmap数组, 逐个进行位运算
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param BitMapBase02
     */
    public void or(long[] BitMapBase02) {
        int minLen = Math.min(this.BitMapBase02.length, BitMapBase02.length);
        for (int i = 0; i < minLen; i++) {
            this.BitMapBase02[i] = this.BitMapBase02[i] | BitMapBase02[i];
        }
    }

    /**
     * 或运算
     * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
     * 循环bitmap数组, 逐个进行位运算
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @param BitMapBase02
     * @return
     */
    public BitMap02 orNew(int[] BitMapBase02) {
        long[] bm = bitMapArrayImage();
        int minLen = Math.min(bm.length, BitMapBase02.length);
        for (int i = 0; i < minLen; i++) {
            bm[i] = bm[i] | BitMapBase02[i];
        }
        return new BitMap02(bm);
    }

    /**
     * 取反运算
     * 将当前bitmap中的数据和传入的参数进行位运算, bitmap中的数据会被替换
     * 循环bitmap数组, 逐个进行位运算
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     */
    public void not() {
        for (int i = 0; i < this.BitMapBase02.length; i++) {
            this.BitMapBase02[i] = ~this.BitMapBase02[i];
        }
    }

    /**
     * 取反运算
     * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
     * 循环bitmap数组, 逐个进行位运算
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @return
     */
    public BitMap02 notNew() {
        long[] bm = bitMapArrayImage();
        for (int i = 0; i < bm.length; i++) {
            bm[i] = ~bm[i];
        }
        return new BitMap02(bm);
    }

    /**
     * 计算bitmap中所有为1的元素
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     *
     * @return
     */
    public long bitCount() {
        return bitCount(this.BitMapBase02, 0, this.BitMapBase02.length, false);
    }

    /**
     * 计算bitmap中所有为1的元素
     * 偏移量, 从0开始
     * <p>
     * 时间复杂度 O(n), n指的是bitmap数组数量
     * <p>
     * 通过起止偏移量, 将bitmap数组分成3个
     * head: 非完整的int元素, 单独计算其后面的元素
     * 完整中间元素: 采用汉明重量计算每一个元素bitCount
     * tail: 非完整的int元素, 单独计算前后面的元素
     * <p>
     * 特殊情况, 起止偏移量在同一个元素内, 则需要取头尾的交集
     *
     * @param startOffset 开始偏移量
     * @param endOffset   截止偏移量
     * @return
     */
    public int bitCount(int startOffset, int endOffset) {
        if (startOffset >= this.BitMapBase02.length * bit || endOffset <= 0 || endOffset <= startOffset) {
            return 0;
        }
        int moreStartOffset = startOffset % bit, // 头offset
                moreEndOffset = endOffset % bit, // 尾offset
                startIndex = startOffset / bit, // 头所在的下标
                endIndex = endOffset / bit; // 尾所在的下标

        int bitCount = 0;
        if (endIndex > startIndex) {
            // 他们不在同一个下标元素中
            if (endOffset - startOffset > bit) {
                // 他们间隔大于64, 有可能有完整的一个元素
                if (moreStartOffset == 0
                        || moreEndOffset == 0
                        || (endIndex - startIndex) > 1) {
                    // 有完整的元素
                    bitCount += bitCount(BitMapBase02, startIndex, endIndex, moreStartOffset != 0);
                }
            }
            // 取头, 在bitmap数组中是头部, 单独看这个元素, 取的是其尾部的元素
            if (moreStartOffset > 0) {
                // 小端序, 所以是int类型中的高位
                bitCount += bitCount(BitMapBase02[startIndex], moreStartOffset, bit);
            }
            // 取尾, 在bitmap数组中是尾部, 单独看这个元素, 取的是其头部的元素
            if (moreEndOffset > 0) {
                // 小端序, 所以是int类型中的低位
                bitCount += bitCount(BitMapBase02[endIndex], 0, moreEndOffset);
            }
        } else {
            // 特殊情况, 起止偏移量在同一个元素内, 则需要取头尾的交集
            bitCount += bitCount(BitMapBase02[startIndex], moreStartOffset, moreEndOffset);
        }
        return bitCount;
    }

    /**
     * 获取bitmap的数组, 注意, 返回的数镜像, 并不会对原数组有影响
     *
     * @return
     */
    public long[] bitMapArrayImage() {
        long[] newBitMap = new long[BitMapBase02.length];
        System.arraycopy(BitMapBase02, 0, newBitMap, 0, BitMapBase02.length);
        return newBitMap;
    }

    /**
     * 清空bitmap数组
     */
    public void clean() {
        this.BitMapBase02 = new long[1];
    }

    /**
     * 左开右闭
     *
     * @param subBitMap
     * @return
     */
    private long bitCount(long[] subBitMap, int startIndex, int endIndex, boolean skipFirst) {
        long count = 0;
        int i = startIndex + (skipFirst ? 1 : 0);
        for (; i < endIndex; i++) {
            count += hammingWeight(subBitMap[i]);
        }
        return count;
    }

    /**
     * 计算int类型中1的数量,
     * 注意: 并不是计算int中所有的1, 而是以小端序限制起止下标, 左开右闭
     * <p>
     * 时间复杂度 O(n), n指的是int位数
     *
     * @param n
     * @param start int高位, 包含
     * @param end   int低位, 不包含
     * @return
     */
    private int bitCount(long n, int start, int end) {
        int c = 0;
        n = n << start;
        for (int i = start; i < end; i++) {
            if ((n & 0x80000000_00000000L) != 0) {
                c += 1;
            }
            n = n << 1;
        }
        return c;
    }

    /**
     * 汉明重量计算bit数量
     *
     * @param n
     * @return
     */
    private long hammingWeight(long n) {
        if (n == 0 || n == 1) {
            return n;
        }
        if (n == -1) {
            return bit;
        }

        long c = 0;
        c = (n & 0B01010101_01010101_01010101_01010101_01010101_01010101_01010101_01010101L)
                + ((n >>> 1) & 0B01010101_01010101_01010101_01010101_01010101_01010101_01010101_01010101L);
        c = (c & 0B00110011_00110011_00110011_00110011_00110011_00110011_00110011_00110011L)
                + ((c >>> 2) & 0B00110011_00110011_00110011_00110011_00110011_00110011_00110011_00110011L);
        c = (c & 0B00001111_00001111_00001111_00001111_00001111_00001111_00001111_00001111L)
                + ((c >>> 4) & 0B00001111_00001111_00001111_00001111_00001111_00001111_00001111_00001111L);
        c = (c & 0B00000000_11111111_00000000_11111111_00000000_11111111_00000000_11111111L)
                + ((c >>> 8) & 0B00000000_11111111_00000000_11111111_00000000_11111111_00000000_11111111L);
        c = (c & 0B00000000_00000000_11111111_11111111_00000000_00000000_11111111_11111111L)
                + ((c >>> 16) & 0B00000000_00000000_11111111_11111111_00000000_00000000_11111111_11111111L);
        c = (c & 0B00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L)
                + ((c >>> 32) & 0B00000000_00000000_00000000_00000000_11111111_11111111_11111111_11111111L);
        return c;
    }

    private long fullBit(long n) {
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n |= n >>> 32;
        return n;
    }

    private int newSize(int offset) {
        return offset / bit + 1;
    }

    /**
     * 确保容量
     */
    private void ensureCapacity(int offset) {
        if (offset > BitMapBase02.length * bit - 1) {
            dilatation(newSize(offset));
        }
    }

    /**
     * 扩容
     */
    private void dilatation(int newSize) {
        long[] newBitMap = new long[newSize];
        System.arraycopy(BitMapBase02, 0, newBitMap, 0, BitMapBase02.length);
        this.BitMapBase02 = newBitMap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BitMapBase02.length; i++) {
            sb.append(i).append(": ").append(BinaryUtils.binaryPretty(BitMapBase02[i])).append("\r\n");
        }
        return sb.toString();
    }
}
