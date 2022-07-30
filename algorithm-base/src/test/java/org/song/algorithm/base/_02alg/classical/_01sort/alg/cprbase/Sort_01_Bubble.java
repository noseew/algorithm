package org.song.algorithm.base._02alg.classical._01sort.alg.cprbase;

import org.junit.Test;
import org.song.algorithm.base._02alg.classical._01sort.AbstractSort;

/**
 * 冒泡排序: 两两比较和交换, 将最小(大)的移动到一边
 * 
 * 复杂度:
 * 最好 O(n)
 * 最差 O(n^2)
 * 平均 O(n^2)
 * 
 * 原地排序: 不依赖外部空间, 直接在原数组上进行排序, 冒牌排序属于原地排序
 *
 * 1. 内层循环, 相邻两个两两对比, 对比出最大的逐渐右移, 一轮之后排在最后
 * 2. 外层循环, 在剩下的数字中执行1
 */
public class Sort_01_Bubble {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new BubbleSort4().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
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
                        exchange(cs, j + 1, j);
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
                        exchange(cs, j + 1, j);
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
                        exchange(cs, j + 1, j);
                        lastSwapIndex = j; // 记录最后交换的位置
                    }
                }
            }
        }
    }

    /*
    将预先排序优化和尾部优化合并
     */
    public static class BubbleSort4 extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            for (int end = cs.length - 1; end > 0; end--) {
                // 记录最后一次交换的索引, 将末尾已经排好序的
                int lastSwapIndex = 1;
                for (int begin = 1; begin <= end; begin++) {
                    if (less(cs[begin], cs[begin - 1])) {
                        exchange(cs, begin, begin - 1);
                        lastSwapIndex = begin; // 记录最后交换的位置
                    }
                }
                end = lastSwapIndex; // 已经排好序的尾部
            }
        }
    }

    /**
     * 左程云
     */
    public static class BubbleSort5 extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            for (int i = cs.length - 1; i > 0; i--) {
                findMax(cs, i);
            }
        }

        public void findMax(Comparable[] a, int n) {
            for (int j = 0; j < n; j++) {
                if (less(a[j + 1], a[j])) {
                    exchange(a, j, j + 1);
                }
            }
        }
    }
}
