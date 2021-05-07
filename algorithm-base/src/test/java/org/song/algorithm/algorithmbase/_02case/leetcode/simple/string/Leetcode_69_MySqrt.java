package org.song.algorithm.algorithmbase._02case.leetcode.simple.string;

import org.junit.Test;

/**
 * 69. x 的平方根
 *
 * 实现 int sqrt(int x) 函数。
 *
 * 计算并返回 x 的平方根，其中 x 是非负整数。
 *
 * 由于返回类型是整数，结果只保留整数的部分，小数部分将被舍去。
 *
 * 示例 1:
 *
 * 输入: 4
 * 输出: 2
 * 示例 2:
 *
 * 输入: 8
 * 输出: 2
 * 说明: 8 的平方根是 2.82842...,
 *      由于返回类型是整数，小数部分将被舍去。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/sqrtx
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class Leetcode_69_MySqrt {

    @Test
    public void test() {

    }

    /**
     * 计算平方根公式
     * 1. 将数字两两一组分好, 如果是奇数位则第一组个数为1
     * 2. 每一组单独计算最近似的平方根, 计算出的数就是 len / 2 个最近似的数
     * 3. 余数和下一组组成一组3位数的数
     *
     * @param x
     * @return
     */
    public int mySqrt(int x) {
        int len = 0;
        int x_temp = x;
        while (x_temp > 0) {
            x_temp /= 10;
            len++;
        }

        return 0;
    }
}
