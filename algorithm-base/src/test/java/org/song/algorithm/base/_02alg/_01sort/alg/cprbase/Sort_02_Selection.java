package org.song.algorithm.base._02alg._01sort.alg.cprbase;

import org.junit.Test;
import org.song.algorithm.base._02alg._01sort.AbstractSort;

/**
 *
 * 选择排序
 * 复杂度:
 * 最好 O(n)
 * 最差 O(n^2)
 * 平均 O(n^2)
 *
 * 原地排序: 不依赖外部空间, 直接在原数组上进行排序, 选择排序属于原地排序
 * 
 * 和冒牌排序相比: 
 * 减少了循环比较的次数, 平均复杂度小于冒牌排序, 但是依然在O(n^2)
 * 
 * 选择排序每一轮外层循环都是在找最大/小值, 然后进行排序, 类似的思路可以使用 堆排序实现
 * 
 */
public class Sort_02_Selection {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new SelectionSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /*
    等价示例: 在一群人中按颜值挑从高到低选美女, 将选出的美女依次排队, 当挑选完成后, 所有美女有序排成一排
    
    选择排序: 在剩下的数组中, 选择最小(大)的依次放入已经排好序的数据中
        1. 内层循环, 选择最小的数字放在最左边, 一轮之后最小的在最左
        2. 外层循环, 在剩下的数字中执行1
    
    选择排序需要大约N2/2次比较和N次交换。
     */
    public static class SelectionSort extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            for (int i = 0; i < cs.length - 1; i++) {
                for (int j = i + 1; j < cs.length; j++) {
                    if (less(cs[j], cs[i])) {
                        exchange(cs, j, i);
                    }
                }
            }
        }
    }

    /**
     * 左程云
     */
    public static class SelectionSort2 extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            for (int i = 0; i < cs.length - 1; i++) {
                int minPos = i;
                for (int j = i + 1; j < cs.length; j++) {
                    minPos = less(cs[j], cs[minPos]) ? j : minPos;
                }
                exchange(cs, i, minPos);
            }
        }
    }

    public static class SelectionSort3 extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            for (int end = cs.length - 1; end > 0; end--) {
                int max = 0;
                for (int begin = 1; begin <= end; begin++) {
                    if (less(max, begin)) {
                        max = begin;
                    }
                }
                exchange(cs, max, end);
            }
        }
    }

}
