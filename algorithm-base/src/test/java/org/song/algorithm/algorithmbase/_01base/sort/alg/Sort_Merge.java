package org.song.algorithm.algorithmbase._01base.sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01base.sort.AbstractSort;

public class Sort_Merge {

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
    
    public static abstract class AbstractMergeSort extends AbstractSort {

        protected Comparable[] temp;

        protected void merge(Comparable[] cs, int low, int mid, int hi) {
            for (int k = low; k <= hi; k++) {
                // 复制到临时数组, [low~hi]
                temp[k] = cs[k];
            }
            /*
            两个指针, 分别指的是两个有序数组的起始 index
             */
            int i = low, j = mid + 1;
            for (int k = low; k <= hi; k++) {
                // 归并到 cs
                if (i > mid) {
                    // 如果i到头了, 第一个数组用完了, 则直接使用第2个数组
                    cs[k] = temp[j++];
                } else if (j > hi) {
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
}
