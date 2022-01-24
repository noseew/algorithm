package org.song.algorithm.algorithmbase._02alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;

/*
桶排序将待排序序列划分成若干个区间, 每个区间可形象地看作一个桶, 
如果桶中的记录多于一个则使用较快的排序方法进行排序, 把每个桶中的记录收集起来, 最终得到有序序列

思路和步骤: 
1. 分桶, 
    桶采用数组表示(这样选择桶才能是O(1)的), 选择桶效率是O(1)将数据装入桶内O(n)
    多个桶之间本身是有序的, 这样装入桶内的数据, 自然就是有序的
2. 桶内排序, 
    同一个桶内可能会出现多个数据(类似于hash), 可以采用常用的排序算法
3. 收集, 将每个排好序的桶的数据收集起来, 最终的数据就是排好序的数据

注意:
    - 桶排序的数据最好是均匀分布的. 
    - 桶排序针对不同的数据选择的划分方法是不同的. 
    - 桶内排序时使用的比较排序算法也有可能不同. 可以使用直接插入排序, 也可以使用快速排序. 
    
分桶的规则
    1. 要实现桶自身的有序性, 
        比如按照位数分桶, 1位数的数字第一个桶, 2位数的数据第二个桶, 以此类推
    2. 
 */
public class Sort_08_Bucket {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new BucketSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /*
     */
    public static class BucketSort extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
        }
    }

}
