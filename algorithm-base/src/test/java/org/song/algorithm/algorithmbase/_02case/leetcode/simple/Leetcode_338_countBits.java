package org.song.algorithm.algorithmbase._02case.leetcode.simple;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase.utils.BinaryUtils;

import java.util.Arrays;

/**
 * 338. 比特位计数
 * 给定一个非负整数 num。对于 0 ≤ i ≤ num 范围中的每个数字 i ，计算其二进制数中的 1 的数目并将它们作为数组返回。
 */
public class Leetcode_338_countBits {

    @Test
    public void test() {

        System.out.println(Arrays.toString(countBits(16)));
    }
    
    public int[] countBits(int n) {
        
        int m = n;
        m |= (m >>> 1);
        m |= (m >>> 2);
        m |= (m >>> 4);
        m |= (m >>> 8);
        m |= (m >>> 16);
        m += 1;

        int size = 0;
        while (m > 0) {
            size++;
            m = (m >> 1);
        }
        if (size == 0) {
            return new int[]{};
        }
        int[] ints = new int[size + 1];

        return ints;
    }


}
