package org.song.algorithm.base._02alg.classical._01sort.alg.noncpr;

import org.junit.Test;
import org.song.algorithm.base._01datatype._01base._01linear.list._01model.ArrayBase01;
import org.song.algorithm.base._02alg.classical._01sort.AbstractSort;

import java.util.Arrays;

/*
基数排序
基数排序可以看作桶排序的扩展, 它是一种多关键字排序算法, 每个桶中数据的范围称作基数

思路和步骤
1. 分桶
    和分桶算法一样, 不过这里要分成多组桶, 多个组的桶保证把数据各个纬度的排序都覆盖到
2. 逐个桶组, 将数据分配到桶中, 然后收集, 然后再分配到下一组桶中, 然后在收集, 直到所有的桶走完, 得到的数据就是排好序的数据

假设两组桶就能得到最终的排序, 则两组桶分配的数据就像是将数据平铺二维化(比如 从左下 -> 右上 排序递增)桶对应着就是二维的坐标, 此时再按照坐标进行收集, 得到的数据就是排好序的数据

本例中采用的是按10进制位分桶, 求余数定位桶, 其他类型的数据可以有自己的规则
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
            Integer[] datas = (Integer[]) cs;

            int maxDigit = getDigit(getMax(datas));

            for (int i = 0; i < maxDigit; i++) {
                // 经过第i个桶, 先按照第1位, 进行分桶和收集, 然后按照第2位, 以此类推, 每一次的分桶和收集, 都是该位数的数字进行排序了
                ArrayBase01<Comparable>[] byMod = distributionByDigitsMod(datas, i);
                collect(byMod, cs);
            }

        }

        /**
         * 按位数余数分组
         * 先按照第1位分桶, 再按照第2位分桶, 每一位数都分成10个桶, 所以直接取余运算
         *
         * @param datas
         * @param digit
         */
        private ArrayBase01<Comparable>[] distributionByDigitsMod(Integer[] datas, int digit) {

            ArrayBase01<Comparable>[] buckets = initBucket(10);

            for (Integer d : datas) {
                // 定位到第几个桶, 并放入
                double pow = Math.pow(10d, digit);
                buckets[(d / (int) pow) % 10].add(d);
            }

            return buckets;
        }

        private int getMax(Integer[] datas) {
            Integer max = null, min = null;
            for (Integer c : datas) {
                if (max == null || greater(c, max)) max = c;
                if (min == null || less(c, min)) min = c;
            }
            return max;
        }

        /**
         * 获取数的10进制位数
         *
         * @param num
         * @return
         */
        private int getDigit(int num) {
            int maxDigit = 0;
            while (num > 0) {
                maxDigit++;
                num = num / 10;
            }
            return maxDigit;
        }

        /**
         * 将桶内数据收集到原数组
         *
         * @param buckets
         * @param cs
         */
        private void collect(ArrayBase01<Comparable>[] buckets, Comparable[] cs) {
            int index = 0;
            for (ArrayBase01<Comparable> bucket : buckets) {
                if (bucket.isEmpty()) {
                    continue;
                }
                Integer[] temp = new Integer[bucket.length()];
                for (int i = 0; i < bucket.length(); i++) {
                    cs[index++] = (Integer) bucket.get(i);
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
    }

    /**
     * 左程云
     */
    public static class RadixSort2 {

        public static int[] sort(int[] arr) {
            int[] result = new int[arr.length];
            int[] count = new int[10];


            for (int i = 0; i < 3; i++) {
                int division = (int) Math.pow(10, i);
                System.out.println(division);
                for (int j = 0; j < arr.length; j++) {
                    int num = arr[j] / division % 10;
                    count[num]++;
                }
                for (int m = 1; m < count.length; m++) {
                    count[m] = count[m] + count[m - 1];
                }
                for (int n = arr.length - 1; n >= 0; n--) {
                    int num = arr[n] / division % 10;
                    result[--count[num]] = arr[n];
                }
                System.arraycopy(result, 0, arr, 0, arr.length);
                Arrays.fill(count, 0);
            }
            return result;
        }
    }

    public static class RadixSort3 {
        protected int[] array;

        protected void sort() {
//         找出最大值
            int max = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
//         个位数:
//         array[i] / 1 % 10 = 3
//         十位数：array[i] / 10 % 10 = 9
//         百位数：array[i] / 100 % 10 = 5
//         千位数：array[i] / 1000 % 10 = ...
            for (int divider = 1; divider <= max; divider *= 10) {
                countingSort(divider);
            }
        }

        protected void countingSort(int divider) {
//         开辟内存空间，存储次数
            int[] counts = new int[10];
//         统计每个整数出现的次数
            for (int i = 0; i < array.length; i++) {
                counts[array[i] / divider % 10]++;
            }
//         累加次数
            for (int i = 1; i < counts.length; i++) {
                counts[i] += counts[i - 1];
            }
//         从后往前遍历元素，将它放到有序数组中的合适位置
            int[] newArray = new int[array.length];
            for (int i = array.length - 1; i >= 0; i--) {
                newArray[--counts[array[i] / divider % 10]] = array[i];
            }
//         将有序数组赋值到array
            for (int i = 0; i < newArray.length; i++) {
                array[i] = newArray[i];
            }
        }
    }


}
