package org.song.algorithm.base._02alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.base._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.base._02alg._01sort.AbstractSort;

/*
桶排序将待排序序列划分成若干个区间, 每个区间可形象地看作一个桶, 
如果桶中的记录多于一个则使用较快的排序方法进行排序, 把每个桶中的记录收集起来, 最终得到有序序列

思路和步骤: 
1. 分桶, 
    桶采用数组表示(这样选择桶才能是O(1)的), 选择桶效率是O(1)将数据装入桶内O(n)
    多个桶之间本身是有序的, 例如: 桶1数据范围[1~5], 桶2数据范围[6~10], 这样装入桶内的数据, 自然就是有序的
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
            
            int bucketSize = 10;
            ArrayBase01<Comparable>[] buckets = initBucket(bucketSize);

            // 数据分配入桶
            distributionBucket((Integer[]) cs, buckets);

            // 分桶排序
            int index = 0;
            AbstractSort sort = new Sort_03_Insert.InsertSort1();
            for (ArrayBase01<Comparable> bucket : buckets) {
                if (bucket.isEmpty()) {
                    continue;
                }
                Integer[] temp = new Integer[bucket.length()];
                for (int i = 0; i < bucket.length(); i++) {
                    temp[i] = (Integer) bucket.get(i);
                }
                // 桶内排序
                sort.sort(temp);

                // 收集到原数组
                for (Integer d : temp) {
                    cs[index++] = d;
                }
            }
        }

        private ArrayBase01<Comparable>[] initBucket(int bucketSize) {
            // 初始化桶
            ArrayBase01<Comparable>[] buckets = new ArrayBase01[bucketSize];
            for (int i = 0; i < bucketSize; i++) {
                buckets[i] = new ArrayBase01<Comparable>();
            }
            return buckets;
        }

        /**
         * 数据入桶
         * 采用数据大小分段的方式, 这里暂时将桶大小写死
         * 分桶排序总的分桶规则, 一定是将数据分成多个桶之间有序的, 比如 一定 桶1<=桶2
         * 
         * @param datas
         * @param buckets
         */
        private void distributionBucket(Integer[] datas, ArrayBase01<Comparable>[] buckets) {
            // 数据值的范围
            Integer max = null, min = null;
            for (Integer c : datas) {
                if (max == null || greater(c, max)) max = c;
                if (min == null || less(c, min)) min = c;
            }
            // 桶数据大小 这里分桶的规则是按照值的范围写死
            int step = ((max - min) / buckets.length) + 1;
            for (Integer d : datas) {
                // 定位到第几个桶, 并放入
                buckets[(d - min) / step].add(d);
            }
        }
    }

}
