package org.song.algorithm.base._02alg.thinking.bloom;

import org.junit.Test;

/**
 * 布隆过滤器
 * 实现一个简单版的布隆过滤器
 */
public class BloomFilterTest {

    @Test
    public void test_01_start() {
        BloomFilter bloomFilter = new BloomFilter(1 << 16, 1 << 16);
        System.out.println("存放----------------");
        for (int i = 0; i < 5; i++) {
            bloomFilter.add(i + "");
        }

        System.out.println("判断----------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + ": " + bloomFilter.contains(i + ""));
        }

    }


}
