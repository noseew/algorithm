package org.song.algorithm.base._01datatype._02high.bloom.fromos;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.BitSet;

/**
 * 内容来自网络, 原始来自Hadoop源码
 * <p>
 * 实现一个<i>Bloom filter</i>，由Bloom在1970年定义。
 * <p>
 * Bloom过滤器是1970年引入的一种数据结构，在过去十年中，由于它为网络主机之间的集合成员信息传输提供了带宽效率，
 * 网络研究团体已经采用了这种数据结构。发送方将信息编码成比特向量，即布卢姆滤波器，它比传统的表示形式更紧凑。
 * 计算和空间成本的建设是线性的数量的元素。接收方使用过滤器来测试各种元素是否属于集合。
 * 尽管过滤器偶尔会返回假阳性，但它永远不会返回假阴性。在创建过滤器时，发送方可以在假阳性率和大小之间选择自己想要的点。
 *
 * <p>
 * Originally created by
 * <a href="http://www.one-lab.org">European Commission One-Lab Project 034819</a>.
 *
 * @see Filter The general behavior of a filter
 * @see <a href="http://portal.acm.org/citation.cfm?id=362692&dl=ACM&coll=portal">Space/Time Trade-Offs in Hash Coding with Allowable Errors</a>
 */
public class BloomFilter extends Filter {
    private static final byte[] bitvalues = new byte[]{
            (byte) 0x01,
            (byte) 0x02,
            (byte) 0x04,
            (byte) 0x08,
            (byte) 0x10,
            (byte) 0x20,
            (byte) 0x40,
            (byte) 0x80
    };

    /**
     * bit向量
     * <p>
     * The bit vector.
     */
    BitSet bits;

    public BloomFilter() {
        super();
    }

    /**
     * Constructor
     *
     * @param vectorSize 过滤器中bit向量的大小
     * @param nbHash     哈希函数的个数
     * @param hashType   哈希函数 (see {@link org.apache.hadoop.util.hash.Hash}).
     */
    public BloomFilter(int vectorSize, int nbHash, int hashType) {
        super(vectorSize, nbHash, hashType);

        bits = new BitSet(this.vectorSize);
    }

    @Override
    public void add(Key key) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            bits.set(h[i]);
        }
    }

    @Override
    public void and(Filter filter) {
        if (filter == null
                || !(filter instanceof BloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be and-ed");
        }

        this.bits.and(((BloomFilter) filter).bits);
    }

    @Override
    public boolean membershipTest(Key key) {
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }

        int[] h = hash.hash(key);
        hash.clear();
        for (int i = 0; i < nbHash; i++) {
            if (!bits.get(h[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void not() {
        bits.flip(0, vectorSize - 1);
    }

    @Override
    public void or(Filter filter) {
        if (filter == null
                || !(filter instanceof BloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be or-ed");
        }
        bits.or(((BloomFilter) filter).bits);
    }

    @Override
    public void xor(Filter filter) {
        if (filter == null
                || !(filter instanceof BloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be xor-ed");
        }
        bits.xor(((BloomFilter) filter).bits);
    }

    @Override
    public String toString() {
        return bits.toString();
    }

    /**
     * @return size of the the bloomfilter
     */
    public int getVectorSize() {
        return this.vectorSize;
    }

    // Writable

    @Override
    public void write(DataOutput out) throws IOException {
        super.write(out);
        byte[] bytes = new byte[getNBytes()];
        for (int i = 0, byteIndex = 0, bitIndex = 0; i < vectorSize; i++, bitIndex++) {
            if (bitIndex == 8) {
                bitIndex = 0;
                byteIndex++;
            }
            if (bitIndex == 0) {
                bytes[byteIndex] = 0;
            }
            if (bits.get(i)) {
                bytes[byteIndex] |= bitvalues[bitIndex];
            }
        }
        out.write(bytes);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        bits = new BitSet(this.vectorSize);
        byte[] bytes = new byte[getNBytes()];
        in.readFully(bytes);
        for (int i = 0, byteIndex = 0, bitIndex = 0; i < vectorSize; i++, bitIndex++) {
            if (bitIndex == 8) {
                bitIndex = 0;
                byteIndex++;
            }
            if ((bytes[byteIndex] & bitvalues[bitIndex]) != 0) {
                bits.set(i);
            }
        }
    }

    /* @return number of bytes needed to hold bit vector */
    private int getNBytes() {
        return (vectorSize + 7) / 8;
    }
}//end class