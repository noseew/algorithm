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
        int[] arr = new int[]{4, 5, 2, 0};
        System.out.println(Arrays.toString(sort_01(arr)));
    }

    /**
     */
    public static int[] sort_01(int[] data) {
        int count = 0;
        int countOuter = 0;

        for (int i = 1; i < data.length; i++) {
            int countInner = 0;
            System.out.println(String.format("外部循环: 次数 %s, 结果: %s", countOuter++, Arrays.toString(data)));

            int cindex = i;
            int cdata = data[i];
            for (int j = i - 1; j >= 0; j--) {
                if (cdata < data[j]) {
                    int temp = data[j];
                    data[j] = cdata;
                    data[cindex] = temp;
                    cindex = j;

                    count++;
                    System.out.println(String.format("\t内部循环: 次数 %s, 结果: %s", countInner++, Arrays.toString(data)));
                } else {
                    break;
                }
            }
        }
        System.out.println(String.format("总次数 %s", count));
        return data;
    }


}
