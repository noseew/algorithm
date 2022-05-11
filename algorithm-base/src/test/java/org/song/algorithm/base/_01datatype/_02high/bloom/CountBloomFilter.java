package org.song.algorithm.base._01datatype._02high.bloom;


/*
计数布隆过滤器
和普通布隆过滤器相比, 
优点: 
    支持添加和删除
    支持计数
缺点:
    内存消耗是标准布隆过滤器的3-4倍
    
标准Bloom filter对于需要精确检测结果的场景将不再适用，而带计数器的Bloom filter的出现解决了这个问题。Counting Bloom filter实际只是在标准Bloom filter的每一个位上都额外对应得增加了一个计数器，
 */
public class CountBloomFilter extends BloomFilter {

    /**
     * 和位图对应的一个计数数组, 用来记录该bit位存储了多少次数据
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
