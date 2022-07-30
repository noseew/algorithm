package org.song.algorithm.base._01datatype._02high.bloom.fromos;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 实现一个 dynamic Bloom filter (DBF)，如INFOCOM 2006论文中定义的那样。
 * <p>
 * 动态Bloom filter (DBF)使用一个<code>s * m</code>位矩阵，但是每个<code>s</code>行都是一个标准的Bloom filter。
 * DBF的创建过程是迭代的。DBF一开始是一个<code>1 * m</code>位矩阵，即由单个标准Bloom filter组成。
 * 假设在初始位向量中记录<code>n<sub>r</sub></code>元素，
 * 其中<code>n<sub>r</sub> &lt;= n</code> (<code>n</code>是<code>A</code>集在过滤器中记录的基数)。
 * <p>
 * 在应用程序的执行过程中，当<code>A</code>的大小增加时，必须在DBF中插入几个键。
 * 当向DBF中插入一个密钥时，必须首先在矩阵中得到一个活跃的Bloom filter。
 * 当记录的键数<code>n<sub>r</sub></code>严格小于<code>A</code>， <code>n</code>的当前基数时，Bloom filter是活跃的。
 * 如果发现一个活跃的Bloom filter，则插入该键，并且<code>n<sub>r</sub></code>加1。
 * 另一方面,如果没有活跃的布隆过滤器,创建一个新的(例如,添加一个新行矩阵)根据当前大小的<代码> < /代码>和元素添加在这个新的布隆过滤器和<代码> n <子> r < /订阅> > < /代码的价值这一新的布隆过滤器设置为1。
 * 如果矩阵行中的<code>k</code>的位置被设为1，那么给定的键就属于DBF。
 * <p>
 * Originally created by
 * <a href="http://www.one-lab.org">European Commission One-Lab Project 034819</a>.
 *
 * @see Filter The general behavior of a filter
 * @see BloomFilter A Bloom filter
 * @see <a href="http://www.cse.fau.edu/~jie/research/publications/Publication_files/infocom2006.pdf">Theory and Network Applications of Dynamic Bloom Filters</a>
 */
public class DynamicBloomFilter extends Filter {
    /**
     * 动态Bloom过滤器行中记录的最大键数的阈值。
     * Threshold for the maximum number of key to record in a dynamic Bloom filter row.
     */
    private int nr;

    /**
     * 当前标准活动Bloom过滤器中记录的键数。
     * The number of keys recorded in the current standard active Bloom filter.
     */
    private int currentNbRecord;

    /**
     * The matrix of Bloom filter.
     */
    private BloomFilter[] matrix;

    /**
     * Zero-args constructor for the serialization.
     */
    public DynamicBloomFilter() {
    }

    /**
     * Constructor.
     * <p>
     * Builds an empty Dynamic Bloom filter.
     *
     * @param vectorSize The number of bits in the vector. 位图最大位数
     * @param nbHash     The number of hash function to consider.
     * @param hashType   type of the hashing function (see
     *                   {@link org.apache.hadoop.util.hash.Hash}).
     * @param nr         The threshold for the maximum number of keys to record in a
     *                   dynamic Bloom filter row.
     *                   动态Bloom过滤器行中记录的最大键数的阈值
     */
    public DynamicBloomFilter(int vectorSize, int nbHash, int hashType, int nr) {
        super(vectorSize, nbHash, hashType);

        this.nr = nr; // 每个过滤器中最大key阈值
        this.currentNbRecord = 0; // 当前过滤器中key数量

        matrix = new BloomFilter[1];
        matrix[0] = new BloomFilter(this.vectorSize, this.nbHash, this.hashType);
    }

    @Override
    public void add(Key key) {
        if (key == null) {
            throw new NullPointerException("Key can not be null");
        }

        BloomFilter bf = getActiveStandardBF();

        if (bf == null) {
            addRow();
            bf = matrix[matrix.length - 1];
            currentNbRecord = 0;
        }

        bf.add(key);

        currentNbRecord++;
    }

    @Override
    public void and(Filter filter) {
        if (filter == null
                || !(filter instanceof DynamicBloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be and-ed");
        }

        DynamicBloomFilter dbf = (DynamicBloomFilter) filter;

        if (dbf.matrix.length != this.matrix.length || dbf.nr != this.nr) {
            throw new IllegalArgumentException("filters cannot be and-ed");
        }

        for (int i = 0; i < matrix.length; i++) {
            matrix[i].and(dbf.matrix[i]);
        }
    }

    @Override
    public boolean membershipTest(Key key) {
        if (key == null) {
            return true;
        }

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].membershipTest(key)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void not() {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i].not();
        }
    }

    @Override
    public void or(Filter filter) {
        if (filter == null
                || !(filter instanceof DynamicBloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be or-ed");
        }

        DynamicBloomFilter dbf = (DynamicBloomFilter) filter;

        if (dbf.matrix.length != this.matrix.length || dbf.nr != this.nr) {
            throw new IllegalArgumentException("filters cannot be or-ed");
        }
        for (int i = 0; i < matrix.length; i++) {
            matrix[i].or(dbf.matrix[i]);
        }
    }

    @Override
    public void xor(Filter filter) {
        if (filter == null
                || !(filter instanceof DynamicBloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be xor-ed");
        }
        DynamicBloomFilter dbf = (DynamicBloomFilter) filter;

        if (dbf.matrix.length != this.matrix.length || dbf.nr != this.nr) {
            throw new IllegalArgumentException("filters cannot be xor-ed");
        }

        for (int i = 0; i < matrix.length; i++) {
            matrix[i].xor(dbf.matrix[i]);
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < matrix.length; i++) {
            res.append(matrix[i]);
            res.append(Character.LINE_SEPARATOR);
        }
        return res.toString();
    }

    // Writable

    @Override
    public void write(DataOutput out) throws IOException {
        super.write(out);
        out.writeInt(nr);
        out.writeInt(currentNbRecord);
        out.writeInt(matrix.length);
        for (int i = 0; i < matrix.length; i++) {
            matrix[i].write(out);
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        nr = in.readInt();
        currentNbRecord = in.readInt();
        int len = in.readInt();
        matrix = new BloomFilter[len];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = new BloomFilter();
            matrix[i].readFields(in);
        }
    }

    /**
     * Adds a new row to <i>this</i> dynamic Bloom filter.
     */
    private void addRow() {
        BloomFilter[] tmp = new BloomFilter[matrix.length + 1];

        for (int i = 0; i < matrix.length; i++) {
            tmp[i] = matrix[i];
        }

        tmp[tmp.length - 1] = new BloomFilter(vectorSize, nbHash, hashType);

        matrix = tmp;
    }

    /**
     * 返回<i>中活跃的标准Bloom filter (</i>动态Bloom filter)。
     * 
     * Returns the active standard Bloom filter in <i>this</i> dynamic Bloom filter.
     *
     * @return BloomFilter The active standard Bloom filter.
     * <code>Null</code> otherwise.
     */
    private BloomFilter getActiveStandardBF() {
        if (currentNbRecord >= nr) {
            return null;
        }

        return matrix[matrix.length - 1];
    }
}