package org.song.algorithm.algorithmbase._01datatype._02high.skiplist._01model_redis;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class BaseRandomAlg {
    
    /*
    随机生成数, 
    要求:
    1. 0~32
    2. 
        0的概率=1/(2^1)=1/2, 不生成索引
        1的概率=1/(2^2)=1/4, 生成1层索引
        2的概率=1/(2^3)=1/8, 生成2层索引
        ...
        
    思路: 从低位开始, 连续为1的数量就是指定随机数的数量
     */
    
    @Test
    public void test01() {
        Random r = new Random();

        for (int j = 0; j < 16; j++) {
            int nextInt = r.nextInt(Integer.MAX_VALUE);
            int level = 0;
            // 最高层数 == headerIndex 的层数
            for (int i = 1; i <= 32; i++) {
                // 从低位开始, 连续为1的数量就是指定随机数的数量
                if ((nextInt & 0B1) != 0B1) break;
                nextInt = nextInt >>> 1;
                level++;
            }
            System.out.println(level);
        }
        
    }
}
