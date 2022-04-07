package org.song.algorithm.base._02alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.base._02alg._01sort.AbstractSort;

public class Sort_05_Merge {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new MergeSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    @Test
    public void test2() {
        Comparable[] build = AbstractSort.build(10);

        new MergeSort2().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /**
     * 归并排序的核心, 合并
     */
    public static abstract class AbstractMergeSort extends AbstractSort {
        /**
         * 注意: 
         * 1. 原地归并, 实现可能太过复杂, 通常都是用额外的辅助空间来实现
         * 2. 只需要一个额外数组, 之后所有的归并,都共用这个数组, 防止重复创建数组的时间开销
         * 
         */
        protected Comparable[] temp;

        /**
         * low, mid, hi, 采用左闭右开
         * 
         * @param cs
         * @param low
         * @param mid
         * @param hi
         */
        protected void merge(Comparable[] cs, int low, int mid, int hi) {
            for (int k = low; k <= hi; k++) {
                // 复制到临时数组, [low~hi], 为什么只取一半?, 因为节省性能, 后半个绝对不会被覆盖, 可以直接使用
                temp[k] = cs[k];
            }
            /*
            两个指针, 分别指的是两个有序数组的起始 index
             */
            int i = low, j = mid + 1;
            for (int k = low; k <= hi; k++) {
                // 归并到 cs
                if (i > mid) {
                    // 左边提前结束
                    // 如果i到头了, 第一个数组用完了, 则直接使用第2个数组
                    cs[k] = temp[j++];
                } else if (j > hi) {
                    // 右边提前结束
                    // 如果j到头了, 第二个数组用完了, 则直接使用第1个数组
                    cs[k] = temp[i++];
                } else if (less(temp[j], temp[i])) {
                    // j < i, 则将j合并, 同时j前进一步
                    cs[k] = temp[j++];
                } else {
                    // 否则 i <= j, 将i合并, 同时i前进一步
                    cs[k] = temp[i++];
                }
            }
        }
        
    }

    /*
    归并排序最吸引人的性质是它能够保证将任意长度为N的数组排序所需时间和 NlogN 成正比
    分治思想

    两两有序数组合并, 最小的长度为1, 也就是两个 item合并,
    流程示例: 数组下标 1,2,3,4,5,6,7,8
        [1],[2] 进行合并, [3],[4] 进行合并,...
        [1,2],[3,4] 进行合并
        [1,2,3,4],[5,6,7,8] 进行合并
        
    自顶向下的 merge
        逐个二分, 由前向后, 每有两个数组排好序就将其 merge 
     */
    public static class MergeSort extends AbstractMergeSort {

        @Override
        public void sort(Comparable[] cs) {
            // 归并所需要的辅助数组
            temp = new Comparable[cs.length];
            sort(cs, 0, cs.length - 1);
        }

        private void sort(Comparable[] cs, int low, int hi) {
            if (hi <= low) {
                return;
            }
            int mid = low + (hi - low) / 2;
            // 左半边排序
            sort(cs, low, mid);
            // 右半边排序
            sort(cs, mid + 1, hi);
            // 归并结果
            merge(cs, low, mid, hi);
        }
    }

    /*
    自底向上的 merge
        逐个二分, 由前向后, 每两个数组都将其排好序, 然后分别两两进行merge 

     */
    public static class MergeSort2 extends AbstractMergeSort {

        @Override
        public void sort(Comparable[] cs) {
            // 归并所需要的辅助数组
            temp = new Comparable[cs.length];
            int n = cs.length;
            for (int sz = 1; sz < n; sz = sz + sz) {
                for (int low = 0; low < n - sz; low += sz + sz) {
                    merge(cs, low, low + sz - 1, Math.min(low + sz + sz - 1, n - 1));
                }
            }
        }
    }


    /**
     * 左程云
     */
    public static class MergeSort3 extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
            sort(cs, 0, cs.length - 1);
        }

        public static void sort(Comparable[] arr, int left, int right) {
            if (left == right) return;
            int mid = left + (right - left) / 2;
            sort(arr, left, mid);
            sort(arr, mid + 1, right);
            merge(arr, left, mid + 1, right);
        }

        static void merge(Comparable[] arr, int leftPtr, int rightPtr, int rightBound) {
            int mid = rightPtr - 1;
            Comparable[] temp = new Comparable[rightBound - leftPtr + 1];

            int i = leftPtr;
            int j = rightPtr;
            int k = 0;

            while (i <= mid && j <= rightBound) {
                temp[k++] = lessEq(arr[i], arr[j]) ? arr[i++] : arr[j++];
            }

            while (i <= mid) temp[k++] = arr[i++];
            while (j <= rightBound) temp[k++] = arr[j++];

            for (int m = 0; m < temp.length; m++) arr[leftPtr + m] = temp[m];

        }
    }
}
