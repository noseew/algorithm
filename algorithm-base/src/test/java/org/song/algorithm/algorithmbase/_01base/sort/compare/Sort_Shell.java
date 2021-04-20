package org.song.algorithm.algorithmbase._01base.sort.compare;

import org.junit.Test;

import java.util.Arrays;

/**
 * shell排序
 * 属于分组的插入排序, 分组规则是使用二分法,
 * 每分一次得到一组数, 取这些组的首位形成一个新的数据, 而这个数组就是需要进行插入排序的数, 一只分到1为止
 */
public class Sort_Shell {

    @Test
    public void test() {
        int[] arr = new int[]{3, 0, 4, 2, 1};
        System.out.println(Arrays.toString(sort_01(arr)));
    }

    /**
     *
     */
    public static int[] sort_01(int[] data) {
        int i = 0, j = 0, k = 0;
        int key = 0;
        for (int curStep = 4; curStep > 0; curStep = curStep / 2) {
            for (i = 0; i < curStep; i++) {
                for (k = i + curStep; k < data.length; k += curStep) {
                    key = data[k];
                    for (j = k - curStep; j >= i; j -= curStep) {
                        if (data[j] > key) {
                            data[j + curStep] = data[j];
                            data[j] = key;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return data;
    }


    public static int[] sort_insert(int[] data) {

        for (int i = 1; i < data.length; i++) {
            int cindex = i;
            int cdata = data[i];
            for (int j = i - 1; j >= 0; j--) {
                if (cdata < data[j]) {
                    int temp = data[j];
                    data[j] = cdata;
                    data[cindex] = temp;
                    cindex = j;
                } else {
                    break;
                }
            }
        }
        return data;
    }


}
