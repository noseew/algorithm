package algorithmbase._01base.sort.compare;

import org.junit.Test;

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

    /**
     *
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
