package org.song.algorithm.algorithmbase._01base.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 堆排序
 * 非比较排序, 而是利用额外工具 堆(也可以将数组原地变堆), 进行排序,
 * 思路和选择排序类似
 * 选择排序: 在剩下的数组中选择最小的数据, 依次码放
 * 堆排序: 从堆中选择最小的数据, 依次码放, 区别在于,
 * -    堆选出来最小的数据的复杂度是O(1), 这是由堆得性质决定的,
 * -    同时, 还要将数组中的数据放入堆中
 * -    如果是原地变堆, 则过程是将堆顶元素和数组最后元素交换
 *
 * 复杂度是 O(logn), 主要在数组堆化这一步是 O(logn) 的
 */
public class Sort_Heap {


    @Test
    public void test() {
        int[] arr = new int[]{3, 0, 4, 2, 1};
        System.out.println(Arrays.toString(sort_01(arr)));
    }

    /**
     */
    public static int[] sort_01(int[] data) {

        return data;
    }
}
