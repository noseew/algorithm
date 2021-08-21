package org.song.algorithm.algorithmbase.datatype.bit.bitmap;

import org.junit.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

public class BitMap_base_01 {


    @Test
    public void test_01_start() {
        BitMap bitMap = new BitMap(32);

        bitMap.set(2);
        bitMap.set(4);

        System.out.println(bitMap.get(4));
        System.out.println(bitMap.get(5));

        System.out.println(bitMap.bitCount());
        System.out.println(bitMap.bitCount(0, 3));

        bitMap.set(35);
        System.out.println(bitMap.bitCount(0, 36));

    }

    @Test
    public void test_01_bitCount() {
        BitMap bitMap = new BitMap(32);

        bitMap.set(0);
        bitMap.set(3);
        bitMap.set(31);

        bitMap.set(32);
        bitMap.set(35);
        bitMap.set(63);

        bitMap.set(64);
        bitMap.set(65);

        System.out.println(bitMap.bitCount()); // 8
        System.out.println(bitMap.bitCount(0, 3)); // 1
        System.out.println(bitMap.bitCount(0, 36)); // 5
        
        System.out.println(bitMap.bitCount(3, 31)); // 1
        System.out.println(bitMap.bitCount(3, 63)); // 4
        System.out.println(bitMap.bitCount(3, 95)); // 7
        
        System.out.println(bitMap.bitCount(3, 65)); // 6

    }

    /*
    bitmap
    1. 数据结构: 一维数组长度n, 子数组长度32(也可以用long类型, 64位)
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
            bitMap[x/32] = (1 << (x%32)) | (bitMap[x/32])
        3. 将指定bit位x设置成0
            (~(1 << (x%32))) & (bitMap[x/32])
        4.
    3. bitmap其他计算
        1. 计算bitmap中1的数量, 时间复杂度O(1)
            1. 思路, 单独计算每个int元素, 单个元素采用汉明重量计算
        2. 计算bitmap区间start和end中1的数量,
            1. 思路, 头尾非完整数据单独计算, 中间完整元素采用汉明重量计算
     */
    public static class BitMap {

        private final int bit = 32;

        private int[] bitMap;

        private BitMap(int offset) {
            this.bitMap = new int[newSize(offset)];
        }

        private BitMap() {
            this(1);
        }

        /**
         * 偏移量从0开始
         *
         * @param offset
         */
        public void set(int offset) {
            ensureCapacity(offset);
            bitMap[offset / bit] = (1 << (offset % bit)) | (bitMap[offset / bit]);
        }

        public int get(int offset) {
            return ((1 << (offset % bit)) & (bitMap[offset / bit])) > 0 ? 1 : 0;
        }

        public int bitCount() {
            return bitCount(this.bitMap, 0, this.bitMap.length, false);
        }

        /**
         * 偏移量, 从0开始
         *
         * @param startOffset 开始偏移量, 从0开始
         * @param endOffset
         * @return
         */
        public int bitCount(int startOffset, int endOffset) {
            if (startOffset >= this.bitMap.length * bit || endOffset <= 0 || endOffset <= startOffset) {
                return 0;
            }
            int moreStartOffset = startOffset % bit, // 头offset
                    moreEndOffset = endOffset % bit, // 尾offset
                    startIndex = startOffset / bit, // 头所在的下标
                    endIndex = endOffset / bit; // 尾所在的下标

            int bitCount = 0;
            if (endIndex > startIndex) {
                // 不在同一个下标元素中
                if (endOffset - startOffset > bit) {
                    if (moreStartOffset == 0
                            || moreEndOffset == 0
                            || (endIndex - startIndex) > 1){
                        // 有完整的元素
                        bitCount += bitCount(bitMap, startIndex, endIndex, moreStartOffset != 0);
                    }
                }
                // 取头
                if (moreStartOffset > 0) {
                    bitCount += bitCount(bitMap[startIndex], moreStartOffset, bit);
                }
                // 取尾
                if (moreEndOffset > 0) {
                    bitCount += bitCount(bitMap[startIndex], 0, moreEndOffset);
                }
            } else {
                // 在同一个下标元素中, 头尾交集
                bitCount += bitCount(bitMap[startIndex], moreStartOffset, moreEndOffset);
            }
            return bitCount;
        }

        /**
         * 左开右闭
         *
         * @param subBitMap
         * @return
         */
        private int bitCount(int[] subBitMap, int startIndex, int endIndex, boolean skipFirst) {
            int count = 0;
            int i = startIndex + (skipFirst ? 1 : 0);
            for (; i < endIndex; i++) {
                count += hammingWeight(subBitMap[i]);
            }
            return count;
        }

        private int bitCount(int n) {
            int c = 0;
            while (n > 0) {
                if ((n & 1) == 1) {
                    c++;
                }
                n = n >> 1;
            }
            return c;
        }

        private int bitCount(int n, int start, int end) {
            int c = 0;
            n = n >> start;
            for (int i = start; i < end; i++) {
                if ((n & 1) == 1) {
                    c += 1;
                }
                n = n >> 1;
            }
            return c;
        }

        /**
         * 当前数字最高位为准, 剩余低位全都变为1
         *
         * @param n
         * @return
         */
        private static int fullBit(int n) {
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            return n;
        }

        /**
         * 汉明重量计算bit数量
         *
         * @param n
         * @return
         */
        private int hammingWeight(int n) {
            if (n == 0 || n == 1) {
                return n;
            }
            if (n == -1) {
                return bit;
            }

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

        private int newSize(int offset) {
            return offset / bit + 1;
        }

        /**
         * 确保容量
         */
        private void ensureCapacity(int offset) {
            if (offset > bitMap.length * bit - 1) {
                dilatation(newSize(offset));
            }
        }

        /**
         * 扩容
         */
        private void dilatation(int newSize) {
            int[] newBitMap = new int[newSize];
            System.arraycopy(bitMap, 0, newBitMap, 0, bitMap.length);
            this.bitMap = newBitMap;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bitMap.length; i++) {
                sb.append(i).append(": ").append(BinaryUtils.binaryPretty(bitMap[i])).append("\r\n");
            }
            return sb.toString();
        }
    }


}
