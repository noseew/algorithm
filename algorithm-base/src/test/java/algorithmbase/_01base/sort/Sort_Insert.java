package algorithmbase._01base.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 插入排序
 * 1. 一次选择一个数, 插入到最左边已经排好序的数组中,
 */
public class Sort_Insert {

    @Test
    public void test() {
        int[] arr = new int[]{5, 4, 3, 2, 1};
        System.out.println(Arrays.toString(sort_01(arr)));
    }

    /**
     * 基础版本
     */
    public static int[] sort_01(int[] data) {
        int sortedMaxIndex = 0;
        for (int i = 1; i < data.length; i++) {
            for (int j = 0; j < i; j++) {


                if (data[i] > data[j]) {
                    int temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }
        return data;
    }


}
