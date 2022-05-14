package org.song.algorithm.base._01datatype._02high.bloom;

/*
DCF
动态计数布隆过滤器, 是CountBloomFilter优化版本, 降低计数数组的空间占用

https://blog.csdn.net/jiaomeng/article/details/1543751

 */
public class DynamicCountBFilter extends BloomFilter {
    /**
     * 计数数组, 数组长度和位图数组相同, 注意不是bit位
     * 相比较CountBloomFilter, 数组大大降低
     */
    protected int[] counter;
    /**
     * 向量数组, 数组长度和位图数组相同, 注意不是bit位
     * 相比较CountBloomFilter, 数组大大降低
     */
    protected int[] vector;
    
    
    public DynamicCountBFilter(int initSize, int maxSize) {
        super(initSize, maxSize);
        counter = new int[this.bitmap.arrayLength()];
        vector = new int[this.bitmap.arrayLength()];
    }
}
