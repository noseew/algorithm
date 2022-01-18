package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.bit._03app.bitmap;

import org.junit.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基于 int 32位, 大端序存储(也是int默认存储方式) 实现的bitmap
 */
public class BitMap_base_01 {

    @Test
    public void test_01_start() {
        BitMap bitMap = new BitMap(1);

        bitMap.setBit(0);
        System.out.println(bitMap.getBit(0));
        bitMap.setBit(1);
        System.out.println(bitMap.getBit(1));
        bitMap.setBit(31);
        System.out.println(bitMap.getBit(31));
        bitMap.setBit(32);
        System.out.println(bitMap.getBit(32));

        System.out.println(bitMap.getBit(4)); // 1
        System.out.println(bitMap.getBit(5)); // 0
    }

    @Test
    public void test_01_bitCount() {
        BitMap bitMap = new BitMap(32);

        bitMap.setBit(0);
        bitMap.setBit(3);
        bitMap.setBit(31);

        bitMap.setBit(32);
        bitMap.setBit(35);
        bitMap.setBit(63);

        bitMap.setBit(64);
        bitMap.setBit(65);

        System.out.println(bitMap.bitCount()); // 8
        System.out.println(bitMap.bitCount(0, 3)); // 1
        System.out.println(bitMap.bitCount(0, 36)); // 5

        System.out.println(bitMap.bitCount(3, 31)); // 1
        System.out.println(bitMap.bitCount(3, 63)); // 4
        System.out.println(bitMap.bitCount(3, 95)); // 7

        System.out.println(bitMap.bitCount(3, 65)); // 6

    }

    @Test
    public void test_01_leftRight() {
        BitMap bitMap = new BitMap(1);

        bitMap.setBit(1);
        bitMap.setBit(2);
        bitMap.setBit(31);

        bitMap.setBit(32);
        bitMap.setBit(35);
        bitMap.setBit(63);

        bitMap.rightShift(2);
        bitMap.leftShift(2);
    }

    @Test
    public void test_01_bit() {
        BitMap bitMap = new BitMap(1);

        bitMap.setBit(1);
        bitMap.setBit(2);
        bitMap.setBit(31);

        bitMap.setBit(32);
        bitMap.setBit(35);
        bitMap.setBit(63);

//        bitMap.and(new int[]{14});
//        bitMap.or(new int[]{3});
//        bitMap.not();

        BitMap bitMap1 = bitMap.notNew();
        System.out.println();
    }

    @Test
    public void test_01_consecutive() {
        BitMap bitMap = new BitMap(1);

        bitMap.setBit(1);
        bitMap.setBit(2);
        bitMap.setBit(3);
        bitMap.setBit(31);

        for (int i = 0; i < 32; i++) {
            bitMap.setBit(32 + i);
        }
        bitMap.setBit(64);
        bitMap.setBit(65);
        
        bitMap.setBit(67);

        System.out.println(bitMap.consecutive());
    }

    @Test
    public void test_check() {
//        MD5:key=3, code=46372
//        SHA:key=3, code=1055
        BitMap bitMap = new BitMap();
        bitMap.setBit(46372);
        System.out.println(bitMap.getBit(46372));
        bitMap.setBit(1055);
        System.out.println(bitMap.getBit(1055));
    }

    @Test
    public void test_getValue() {
        BitMap bitMap = new BitMap();
        bitMap.setBit(0);
        System.out.println(Arrays.toString(bitMap.getValues()));
        bitMap.setBit(1);
        System.out.println(Arrays.toString(bitMap.getValues()));
        bitMap.setBit(31);
        System.out.println(Arrays.toString(bitMap.getValues()));
        bitMap.setBit(32);
        System.out.println(Arrays.toString(bitMap.getValues()));
        bitMap.setBit(63);
        System.out.println(Arrays.toString(bitMap.getValues()));
        bitMap.setBit(64);
        System.out.println(Arrays.toString(bitMap.getValues()));
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
        获取元素所在数组下标 bitMap[x/32]
        获取元素的偏移量 x%32
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
        2. 计算bitmap区间start和end中1的数量, 时间复杂度O(n), n表示数组数量
            1. 思路, 头尾非完整数据单独计算, 或者使用查找表计算, 中间完整元素采用汉明重量计算
        3. 位移运算, 时间复杂度O(n), n表示数组数量
            1. 思路, 将数组中的int依次进行位运算, 移位多出的元素拼接到下一个元素上
            2. 位移运算的方向和int方向不同, bitmap中的位移运算采用的是数组序, 
                如果bitmap中int存储采用大端序, 则bitmap右移位对应着int的左移位, 则bitmap左移位对应着int的右移位, 
                
     4. 32位存储范围
        int最大值21亿, 数组最大长度21亿
        1. 能够存储最大的数是, 21亿
        2. 数组长度约6千万, 约250MB内存存储
        3. 如果直接采用int存储, 约8G内存存储
        
     5. 优点:
            1. 少量内存空间能够标记海量数据
            2. 算法复杂度低, O(1)到O(n)
        缺点:
            1. 不能存储重复的数据
            
     6. 应用场景
        1. redis的位图
        2. 布隆过滤器
        3. 海量信息的去重或者判断是否存在
        4. 和HashSet算法思路类似, 但是比他占用空间小
     */

    /**
     * BitMap 位图
     * 偏移量从0开始, 一个元素存储的数据是0, 0到31
     * 每个数组元素就是一个32位的元素, 采用小端序,
     * 每个数组元素就是一个int元素
     */
    public static class BitMap {
        /**
         * 当前元素的位数, int 采用 32
         */
        private final int bit = 32;
        /**
         * 数据数组
         */
        private int[] bitMap;

        private static final int tableBit = 8;
        
        private static final int tableBitTag = 0b1111_1111;
        
        /**
         * 查找表, 采用8个字节, 最多256个数
         * 下标表示对应数字的值
         * 元素表示对应元素值的bitCount
         */
        private static int[] bitCountTable;
        
        static {
            bitCountTable = new int[tableBitTag + 1];
            for (int i = 0; i < bitCountTable.length; i++) {
                // 下标表示 int 值, value表示 bitCount 数
                bitCountTable[i] = hammingWeight(i);
            }
        }

        private BitMap() {
            this(1);
        }

        public BitMap(int offset) {
            this.bitMap = new int[newSize(offset)];
        }

        private BitMap(int[] bitMap) {
            this.bitMap = bitMap;
        }

        /**
         * 设置指定偏移量位置为1
         * 从0开始, 第一位是0, 最高位是31
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
            定位偏移量: 1 << (offset % bit)
             */
            bitMap[offset / bit] = (1 << (offset % bit)) | (bitMap[offset / bit]);
        }

        /**
         * 设置指定偏移量位置为0
         * 会自动进行数组扩容
         * <p>
         * 时间复杂度 O(1)
         *
         * @param offset 偏移量从0开始
         */
        public void removeBit(int offset) {
            ensureCapacity(offset);
            /*
            定位元素: offset / bit
            定位偏移量: 1 << (offset % bit)
            
            将指定位置设为0
             */
            bitMap[offset / bit] = ~(1 << (offset % bit)) & bitMap[offset / bit];
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
            定位偏移量: 1 << (offset % bit)
             */
            return ((1 << (offset % bit)) & (bitMap[offset / bit])) != 0 ? 1 : 0;
        }

        /**
         * 获取位图所有的数据, 并返回数组
         *
         * @return
         */
        public int[] getValues() {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < bitMap.length; i++) {
                int[] itemValues = getItemValues(i);
                for (int value : itemValues) {
                    list.add(value);
                }
            }
            return list.stream().mapToInt(Integer::valueOf).toArray();
        }

        /**
         * 获取指定位置的元素对应的int值数组
         * <p>
         * 时间复杂度 O(n)
         *
         * @param index bitmap中元素的下标
         * @return 该元素中bit位所表示的实际的值
         */
        private int[] getItemValues(int index) {
            // 基础值前面有, 由于数据从0开始, 所以-1
            int baseValue = bit * index;
            
            // 当前下标的元素
            int n = bitMap[index];
            // 有多少个1, 该数组就有多大
            int[] values = new int[bitCountByTable(n)];

            // values中的数下标
            int valuesIndex = 0;
            // offset表示当前bit位对应的数的值, + baseValue 之后, 就是表示在位图中的值
            for (int offset = 0; offset < bit && valuesIndex < values.length; offset++) {
                if ((n & 0B1) == 1) {
                    values[valuesIndex++] = baseValue + offset;
                }
                n = n >>> 1;
            }
            return values;
        }

        /**
         * 右移位
         * 宏观上, 采用数组序的右移位, bitmap中的数据会被替换
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param b
         */
        public void rightShift(int b) {
            rightShift(this.bitMap, b, this.bit);
        }

        /**
         * 右移位
         * 宏观上, 采用数组序的右移位,
         * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param b
         * @return
         */
        public BitMap rightShiftNew(int b) {
            int[] bm = bitMapArrayImage();
            rightShift(bm, b, this.bit);
            return new BitMap(bm);
        }

        /**
         * 右移位
         * 宏观上, 采用数组序的右移位, bitmap中的数据会被替换
         * 由于底层int采用是大端序存储, 所以采用的是int的左移位操作
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param bitMap 位图数组
         * @param b      移动距离
         * @param bit    总bit位, 32或者64
         */
        private static void rightShift(int[] bitMap, int b, int bit) {
            // 移位上个元素多出的数, 需要拼接到下一个元素上
            int lastMore = 0;
            // 移位当前元素多出的数, 用于暂存, 然后交给 lastMore
            int currentMore = 0;
            // 右移位, 正序操作, 只需要使用两个临时变量用来保存多出的数即可
            for (int i = 0; i < bitMap.length; i++) {
                int e = bitMap[i];
                // 获取当前元素后b个数, 因为他们将会被移位掉
                currentMore = e & -1 << (bit - b);
                // 当前元素右移b位(大端序, 所以操作是左移), 并拼接上次移位多出的数
                // 多出的数原来在头部, 需要移动到尾部, 然后才能进行拼接
                bitMap[i] = (e << b) | (lastMore >>> (bit - b));
                // 当前多出的数传下去
                lastMore = currentMore;
            }
        }

        /**
         * 左移位
         * 宏观上, 采用数组序的左移位, bitmap中的数据会被替换
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param b
         */
        public void leftShift(int b) {
            leftShift(this.bitMap, b, this.bit);
        }

        /**
         * 左移位
         * 宏观上, 采用数组序的左移位,
         * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param b
         * @return
         */
        public BitMap leftShiftNew(int b) {
            int[] bm = bitMapArrayImage();
            leftShift(bm, b, this.bit);
            return new BitMap(bm);
        }

        /**
         * 左移位
         * 宏观上, 采用数组序的左移位, bitmap中的数据会被替换
         * 由于底层int采用是大端序存储, 所以采用的是int的右移位操作
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param b
         */
        public static void leftShift(int[] bitMap, int b, int bit) {
            // 移位上个元素多出的数, 需要拼接到下一个元素上
            int lastMore = 0;
            // 移位当前元素多出的数, 用于暂存, 然后交给 lastMore
            int currentMore = 0;
            // 左移位, 倒序操作, 只需要使用两个临时变量用来保存多出的数即可
            for (int i = bitMap.length - 1; i >= 0; i--) {
                int e = bitMap[i];
                // 获取当前元素前b个数, 因为他们将会被移位掉
                currentMore = e & ((1 << b) - 1);
                // 当前元素左移b位(大端序, 所以操作是右移, bitmap中没有符号), 并拼接上次移位多出的数
                // 多出的数原来在头部, 需要移动到尾部, 然后才能进行拼接
                bitMap[i] = (e >>> b) | (lastMore << (bit - b));
                // 当前多出的数传下去
                lastMore = currentMore;
            }
        }

        /**
         * 与运算
         * 将当前bitmap中的数据和传入的参数进行位运算, bitmap中的数据会被替换
         * 循环bitmap数组, 逐个进行位运算, 传入元素的长度必须和bitmap中长度一致
         *
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param bitMap
         */
        public void and(int[] bitMap) {
            if (this.bitMap.length != bitMap.length) {
                throw new IllegalArgumentException("bitMap.length not eq this.bitMap.length");
            }
            for (int i = 0; i < this.bitMap.length; i++) {
                this.bitMap[i] = this.bitMap[i] & bitMap[i];
            }
        }

        /**
         * 与运算
         * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
         * 循环bitmap数组, 逐个进行位运算, 传入元素的长度必须和bitmap中长度一致
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param bitMap
         * @return
         */
        public BitMap andNew(int[] bitMap) {
            if (this.bitMap.length != bitMap.length) {
                throw new IllegalArgumentException("bitMap.length not eq this.bitMap.length");
            }
            int[] bm = bitMapArrayImage();
            for (int i = 0; i < bm.length; i++) {
                bm[i] = bm[i] & bitMap[i];
            }
            return new BitMap(bm);
        }

        /**
         * 或运算
         * 将当前bitmap中的数据和传入的参数进行位运算, bitmap中的数据会被替换
         * 循环bitmap数组, 逐个进行位运算
         *
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param bitMap
         */
        public void or(int[] bitMap) {
            int minLen = Math.min(this.bitMap.length, bitMap.length);
            for (int i = 0; i < minLen; i++) {
                this.bitMap[i] = this.bitMap[i] | bitMap[i];
            }
        }

        /**
         * 或运算
         * 将当前bitmap中的数据和传入的参数进行位运算, 并返回新的bitmap元素, 原bitmap中的数据不会被修改
         * 循环bitmap数组, 逐个进行位运算
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @param bitMap
         * @return
         */
        public BitMap orNew(int[] bitMap) {
            int[] bm = bitMapArrayImage();
            int minLen = Math.min(bm.length, bitMap.length);
            for (int i = 0; i < minLen; i++) {
                bm[i] = bm[i] | bitMap[i];
            }
            return new BitMap(bm);
        }

        /**
         * 取反运算
         * 将当前bitmap中的数据和传入的参数进行位运算, bitmap中的数据会被替换
         * 循环bitmap数组, 逐个进行位运算
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         */
        public void not() {
            for (int i = 0; i < this.bitMap.length; i++) {
                this.bitMap[i] = ~this.bitMap[i];
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
        public BitMap notNew() {
            int[] bm = bitMapArrayImage();
            for (int i = 0; i < bm.length; i++) {
                bm[i] = ~bm[i];
            }
            return new BitMap(bm);
        }

        /**
         * 计算bitmap中所有为1的元素
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * @return
         */
        public int bitCount() {
            return bitCount(this.bitMap, 0, this.bitMap.length, false);
        }

        /**
         * 计算bitmap中所有为1的元素
         * 偏移量, 从0开始
         * <p>
         * 时间复杂度 O(n), n指的是bitmap数组数量
         *
         * 通过起止偏移量, 将bitmap数组分成3个
         * head: 非完整的int元素, 单独计算其后面的元素
         * 完整中间元素: 采用汉明重量计算每一个元素bitCount
         * tail: 非完整的int元素, 单独计算前后面的元素
         *
         * 特殊情况, 起止偏移量在同一个元素内, 则需要取头尾的交集
         *
         * @param startOffset 开始偏移量
         * @param endOffset 截止偏移量
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
                // 他们不在同一个下标元素中
                if (endOffset - startOffset > bit) {
                    // 他们间隔大于32, 有可能有完整的一个元素
                    if (moreStartOffset == 0
                            || moreEndOffset == 0
                            || (endIndex - startIndex) > 1) {
                        // 有完整的元素
                        bitCount += bitCount(bitMap, startIndex, endIndex, moreStartOffset != 0);
                    }
                }
                // 取头, 在bitmap数组中是头部, 单独看这个元素, 取的是其尾部的元素
                if (moreStartOffset > 0) {
                    // 大端序, 所以是int类型中的高位
                    bitCount += bitCountByTable(bitMap[startIndex], moreStartOffset, bit);
                }
                // 取尾, 在bitmap数组中是尾部, 单独看这个元素, 取的是其头部的元素
                if (moreEndOffset > 0) {
                    // 大端序, 所以是int类型中的低位
                    bitCount += bitCountByTable(bitMap[endIndex], 0, moreEndOffset);
                }
            } else {
                // 特殊情况, 起止偏移量在同一个元素内, 则需要取头尾的交集
                bitCount += bitCountByTable(bitMap[startIndex], moreStartOffset, moreEndOffset);
            }
            return bitCount;
        }
        
        public int consecutive() {
            return maxConsecutive(1);
        }

        /**
         * TODO 未完成
         * 上一个遗留的数量并不一定是上一个最大的数量
         * 
         * @param val
         * @return
         */
        private int maxConsecutive(int val) {
            // 目前位置, 最大值
            int totalMax = 0;
            // 上次计算截止到目前的累加值
            int last = 0;
            // 当前元素最大值
            int current = 0;
            // 上一个是否连续接上下一个
            boolean lastConsecutive = false;
            for (int i = 0; i < this.bitMap.length; i++) {

                int[] ints = maxConsecutive(this.bitMap[i], val);
                // 当前最大连续个数
                current = ints[1] - ints[0];
                // 当前是否连续接上上一个
                boolean currentConsecutiveLast = ints[0] == 0;
                // 当前是否连续接上下一个
                boolean currentConsecutiveNext = ints[1] == 32;

                if (lastConsecutive && currentConsecutiveLast) {
                    // 当前连续接上上一个, 则 last 累加
                    last += current;
                }
                
                if (!currentConsecutiveLast) {
                    // 当前不接上一个, 则 last 归档
                    totalMax = Math.max(totalMax, last);
                    last = 0;
                }
                
                // 归档最大值
                totalMax = Math.max(totalMax, current);
                totalMax = Math.max(totalMax, last);
                
                lastConsecutive = currentConsecutiveNext;
            }
            return 0; 
        }

        /**
         * 最大连续数bit的起止下标, 左开右闭, 大端序
         * 0表示包含开头, 31表示不包含结尾
         * <p>
         * 这里采用大端序
         *
         * @param number
         * @param bit    取值 1, 0
         * @return
         */
        private int[] maxConsecutive(int number, int bit) {
            int end = 0;
            int max = 0;
            int current = 0;
            for (int i = 1; i <= Integer.SIZE; i++) {
                if ((number & 1) == bit) {
                    current++;
                } else {
                    current = 0;
                }
                if (current > max) {
                    max = current;
                    end = i;
                }
                number = (number >>> 1);
            }
            int[] indexes = {end - Math.max(current, max), end};
            return indexes;
        }

        /**
         * 获取bitmap的数组, 注意, 返回的数镜像, 并不会对原数组有影响
         *
         * @return
         */
        public int[] bitMapArrayImage() {
            int[] newBitMap = new int[bitMap.length];
            System.arraycopy(bitMap, 0, newBitMap, 0, bitMap.length);
            return newBitMap;
        }

        /**
         * 清空bitmap数组
         */
        public void clean() {
            this.bitMap = new int[1];
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

        /**
         * 计算int类型中1的数量,
         * 注意: 并不是计算int中所有的1, 而是以大端序限制起止下标, 左开右闭
         * <p>
         * 时间复杂度 O(n), n指的是int位数
         *
         * @param n
         * @param start int低位, 包含
         * @param end   int高位, 不包含
         * @return
         */
        private int bitCountByTable(int n, int start, int end) {
//            if (end - start < tableBit) {
//                return bitCountTraverse(n, start, end);
//            }
            // 只保留 start和  end 中间的 bit 位
            n = (-1 >>> (bit - end)) & n;
            n = (-1 << start) & n;
            n = n >>> start;
            int c = 0;
            for (int i = start; i <= end + tableBit; i += tableBit) {
                // 每8位一组, 通过 table 查找 bitCount 数
                c += bitCountTable[tableBitTag & n];
                n = n >>> tableBit;
            }
            return c;
        }

        /**
         * 计算int类型中1的数量,
         * 注意: 并不是计算int中所有的1, 而是以大端序限制起止下标, 左开右闭
         * <p>
         * 时间复杂度 O(n), n指的是int位数
         *
         * @param n
         * @return
         */
        private int bitCountByTable(int n) {
            int c = 0;
            for (int i = 0; i <= bit + tableBit; i += tableBit) {
                // 每8位一组, 通过 table 查找 bitCount 数
                c += bitCountTable[tableBitTag & n];
                n = n >>> tableBit;
            }
            return c;
        }

        /**
         * 计算int类型中1的数量,
         * 注意: 并不是计算int中所有的1, 而是以大端序限制起止下标, 左开右闭
         * <p>
         * 时间复杂度 O(n), n指的是int位数
         *
         * @param n
         * @param start int低位, 包含
         * @param end   int高位, 不包含
         * @return
         */
        private static int bitCountTraverse(int n, int start, int end) {
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
         * 汉明重量计算bit数量
         *
         * @param n
         * @return
         */
        private static int hammingWeight(int n) {
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

        private int fullBit(int n) {
            n |= n >>> 1;
            n |= n >>> 2;
            n |= n >>> 4;
            n |= n >>> 8;
            n |= n >>> 16;
            return n;
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
