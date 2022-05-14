package org.song.algorithm.base._01datatype._02high.bloom.fromos.bloom;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 实现位图计数, 由于已经有计数了, 所以这里可以没有使用位图, 计数=0就等价于位图=0
 * 
 * 实现一个 counting Bloom filter，如Fan等人在ToN 2000论文中定义的那样。
 *
 * <p>
 * 计数布隆过滤器是对标准布隆过滤器的改进，因为它允许动态添加和删除集合成员信息。这是通过使用计数向量而不是位向量来实现的。
 * <p>
 * 最初由
 * <a href="http://www.one-lab.org">European Commission One-Lab Project 034819</a>.
 *
 * @see Filter The general behavior of a filter
 * @see <a href="http://portal.acm.org/citation.cfm?id=343571.343572">Summary cache: a scalable wide-area web cache sharing protocol</a>
 */
public final class CountingBloomFilter extends Filter {
    /**
     * 存储计数桶
     * 一个long值当成多个计数单位
     * 每4bit(BUCKET_MAX_VALUE), 为一个桶, 进行计数, 一个long, 可以为15个bit进行计数
     */
    private long[] buckets;

    /**
     * 我们使用4位桶，所以每个桶可以数到15
     */
    private final static long BUCKET_MAX_VALUE = 15;

    /**
     * Default constructor - use with readFields
     */
    public CountingBloomFilter() {
    }

    /**
     * Constructor
     *
     * @param vectorSize The vector size of <i>this</i> filter.
     * @param nbHash     The number of hash function to consider.
     * @param hashType   type of the hashing function (see
     *                   {@link org.apache.hadoop.util.hash.Hash}).
     */
    public CountingBloomFilter(int vectorSize, int nbHash, int hashType) {
        super(vectorSize, nbHash, hashType);
        /*
        vectorSize 最大bit位数, 也就是int最大位
        这里 buckets 最大位数= vectorSize/16, 
         */
        buckets = new long[buckets2words(vectorSize)];
    }

    /**
     * 返回用于保存vectorSize桶的64位单词的个数
     * returns the number of 64 bit words it would take to hold vectorSize buckets
     */
    private static int buckets2words(int vectorSize) {
        return ((vectorSize - 1) >>> 4) + 1;
    }


    @Override
    public void add(Key key) {
        if (key == null) {
            throw new NullPointerException("key can not be null");
        }

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            // (1111 & hash值) * 4, 最大 60
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            // 1111 左移最大60位, 定位到具体桶位置
            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

            // 仅当桶中的计数小于BUCKET_MAX_VALUE时递增
            // only increment if the count in the bucket is less than BUCKET_MAX_VALUE
            if (bucketValue < BUCKET_MAX_VALUE) {
                // increment by 1
                buckets[wordNum] = (buckets[wordNum] & ~bucketMask) | ((bucketValue + 1) << bucketShift);
            }
        }
    }

    /**
     * Removes a specified key from <i>this</i> counting Bloom filter.
     * <p>
     * <b>Invariant</b>: nothing happens if the specified key does not belong to <i>this</i> counter Bloom filter.
     *
     * @param key The key to remove.
     */
    public void delete(Key key) {
        if (key == null) {
            throw new NullPointerException("Key may not be null");
        }
        if (!membershipTest(key)) {
            throw new IllegalArgumentException("Key is not a member");
        }

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

            // only decrement if the count in the bucket is between 0 and BUCKET_MAX_VALUE
            if (bucketValue >= 1 && bucketValue < BUCKET_MAX_VALUE) {
                // decrement by 1
                buckets[wordNum] = (buckets[wordNum] & ~bucketMask) | ((bucketValue - 1) << bucketShift);
            }
        }
    }

    @Override
    public void and(Filter filter) {
        if (filter == null
                || !(filter instanceof CountingBloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be and-ed");
        }
        CountingBloomFilter cbf = (CountingBloomFilter) filter;

        int sizeInWords = buckets2words(vectorSize);
        for (int i = 0; i < sizeInWords; i++) {
            this.buckets[i] &= cbf.buckets[i];
        }
    }

    @Override
    public boolean membershipTest(Key key) {
        if (key == null) {
            throw new NullPointerException("Key may not be null");
        }

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;

            if ((buckets[wordNum] & bucketMask) == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 这个方法计算密钥的近似计数，即密钥被添加到过滤器的次数。这允许过滤器用作近似的 键-数 代码映射。
     * <p>注意:由于该过滤器的桶大小，如果插入同一个键超过15次，将会导致与该键相关的所有过滤器位置溢出，
     * 并且会显著增加该键和其他键的错误率。由于这个原因，过滤器只能用于存储小计数值<code>0 &lt;= N &lt;15 > < /代码。
     * 
     * This method calculates an approximate count of the key, i.e. how many
     * times the key was added to the filter. This allows the filter to be
     * used as an approximate <code>key -&gt; count</code> map.
     * <p>NOTE: due to the bucket size of this filter, inserting the same
     * key more than 15 times will cause an overflow at all filter positions
     * associated with this key, and it will significantly increase the error
     * rate for this and other keys. For this reason the filter can only be
     * used to store small count values <code>0 &lt;= N &lt;&lt; 15</code>.
     *
     * @param key key to be tested
     *            
     * @return 0 if the key is not present. Otherwise, a positive value v will
     * be returned such that <code>v == count</code> with probability equal to the
     * error rate of this filter, and <code>v &gt; count</code> otherwise.
     * Additionally, if the filter experienced an underflow as a result of
     * {@link #delete(Key)} operation, the return value may be lower than the
     * <code>count</code> with the probability of the false negative rate of such
     * filter.
     * 如果键不存在，则为0。否则，将返回一个正的值v，使v == count，其概率等于该过滤器的错误率，并且<code>v &gt;数> < /代码。
     * 此外，如果过滤器由于{@link #delete(Key)}操作而发生下溢，则返回值可能小于<code>count</code>，且该过滤器有误报率的概率。
     */
    public int approximateCount(Key key) {
        int res = Integer.MAX_VALUE;
        int[] h = hash.hash(key);
        hash.clear();
        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;
            if (bucketValue < res) res = (int) bucketValue;
        }
        if (res != Integer.MAX_VALUE) {
            return res;
        } else {
            return 0;
        }
    }

    @Override
    public void not() {
        throw new UnsupportedOperationException("not() is undefined for "
                + this.getClass().getName());
    }

    @Override
    public void or(Filter filter) {
        if (filter == null
                || !(filter instanceof CountingBloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be or-ed");
        }

        CountingBloomFilter cbf = (CountingBloomFilter) filter;

        int sizeInWords = buckets2words(vectorSize);
        for (int i = 0; i < sizeInWords; i++) {
            this.buckets[i] |= cbf.buckets[i];
        }
    }

    @Override
    public void xor(Filter filter) {
        throw new UnsupportedOperationException("xor() is undefined for "
                + this.getClass().getName());
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < vectorSize; i++) {
            if (i > 0) {
                res.append(" ");
            }

            int wordNum = i >> 4;          // div 16
            int bucketShift = (i & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

            res.append(bucketValue);
        }

        return res.toString();
    }

    // Writable

    @Override
    public void write(DataOutput out) throws IOException {
        super.write(out);
        int sizeInWords = buckets2words(vectorSize);
        for (int i = 0; i < sizeInWords; i++) {
            out.writeLong(buckets[i]);
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        int sizeInWords = buckets2words(vectorSize);
        buckets = new long[sizeInWords];
        for (int i = 0; i < sizeInWords; i++) {
            buckets[i] = in.readLong();
        }
    }
}