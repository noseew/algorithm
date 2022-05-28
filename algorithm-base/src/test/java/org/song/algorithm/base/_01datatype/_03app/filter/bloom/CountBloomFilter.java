package org.song.algorithm.base._01datatype._03app.filter.bloom;


/*
CBF
计数布隆过滤器
和普通布隆过滤器相比, 
优点: 
    支持添加和删除
    支持计数
缺点:
    当前实现版本是一个计数器int对应位图中的一个bit, 计数器的空间非常大, 
    还有一种方式也是论文原版方式, 就是使用int/long中的5位
    
标准Bloom filter对于需要精确检测结果的场景将不再适用，而带计数器的Bloom filter的出现解决了这个问题。Counting Bloom filter实际只是在标准Bloom filter的每一个位上都额外对应得增加了一个计数器，

https://blog.csdn.net/vipshop_fin_dev/article/details/102647115
 */
public class CountBloomFilter extends BloomFilter {

    /**
     * 和位图对应的一个计数数组, 用来记录该bit位存储了多少次数据
     * 由于计数数组和bit位长度相同, 所以计数数组空间占用比较大
     * 
     * 由于已经有计数了, 所以也可以没有使用位图, 计数=0就等价于位图=0, 不过这里还是用了位图
     * 具体参见 org.song.algorithm.base._01datatype._03app.filter.bloom.fromos.bloom.CountingBloomFilter
     * 
     * 还有一种实现是用于LFU缓存计数
     * 具体参见 org.song.algorithm.base._01datatype._03app.counter.LFU02_CountMinSketch
     */
    protected int[] counter;

    public CountBloomFilter(int initSize, int maxSize) {
        super(initSize, maxSize);
        counter = new int[this.maxMask + 1];
    }

    /**
     * 从过滤器中删除key
     * 
     * @param key
     */
    public void remove(String key) {
        if (contains(key)) {
            for (Hash hash : Hash.values()) {
                int index = hash.calculate(key) & this.maxMask;
                // 计数-1
                counter[index]--;
            }
        }
    }

    /**
     * 计算布隆过滤器中 key 存储的数量, 以bit位中最小的计数为准
     * 
     * @param key
     * @return
     */
    public int count(String key) {
        int count = -1;
        for (Hash hash : Hash.values()) {
            int index = hash.calculate(key) & this.maxMask;
            if (count == -1) {
                count = counter[index];
            } else {
                count = Math.min(count, counter[index]);
            }
        }
        return Math.max(count, 0);
    }

    @Override
    protected void postAdd(String key, int index) {
        counter[index]++;
    }
}
