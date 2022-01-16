package org.song.algorithm.algorithmbase._01base.sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01base.sort.AbstractSort;

import java.util.Random;

public class Sort_Quick {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new QuickSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /*
    长度为N的数组排序所需的时间和NlgN成正比。
    
    快速排序和归并排序是互补的：归并排序将数组分成两个子数组分别排序，并将有序的子数组归并以将整个数组排序；
    而快速排序将数组排序的方式则是当两个子数组都有序时整个数组也就自然有序了。
        归并排序是将两个有序的数组合并成一个有序的数组(两个数组原本各自有序, 相互无序, 合并就是让其相互有序), 
            将各自有序相互无序的两个数组合并: [4,5,6],[1,2,3] => [1,2,3,4,5,6]
        快速排序是将两个数组排序(两个数组原本各自无序, 相互有序, 排序就是让其各自有序)
            将相互有序各自无序的两个数组排序: [2,1,3],[5,6,4] => [1,2,3,4,5,6]
    
    
     */
    public static class QuickSort extends AbstractSort {

        private static final int SHUFFLE_THRESHOLD = 5;
        private static Random r;

        @Override
        public void sort(Comparable[] cs) {
            // 重新洗牌数组, 消除 因输入数据 切分数据的选组 对 快速排序 的影响
            shuffle(cs);
            sort(cs, 0, cs.length - 1);
        }

        private void sort(Comparable[] cs, int low, int hi) {
            if (hi <= low) {
                return;
            }
            // 切分
            int j = partition(cs, low, hi);
            // 将左半部分排序
            sort(cs, low, j - 1);
            // 将右半部分排序
            sort(cs, j + 1, hi);
        }

        private int partition(Comparable[] cs, int low, int hi) {
            /*
            将数组切分成 [low, i-1], [i], [i+1, hi]
             */
            int i = low, j = hi + 1; // 左右扫描指针
            Comparable v = cs[low]; // 切分的元素
            while (true) {
                // 扫描左右, 检查扫描是否结束并交换元素
                while (less(cs[++i], v)) {
                    if (i == hi) {
                        break;
                    }
                }
                while (less(v, cs[--j])) {
                    if (j == low) {
                        break;
                    }
                }
                if (i >= j) {
                    break;
                }
                exchange(cs, i, j);
            }
            // 将 v = cs[j] 放入正确的位置
            exchange(cs, low, j);
            // [lo, j-1] <= [j] <= [i+1, hi]
            return j;
        }

        /**
         * 数组重新洗牌, 算法改变自JDK Collections
         *
         * @param cs
         */
        private void shuffle(Comparable[] cs) {
            if (r == null) {
                r = new Random();
            }
            // Shuffle array
            for (int i = cs.length; i > 1; i--) {
                exchange(cs, i - 1, r.nextInt(i));
            }
        }
    }


}
