package org.song.algorithm.base._02alg.thinking.bloom.fromos;

import org.song.algorithm.base._02alg._03app.hash.Hash;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 来自网络, 原始来自Hadoop源码
 * 
 * filter接口
 * <p>
 * 过滤器是一种数据结构，其目的是提供<code>A</code>的有损摘要。
 * 关键思想是通过使用几个散列函数将<code>A</code>(也称为<i>key </i>)的条目映射到向量中的几个位置。
 * <p>
 * 通常，过滤器将被实现为Bloom过滤器(或Bloom过滤器扩展)。
 * <p>
 * 为了定义真正的行为，必须对其进行扩展。
 *
 * @see Key The general behavior of a key
 * @see HashFunction A hash function
 */
public abstract class Filter {
    private static final int VERSION = -1; // negative to accommodate for old format 
    /**
     * 过滤器向量大小
     */
    protected int vectorSize;

    /**
     * 用于将一个键映射到向量中的多个位置的哈希函数
     */
    protected HashFunction hash;

    /**
     * 哈希函数的个数
     */
    protected int nbHash;

    /**
     * 要使用的哈希函数的类型
     */
    protected int hashType;

    protected Filter() {
    }

    /**
     * Constructor.
     *
     * @param vectorSize The vector size of <i>this</i> filter.
     * @param nbHash     The number of hash functions to consider.
     * @param hashType   type of the hashing function (see {@link Hash}).
     */
    protected Filter(int vectorSize, int nbHash, int hashType) {
        this.vectorSize = vectorSize;
        this.nbHash = nbHash;
        this.hashType = hashType;
        this.hash = new HashFunction(this.vectorSize, this.nbHash, this.hashType);
    }

    /**
     * 向过滤器中添加key
     *
     * @param key The key to add.
     */
    public abstract void add(Key key);

    /**
     * 判断key是否存在过滤器内
     *
     * @param key The key to test.
     * @return boolean True if the specified key belongs to <i>this</i> filter.
     * False otherwise.
     */
    public abstract boolean membershipTest(Key key);

    /**
     * 和当前过滤器进行与运算
     * 
     * Peforms a logical AND between <i>this</i> filter and a specified filter.
     * <p>
     * <b>Invariant</b>: The result is assigned to <i>this</i> filter.
     *
     * @param filter The filter to AND with.
     */
    public abstract void and(Filter filter);

    /**
     * 和当前过滤器进行或运算
     * 
     * Peforms a logical OR between <i>this</i> filter and a specified filter.
     * <p>
     * <b>Invariant</b>: The result is assigned to <i>this</i> filter.
     *
     * @param filter The filter to OR with.
     */
    public abstract void or(Filter filter);

    /**
     * 和当前过滤器进行异或运算
     * 
     * Peforms a logical XOR between <i>this</i> filter and a specified filter.
     * <p>
     * <b>Invariant</b>: The result is assigned to <i>this</i> filter.
     *
     * @param filter The filter to XOR with.
     */
    public abstract void xor(Filter filter);

    /**
     * 当前过滤器进行非运算
     * 
     * Performs a logical NOT on <i>this</i> filter.
     * <p>
     * The result is assigned to <i>this</i> filter.
     */
    public abstract void not();

    /**
     * Clear all its fields
     */
    protected void clear() {
        // default empty implementation, sub-class may override it
    }

    /**
     * 批量添加key
     *
     * @param keys The list of keys.
     */
    public void add(List<Key> keys) {
        if (keys == null) {
            throw new IllegalArgumentException("ArrayList<Key> may not be null");
        }

        for (Key key : keys) {
            add(key);
        }
    }//end add()

    /**
     * 批量添加key
     *
     * @param keys The collection of keys.
     */
    public void add(Collection<Key> keys) {
        if (keys == null) {
            throw new IllegalArgumentException("Collection<Key> may not be null");
        }
        for (Key key : keys) {
            add(key);
        }
    }//end add()

    /**
     * 批量添加key
     *
     * @param keys The array of keys.
     */
    public void add(Key[] keys) {
        if (keys == null) {
            throw new IllegalArgumentException("Key[] may not be null");
        }
        for (int i = 0; i < keys.length; i++) {
            add(keys[i]);
        }
    }//end add()

    // Writable interface

    public void write(DataOutput out) throws IOException {
        out.writeInt(VERSION);
        out.writeInt(this.nbHash);
        out.writeByte(this.hashType);
        out.writeInt(this.vectorSize);
    }

    public void readFields(DataInput in) throws IOException {
        int ver = in.readInt();
        if (ver > 0) { // old unversioned format
            this.nbHash = ver;
            this.hashType = Hash.JENKINS_HASH;
        } else if (ver == VERSION) {
            this.nbHash = in.readInt();
            this.hashType = in.readByte();
        } else {
            throw new IOException("Unsupported version: " + ver);
        }
        this.vectorSize = in.readInt();
        this.hash = new HashFunction(this.vectorSize, this.nbHash, this.hashType);
    }
}//end class