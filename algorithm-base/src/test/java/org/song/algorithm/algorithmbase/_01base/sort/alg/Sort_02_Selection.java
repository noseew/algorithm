package org.song.algorithm.algorithmbase._01base.sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01base.sort.AbstractSort;

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

}
