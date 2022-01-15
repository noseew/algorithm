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

    /*
    归并排序最吸引人的性质是它能够保证将任意长度为N的数组排序所需时间和 NlogN 成正比
    分治思想

    两两有序数组合并, 最小的长度为1, 也就是两个 item合并,
    流程示例: 数组下标 1,2,3,4,5,6,7,8
        [1],[2] 进行合并, [3],[4] 进行合并,...
        [1,2],[3,4] 进行合并
        [1,2,3,4],[5,6,7,8] 进行合并
     */

    public static class MergeSort extends AbstractSort {

        private Comparable[] temp;

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

        private void merge(Comparable[] cs, int low, int mid, int hi) {
            int i = low, j = mid + 1;
            for (int k = low; k <= hi; k++) {
                // 复制到临时数组, [low~hi]
                temp[k] = cs[k];
            }
            for (int k = low; k <= hi; k++) {
                // 归并到 cs
                if (i > mid) {
                    cs[k] = temp[j++];
                } else if (j > hi) {
                    cs[k] = temp[i++];
                } else if (less(temp[j], temp[i])) {
                    cs[k] = temp[j++];
                } else {
                    cs[k] = temp[i++];
                }
            }
        }
    }
}
