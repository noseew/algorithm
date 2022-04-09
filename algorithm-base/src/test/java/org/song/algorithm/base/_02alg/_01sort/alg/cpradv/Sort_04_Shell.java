package org.song.algorithm.base._02alg._01sort.alg.cpradv;

import org.junit.Test;
import org.song.algorithm.base._02alg._01sort.AbstractSort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * shell排序
 * 属于分组的插入排序, 分组规则是使用二分法,
 * 每分一次得到一组数, 取这些组的首位形成一个新的数据, 而这个数组就是需要进行插入排序的数, 一只分到1为止
 * <p>
 * 插入排序的优化
 */
public class Sort_04_Shell {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new ShellSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    @Test
    public void test02() {
        Comparable[] build = AbstractSort.build(10);

        new ShellSort2().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    @Test
    public void test03() {
        Comparable[] build = AbstractSort.build(10);

        new ShellSort3().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /*
    基于插入排序的快速的排序算法. 对于大规模乱序数组插入排序很慢, 因为它只会交换相邻的元素, 因此元素只能一点一点地从数组的一端移动到另一端. 
    例如, 如果主键最小的元素正好在数组的尽头, 要将它挪到正确的位置就需要N-1次移动. 
    希尔排序为了加快速度简单地改进了插入排序, 交换不相邻的元素以对数组的局部进行排序, 并最终用插入排序将局部有序的数组排序. 

    希尔排序的思想是使数组中任意间隔为h的元素都是有序的. 这样的数组被称为h有序数组. 换句话说, 一个h有序数组就是h个互相独立的有序数组编织在一起组成的一个数组
    
    希尔排序比插入排序和选择排序要快得多, 并且数组越大, 优势越大. 
    已知在最坏的情况下算法2.3的比较次数和N3/2成正比. 有意思的是, 由插入排序到希尔排序, 一个小小的改变就突破了平方级别的运行时间的屏障. 这正是许多算法设计问题想要达到的目标. 
     */

    public static class ShellSort extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            int n = cs.length;
            int h = 1;
            // h 的选择, 这里采用此
            while (h < n / 3) {
                // 1,4,13,40,121,364,1093...
                h = 3 * h + 1;
            }
            while (h >= 1) {
                // 将数组变为h有序
                for (int i = h; i < n; i++) {
                    // 将 [j] 插入到 [i-h], [i-2*h], [i-3*h]...
                    for (int j = i; j >= h && less(cs[j], cs[j - h]); j -= h) {
                        exchange(cs, j, j - h);
                    }
                }
                h = h / 3;
            }
        }
    }

    /**
     * 左程云
     */
    public static class ShellSort2 extends AbstractSort {
        @Override
        public void sort(Comparable[] cs) {
            int h = 1;
            while (h <= cs.length / 3) {
                h = h * 3 + 1;
            }

            for (int gap = h; gap > 0; gap = (gap - 1) / 3) {

                for (int i = gap; i < cs.length; i++) {
                    for (int j = i; j > gap - 1; j -= gap) {
                        if (less(cs[j], cs[j - gap])) {
                            exchange(cs, j, j - gap);
                        }
                    }
                }
            }
        }
    }

    public static class ShellSort3 extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
//        根据元素数量算出步长序列
            List<Integer> stepSequence = sedgewickStepSequence(cs);
//        按步长序列划分进行排序
            for (Integer step : stepSequence) {
                sort(cs, step); // 按step进行排序
            }
        }

        /**
         * 分成step列进行排序
         */
        private void sort(Comparable[] cs, int step) {
            col:
//        第几列, column的简称
            for (int col = 0; col < step; col++) {
//            插入排序
                for (int begin = col + step; begin < cs.length; begin += step) {
//                col、col + step、col + 2 * step、col + 3 * step
                    int cur = begin;
                    while (cur > col && less(cur, cur - step)) {
                        exchange(cs, cur, cur - step);
                        cur -= step;
                    }
                }
            }
        }

        /**
         * 希尔本人提出的步长序列
         */
        public List<Integer> shellStpSequence(Comparable[] cs) {
            List<Integer> stepSequence = new ArrayList<>();
            int step = cs.length;
            while ((step >>= 1) > 0) {
                stepSequence.add(step);
            }
            return stepSequence;
        }

        /**
         * 目前效率最高的步长序列
         */
        private List<Integer> sedgewickStepSequence(Comparable[] cs) {
            List<Integer> stepSequence = new LinkedList<>();
            int k = 0, step = 0;
            while (true) {
                if (k % 2 == 0) {
                    int pow = (int) Math.pow(2, k >> 1);
                    step = 1 + 9 * (pow * pow - pow);
                } else {
                    int pow1 = (int) Math.pow(2, (k - 1) >> 1);
                    int pow2 = (int) Math.pow(2, (k + 1) >> 1);
                    step = 1 + 8 * pow1 * pow2 - 6 * pow2;
                }
                if (step >= cs.length) break;
                stepSequence.add(0, step);
                k++;
            }
            return stepSequence;
        }
    }

}
