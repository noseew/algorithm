package org.song.algorithm.base._01datatype._02high.bloom;

import org.junit.Test;

public class CountBloomFilterTest {

    @Test
    public void test_01_start() {
        CountBloomFilter bloomFilter = new CountBloomFilter(1 << 16, 1 << 16);
        System.out.println("存放----------------");
        for (int i = 0; i < 5; i++) {
            bloomFilter.add(i + "");
        }

        System.out.println("判断----------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + ": " + bloomFilter.contains(i + ""));
        }
        
        System.out.println("重复存放----------------");
        for (int i = 0; i < 5; i++) {
            bloomFilter.add(i + "");
        }
        System.out.println("获取次数----------------");
        for (int i = 0; i < 10; i++) {
            System.out.println(i +" 次数 "+ bloomFilter.count(i + ""));
        }
        

    }
}
