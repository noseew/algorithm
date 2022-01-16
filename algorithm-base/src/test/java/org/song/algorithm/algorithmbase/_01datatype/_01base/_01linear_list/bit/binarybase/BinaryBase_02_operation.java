package org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.bit.binarybase;

import org.junit.jupiter.api.Test;

public class BinaryBase_02_operation {
    
    /*
    位运算, 常用
        逻辑运算
            &: 与远算, 都为1则是1
            |: 或运算, 有1则是1
            ~: 非运算, 取反
            ^: 异或运算, 同0异1
        位移运算
            >>: 右移位, 带符号, 向低位移动, 正数左边补0, 负数左边补1, 相当于 /2
            >>>: 右移位, 无符号, 向低位移动, 左边补0
            <<: 左移位, 向高位移动, 向边补0, 相当于 *2
        
    补位, 负数
    
     */

    /**
     * 逻辑运算
     */
    @Test
    public void binary_01_logic() {
        // & 与
        System.out.println(0b1 & 0b1); // 1
        System.out.println(0b1 & 0b0); // 0
        System.out.println(0b0 & 0b1); // 0
        System.out.println(0b0 & 0b0); // 0

        // | 或
        System.out.println(0b1 | 0b1); // 1
        System.out.println(0b1 | 0b0); // 1
        System.out.println(0b0 | 0b1); // 1
        System.out.println(0b0 | 0b0); // 0
        
        // ~ 非 取反
        System.out.println(~0b1); // 0
        System.out.println(~0b0); // 1

        // ^ 抑或
        System.out.println(0b1 ^ 0b1);// 0
        System.out.println(0b1 ^ 0b0);// 1
        System.out.println(0b0 ^ 0b1);// 1
        System.out.println(0b0 ^ 0b0);// 0
    }

    /**
     * 位运算
     */
    @Test
    public void binary_01_shift() {

        /*
        <<: 左移位(算数 左移位)
        >>: 右移位(算数 右移位)
        >>>: 右移位(逻辑 右移位, 无符号右移位)
        <p>
        >> 算数右移位:  数字向右移, 正数高位补0/负数高位补1. 数学右移运算
        (在正数情况下, 与上一个一样, 负数时候不一样)
        <p>
        >>>右移位: 数字向右移, 高位补0. 逻辑右移运算
        数字按照二进制, 顺序向右移动, 高位补0(不分正负因此可能会出现溢出(正负转换))/低位舍去;(负数运算结果没有数学意义). 又称, 逻辑右移运算
        <p>
        移位运算的数学意义: 
        <<左移位一次, 乘以2(进制
        >>右移位n, 除以2的n次幂,
        负数向下(值小)取整, 正数(向下)取整
         */
        int num = 127;
        System.out.println(num + " 的二进制: " + Integer.toBinaryString(num)); // 15 的二进制: 1111
        System.out.println(num + " 的 << 1: " + Integer.toBinaryString(num << 1)); // 15 的 << 1: 11110
        System.out.println(num + " 的 >> 1: " + Integer.toBinaryString(num >> 1)); // 15 的 >> 1: 0111
        System.out.println(num + " 的 >>> 1: " + Integer.toBinaryString(num >>> 1)); // 15 的 >>> 1: 0111
        num = -128;
        System.out.println(num + " 的二进制: " + Integer.toBinaryString(num)); // -3 的二进制: 10001
        System.out.println(num + " 的 << 1: " + Integer.toBinaryString(num << 1)); // -3 的 << 1: 100010
        System.out.println(num + " 的 >> 1: " + Integer.toBinaryString(num >> 1)); // -3 的 >> 1: 11000
        System.out.println(num + " 的 >>> 1: " + Integer.toBinaryString(num >>> 1)); // -3 的 >>> 1: 11000

        // int 负数最小值 = int 最大值 + 1
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE + 1)); // 10000000000000000000000000000000
        // 逻辑右移位, 高位补零(默认0不显示)
        System.out.println(Integer.toBinaryString((Integer.MAX_VALUE + 1) >>> 1)); // 1000000000000000000000000000000

        /*
        
        1. 结果等于并集, 添加一个状态位: a | b, 没有方向
        2. 结果等于交集, 查看一个状态位: a & b, 没有方向
        3. 结果等于移除差集, 移除一个状态位: a & ~b, 从a中 移除 b
         */
        // 添加写权限 或
        System.out.println(Integer.toBinaryString(4 | 2)); //  0100 | 0010
        // 查看是否有写权限 与
        System.out.println(Integer.toBinaryString(6 & 2)); //  0110 & 0010
        // 减去权限 与非
        System.out.println(Integer.toBinaryString(5 & ~2)); // 0101 & 1101 (~0010)
    }

}