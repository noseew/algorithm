package org.song.algorithm.base._02alg._01sort.alg.cprbase;

import org.junit.Test;
import org.song.algorithm.base._02alg._01sort.AbstractSort;

/**
 * 插入排序: 和选择排序类似,
 * 选择排序是在剩下的数据中, 按照顺找出数据, 并依次放好, 找出的过程就是排序的过程, 码放的过程直接并排码放即可
 * 插入排序是依次将剩下的数据取出, 并码放好, 取出的过程逐个取出即可, 码放的过程是排序的过程
 * <p>
 * 1. 一次选择一个数, 插入到最左边已经排好序的数组中,
 */
public class Sort_03_Insert {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new InsertSort1().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /*
    等价示例: 接扑克牌, 从一堆乱序的扑克牌中一张一张的揭牌, 并按一定顺序插入手中, 当扑克牌接完后, 手中的扑克牌将全部有序
    
    平均情况下插入排序需要～N2/4次比较以及～N2/4次交换。最坏情况下需要～N2/2次比较和～N2/2次交换，最好情况下需要N-1次比较和0次交换。
    
    注意: 在插入的过程中, 即使采用而分法快速定位, 依然需要移动O(n)个元素, 因此插入排序效率依然是 O(n^2)
     */
    public static class InsertSort1 extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
            // 从1开始, 遍历每一个元素
            for (int i = 1; i < cs.length; i++) {
                int newI = i; // 标志元素
                for (int j = newI - 1; j >= 0; j--) {
                    // 外层循环的元素, 和前一个对比, 如果外层循环更小, 则交换
                    if (less(cs[newI], cs[j])) {
                        exchange(cs, newI, j);
                        // 此时对比角色发生变化, 标志元素要和其前一个元素作对比, 而不是原始的j
                        newI = j;
                    } else {
                        break;
                    }
                }
            }

        }
    }

    /**
     * 左程云
     */
    public static class InsertSort2 extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
            for (int i = 1; i < cs.length; i++) {
                for (int j = i; j > 0 && less(cs[j], cs[j - 1]); j--) {
                    exchange(cs, j, j - 1);
                }
            }
        }
    }

}
