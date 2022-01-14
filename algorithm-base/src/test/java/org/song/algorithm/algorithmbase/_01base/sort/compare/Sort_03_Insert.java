package org.song.algorithm.algorithmbase._01base.sort.compare;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01base.sort.AbstractSort;

import java.util.Arrays;

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
        int[] arr = new int[]{1, 4, 5, 2, 0, 3};
        System.out.println(Arrays.toString(sort_01(arr)));
    }

    /*
    等价示例: 接扑克牌, 从一堆乱序的扑克牌中一张一张的揭牌, 并按一定顺序插入手中, 当扑克牌接完后, 手中的扑克牌将全部有序
    
    平均情况下插入排序需要～N2/4次比较以及～N2/4次交换。最坏情况下需要～N2/2次比较和～N2/2次交换，最好情况下需要N-1次比较和0次交换。
     */
    public static class InsertSort extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
            for (int i = 1; i < cs.length; i++) {
                int cindex = i;
                Comparable cdata = cs[i];
                for (int j = i - 1; j >= 0; j--) {
                    if (less(cdata, cs[j])) {
                        exch(cs, cindex, j);
                        cindex = j;
                    } else {
                        break;
                    }
                }
            }

        }
    }

    /**
     *
     */
    public static int[] sort_01(int[] cs) {
        for (int i = 1; i < cs.length; i++) {
            int cindex = i;
            int cdata = cs[i];
            for (int j = i - 1; j >= 0; j--) {
                if (cdata < cs[j]) {
                    int temp = cs[j];
                    cs[j] = cdata;
                    cs[cindex] = temp;
                    cindex = j;
                } else {
                    break;
                }
            }
        }
        return cs;
    }


}
