package org.song.algorithm.algorithmbase.alg._01sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class Base {
    
    /*
    时间复杂度: 记作O
    额外空间复杂度: 记录O
        除了算法本身需要的空间, 在解决该算法问题时需要申请的额外空间, 
        比如如果永续需要复制一份数组, 则复制出来的数组空间不属于额外空间, 而属于算法本身的空间
     */

    /**
     * 交换两个数的方式
     */
    @Test
    public void swap_test() {
        int[] arr = {3, 5};
//        swap_01(arr, 0, 1);
//        swap_02(arr, 0, 1);
        swap_03(arr, 0, 1);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 交换两个数
     * 这里是将数组下标中两个数进行交换
     * 使用中间变量实现
     */
    public void swap_01(int[] arr, int i1, int i2) {
        int temp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = temp;
    }

    /**
     * 交换两个数
     * 这里是将数组下标中两个数进行交换
     * 使用加减法实现
     */
    public void swap_02(int[] arr, int i1, int i2) {
        arr[i1] = arr[i1] + arr[i2];
        arr[i2] = arr[i1] - arr[i2];
        arr[i1] = arr[i1] - arr[i2];
    }

    /**
     * 交换两个数
     * 这里是将数组下标中两个数进行交换
     * 使用异或实现
     * 
     * 一个数同另一个数异或两次, 等于这个数本身
     */
    public void swap_03(int[] arr, int i1, int i2) {
        arr[i1] = arr[i1] ^ arr[i2]; // i1 和 i2 异或了一次, 或者, i2 和 i1 异或了一次, 此时的i1属于他们结果的中间变量
        
        arr[i2] = arr[i1] ^ arr[i2]; // i1又和i2异或了一次, 返回结果等于 i1本身, 将i1的值赋值给i2
        arr[i1] = arr[i1] ^ arr[i2]; // 此时的 i2=i1, i2又和i1异或了一次, 结果等于i2本身, 将i2赋值给i1
    }
}
