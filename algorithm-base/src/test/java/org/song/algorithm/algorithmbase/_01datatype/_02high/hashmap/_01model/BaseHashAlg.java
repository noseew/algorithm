package org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model;

import org.junit.jupiter.api.Test;
import sun.security.provider.MD5;
import sun.security.rsa.RSASignature;

import java.util.concurrent.ThreadLocalRandom;

public class BaseHashAlg {
    
    /*
    hash 计算方法
    规则: 简单, 均匀
    1. 直接定址法, hash算出来是什么放在数组的下标中就是什么
        hash(key) = a*key+b; ab为常数
    2. 除留余数法, key按照数组长度取余
        hash(key) = key%len; len为数组长度
    3. 随机数法, 目的是让key更均匀, 是否要求对同一个key, 每次随机都得到相同的结果
        hash(key) = rand(key)%len; len为数组长度
    4. 数字分析法; 特殊数字特殊处理
    5. 平方取中法, 适用于事先不知道key的分布, 且key不是很大的情况
        hash(key) = key^2 取中间的几个数, 具体取几个根据情况来
    6. 折叠法, 实现不知道key的分布, 优点每一位都参与了hash运算
        边界折叠: 高位低位折叠后再进行计算, 假设key为12位, 最终要求长度为3位, 将key折叠3次, 分成3个数, 然后相加取边界的3个数
        位移折叠: 高低低位位移后再进行计算, 总共12位, 最终要求6位, 则高6位平移和低6位相同然后计算
    7. 基数转换法: 转换进制或者将小数变为整数
    8. 全域散列法: 多种hash算法共用, 类似于布隆过滤器?
    
    这里值采用常用的算法进行测试
     */
    
    @Test
    public void test01() {
        
    }

    public int hash1_addr(int key, int len) {
        /*
        直接定址法, 要求 len >= key
        直接返回key本身, key本身的值当成数组的下标, 
        可以进行一些简单的处理
        hash(key) = a*key+b; ab为常数
         */
        return key;
    }

    public int hash2_mod(int key, int len) {
        /*
        除留余数法, 好处简单高效, 缺点高位不参与计算
         */
        return key % len;
    }

    public int hash_square(int key, int len) {
        /*
        平方取中法
        这里展示按照10进制取中
         */
        // 规定最长位数
        int n = 1, lenTemp = len;
        while ((lenTemp = (lenTemp / 10)) > 0) {
            n++;
        }
        
        char[] chars = String.valueOf(key * key).toCharArray();
        // 取中长的长度n个数
        int start = (chars.length / 2) - (n / 2);
        
        int hash = 0;
        for (int i = 1; i <= n; i++) {
            hash += Integer.parseInt(chars[start++] + "") * 10;
        }
        return hash;
    }
}
