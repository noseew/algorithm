package org.song.algorithm.base._01datatype._02high.hashmap._01base;

import org.junit.jupiter.api.Test;

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
    
    /*
    hash 函数的计算方法
    java 中hash值是一个32位的数
     */
    @Test
    public void test02() {
        // int
        int n = 0;
        System.out.println(Integer.toBinaryString(n)); // 1. 直接取对应数的二进制即可
        System.out.println(n); // 2. 重写的hash函数直接返回该值本身, 同时也是 Integer 实现方式
        
        // float
        float f = 0.0F;
        System.out.println(Float.floatToIntBits(f)); // 1. 直接取对应数的二进制即可, 通过native方法获取原始值, 然后经过一些列范围运算得到
        
        // long
        long l = 1L;
        System.out.println(l & (-1)); // 1. 直接取低位32位
        System.out.println(l & (-1L << 32)); // 2. 直接取高位32位
        System.out.println(l ^ (l >>> 32)); // 3. 高低32位异或, 同时也是 Long 的hash函数实现方式
        
        // double
        double d = 0.0D;
        System.out.println(Double.doubleToLongBits(d)); // 1. 先转成long, 然后用long的方式
        
        // char
        char c = 'a';
        System.out.println(c); // 1. char 本身就是int
        
        // String
        String s = "";
        System.out.println(s.hashCode());
        /*
        String 是由 char 组成的, 每个char对应一个int c, 总共有m个c, 同时对应一个常数n(随便什么数都行, jdk中, n=31)
        假设 m = 4;
            h = c1*n^3 + c2*n^2 + c3*n + c4; // n^x 会多次计算
            等价于
            h = ((c1*n + c2) * n + c3) * n + c4; // 效率更高
        为什么n采用31
            1. 他是奇素数, 31*i = (i << 5) - i, 当JVM遇到此类的乘法运算会自动优化成位运算, 提高效率
            因此上述计算方法等价于, 替换成i
            
         */

        int h = 0;
        char val[] = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            h = 31 * h + val[i]; // h 每次循环都利用前一次的计算结果
//                h = ((h + val[i]) << 5) - (h + val[i]); // 使用31等价的结果, 效率更高
        }
        System.out.println(h);
    }
}
