package org.song.algorithm.algorithmbase._01base.sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01base.sort.AbstractSort;

/**
 * 冒泡排序: 两两比较和交换, 将最小(大)的移动到一边
 *
 * 1. 内层循环, 相邻两个两两对比, 对比出最大的逐渐右移, 一轮之后排在最后
 * 2. 外层循环, 在剩下的数字中执行1
 */
public class Sort_01_Bubble {

    @Test
    public void test() {

    }

    /*
    冒泡排序 基础版本
     */
    public static class BubbleSort1 extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
            for (int i = 0; i < cs.length - 1; i++) {
                for (int j = 0; j < cs.length - i - 1; j++) {
                    if (less(cs[j + 1], cs[j])) {
                        exch(cs, j + 1, j);
                    }
                }
            }
        }
    }

    /*
     冒泡排序 2
     如果一次遍历中, 给定的数据已经排好序, 则无需重复排序比较, 时间复杂度 O(n^2) 降为 O(n)
     注意: 已经拍好序的数组概率比较低, 所以此算法只在特殊场景下有效
     */
    public static class BubbleSort2 extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            boolean change = true;
            for (int i = 0; i < cs.length - 1 && change; i++) {
                change = false;
                for (int j = 0; j < cs.length - i - 1; j++) {
                    if (less(cs[j + 1], cs[j])) {
                        exch(cs, j + 1, j);
                        change = true;
                    }
                }
            }
        }
    }

    /*
    针对结尾数据已经排好序的, 进行优化
     */
    public static class BubbleSort3 extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            // 记录最后一次交换的索引, 将末尾已经排好序的
            int lastSwapIndex = -1;
            for (int i = 0; i < cs.length - 1; i++) {
                int maxJ = lastSwapIndex < 0 ? cs.length - i - 1 : lastSwapIndex;
                for (int j = 0; j < maxJ; j++) {
                    if (less(cs[j + 1], cs[j])) {
                        exch(cs, j + 1, j);
                        lastSwapIndex = j;
                    }
                }
            }
        }
    }
}
