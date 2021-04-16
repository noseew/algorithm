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
        int count = 0;
        int countOuter = 0;
        for (int i = 0; i < data.length - 1; i++) {
            int countInner = 0;
            System.out.println(String.format("外部循环: 次数 %s, 结果: %s", countOuter++, Arrays.toString(data)));
            for (int j = i + 1; j < data.length; j++) {
                if (data[i] > data[j]) {
                    int temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
                count++;
                System.out.println(String.format("\t内部循环: 次数 %s, 结果: %s", countInner++, Arrays.toString(data)));
            }
        }
        System.out.println(String.format("总次数 %s", count));
        return data;
    }


}
