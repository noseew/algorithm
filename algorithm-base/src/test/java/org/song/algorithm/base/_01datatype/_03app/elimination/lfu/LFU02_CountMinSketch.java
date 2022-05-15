package org.song.algorithm.base._01datatype._03app.elimination.lfu;

import org.checkerframework.checker.index.qual.NonNegative;
import org.junit.jupiter.api.Test;

public class LFU02_CountMinSketch {
    /*
    
    Count-min Sketch 是一个概率数据结构, 用作数据流中事件的频率表. 
    它使用散列函数将事件映射到频率, 但与散列表不同, 散列表仅使用子线性空间, 但会因过多计算冲突导致的某些事件. 

    Count-min Sketch 本质上与 Fan 等人在 1998 年引入的计数 Bloom filter 相同的数据结构. 
    但是, 它们的使用方式各不相同, 因此尺寸也有所不同：
    计数最小草图通常具有次线性单元数, 与草图的所需近似质量有关, 而计数 Bloom filter 的大小通常与其中的元素数集合. 
     */


    @Test
    public void test01() {
        FrequencySketch<String> frequencySketch = new FrequencySketch<>();
        frequencySketch.ensureCapacity(100);
        // 添加元素
        frequencySketch.increment("key");

        // 返回次数
        frequencySketch.frequency("key");

    }


    /**
     * 摘自caffeine
     *
     * 估计某一元素在时间窗口内受欢迎程度的概率多集. 
     * 一个元素的最大频率被限制在15(4位), 周期性的老化过程会使所有元素的普及程度减半. 
     *
     * @author ben.manes@gmail.com (Ben Manes)
     */
    static class FrequencySketch<E> {

        /*
         * 这个类维护一个4位的CountMinSketch[1], 并定期老化, 以提供TinyLfu准入策略[2]的流行历史.
         * 该Sketch的时间和空间效率允许它以较低的成本估计缓存访问事件流中某个条目的频率.
         *
         * 计数器矩阵表示为单个维度数组, 每个槽容纳16个计数器. 固定深度为4, 可以平衡精度和成本, 因此宽度是阵列长度的4倍.
         * 为了保持准确的估计, 数组的长度等于缓存中的最大条目数, 增加到最接近的2次方, 以利用更有效的位掩蔽.
         * 这种配置的置信值为93.75%, 误差范围为e / width.
         *
         * 根据缓存中的最大条目数, 通过采样窗口周期性地老化所有条目的频率.
         * 这被TinyLfu称为重置操作, 它通过将所有计数器除以2并根据找到的奇数计数器的数量减去计数器来保持草图的新鲜度.
         * 老化的O(n)代价是平摊的, 非常适合硬件预取, 并且在每个数组位置使用廉价的位操作.
         *
         *
         * [1] 一种改进的数据流摘要:Count-Min Sketch及其应用
         * http://dimacs.rutgers.edu/~graham/pubs/papers/cm-full.pdf
         * [2] TinyLFU: 一种高效的缓存接纳策略
         * https://dl.acm.org/citation.cfm?id=3149371
         */

        static final long[] SEED = { // 来自FNV-1a, CityHash和Murmur3的混合种子
                0xc3a5c85c97cb3127L, 0xb492b66fbe98f273L, 0x9ae16a3b2f90404fL, 0xcbf29ce484222325L};
        static final long RESET_MASK = 0x7777777777777777L;
        // 10001 ... 0001_0001
        static final long ONE_MASK = 0x1111111111111111L;

        int sampleSize;
        int tableMask;
        long[] table;
        int size;

        /**
         * 创建一个延迟初始化的频率Sketch, 当缓存的最大大小确定后, 需要调用{@link #ensureCapacity}. 
         */
        @SuppressWarnings("NullAway.Init")
        public FrequencySketch() {
        }

        /**
         * 初始化并增加这个<tt>FrequencySketch</tt>实例的容量(如果需要的话), 
         * 以确保它可以在给定缓存的最大大小时准确地估计元素的受欢迎程度. 
         * 此操作在调整大小时忘记以前的所有计数. 
         *
         * @param maximumSize the maximum size of the cache
         */
        public void ensureCapacity(@NonNegative long maximumSize) {
            int maximum = (int) Math.min(maximumSize, Integer.MAX_VALUE >>> 1);
            if ((table != null) && (table.length >= maximum)) {
                return;
            }

            table = new long[(maximum == 0) ? 1 : ceilingPowerOfTwo(maximum)];
            tableMask = Math.max(0, table.length - 1);
            sampleSize = (maximumSize == 0) ? 10 : (10 * maximum);
            if (sampleSize <= 0) {
                sampleSize = Integer.MAX_VALUE;
            }
            size = 0;
        }

        /**
         * 如果草图还没有初始化, 则返回, 要求在开始跟踪频率之前调用{@link #ensureCapacity}. 
         */
        public boolean isNotInitialized() {
            return (table == null);
        }

        /**
         * 返回元素的估计出现次数, 最多为(15). 
         *
         * @param e 要计数出现次数的元素
         * @return 元素出现的估计次数;可能是零, 但绝不是负的
         */
        @NonNegative
        public int frequency(E e) {
            if (isNotInitialized()) {
                return 0;
            }

            int hash = spread(e.hashCode());
            int start = (hash & 3) << 2;
            int frequency = Integer.MAX_VALUE;
            for (int i = 0; i < 4; i++) {
                int index = indexOf(hash, i);
                int count = (int) ((table[index] >>> ((start + i) << 2)) & 0xfL);
                frequency = Math.min(frequency, count);
            }
            return frequency;
        }

        /**
         * 类似 CountingBloomFilter
         * <p>
         * 如果元素的流行度不超过最大值(15), 则增加元素的流行度. 当观察到的事件超过阈值时, 将对所有元素的流行度进行周期性下采样. 
         * 这个过程提供了一个频率老化, 允许过期的长期条目消失. 
         *
         * @param e 要添加的元素
         */
        public void increment(E e) {
            if (isNotInitialized()) {
                return;
            }

            int hash = spread(e.hashCode());
            int start = (hash & 3) << 2;

            // 4次hash计算
            // Loop unrolling improves throughput by 5m ops/s
            int index0 = indexOf(hash, 0);
            int index1 = indexOf(hash, 1);
            int index2 = indexOf(hash, 2);
            int index3 = indexOf(hash, 3);

            // 分别计次
            boolean added = incrementAt(index0, start);
            added |= incrementAt(index1, start + 1);
            added |= incrementAt(index2, start + 2);
            added |= incrementAt(index3, start + 3);

            if (added && (++size == sampleSize)) {
                reset();
            }
        }

        /**
         * 如果指定的计数器还没有达到最大值(15), 则增加1. 
         *
         * @param i the table index (16 counters)
         * @param j the counter to increment
         * @return if incremented
         */
        boolean incrementAt(int i, int j) {
            int offset = j << 2;
            // 统计的 key 最大使用频率为 15
            long mask = (0xfL << offset);
            if ((table[i] & mask) != mask) {
                table[i] += (1L << offset);
                return true;
            }
            return false;
        }

        /**
         * 使每个计数器的初始值减半. 
         * 减半使用的方式是为计数器批量进行与运算, 所以效率是计数器所在数组的数量, 也就是计数器/
         * 
         * 对突发稀疏流量表现并不好, 不如W-LFU, 如果访稀疏流量访问次数没有到减半阈值
         */
        void reset() {
            /*
            方法目标, 重置桶计数器, 和重新计算元素数量size, 这里的减半是约数, 并不是精确值
            
            重置计数器方法, 将计数器数值减半, 右移1位
                每个桶次数 c = 1010
                count 计算总次数方法为  count = c & 0000_0001
                循环 c >>> 1, 然后 c & 0000_0001
            
            总数size计算, 根据上边count值
                count数量=每个桶中1的数量, 最多有4个1, size最多16
                也就是size数量大约和count数量成 4:1
                
            总数 = (总数 - (count / 4)) / 2
             */
            int count = 0;
            for (int i = 0; i < table.length; i++) {
                count += Long.bitCount(table[i] & ONE_MASK);
                table[i] = (table[i] >>> 1) & RESET_MASK;
            }
            size = (size - (count >>> 2)) >>> 1;
        }

        /**
         * 返回计数器在指定深度的表索引. 
         *
         * @param item the element's hash
         * @param i    the counter depth
         * @return the table index
         */
        int indexOf(int item, int i) {
            long hash = (item + SEED[i]) * SEED[i];
            hash += (hash >>> 32);
            return ((int) hash) & tableMask;
        }

        /**
         * 对给定的hashCode应用一个补充的哈希函数, 以防止低质量的哈希函数. 
         */
        int spread(int x) {
            x = ((x >>> 16) ^ x) * 0x45d9f3b;
            x = ((x >>> 16) ^ x) * 0x45d9f3b;
            return (x >>> 16) ^ x;
        }

        static int ceilingPowerOfTwo(int x) {
            // 摘自《骇客的喜悦》, 第三章, 小哈里·s·沃伦
            // From Hacker's Delight, Chapter 3, Harry S. Warren Jr.
            return 1 << -Integer.numberOfLeadingZeros(x - 1);
        }
    }
    
}
