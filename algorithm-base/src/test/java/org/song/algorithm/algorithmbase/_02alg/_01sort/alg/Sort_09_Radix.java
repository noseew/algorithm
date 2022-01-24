package org.song.algorithm.algorithmbase._02alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;

/*
基数排序
基数排序可以看作桶排序的扩展, 它是一种多关键字排序算法, 每个桶中数据的范围称作基数

思路和步骤
1. 分桶
    和分桶算法一样, 不过这里要分成多组桶, 多个组的桶保证把数据各个纬度的排序都覆盖到
2. 逐个桶组, 将数据分配到桶中, 然后收集, 然后再分配到下一组桶中, 然后在收集, 直到所有的桶走完, 得到的数据就是排好序的数据

假设两组桶就能得到最终的排序, 则两组桶分配的数据就像是将数据平铺二维化(比如 从左下 -> 右上 排序递增)桶对应着就是二维的坐标, 此时再按照坐标进行收集, 得到的数据就是排好序的数据
 */
public class Sort_09_Radix {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new RadixSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /*
     */
    public static class RadixSort extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
        }
    }


}
