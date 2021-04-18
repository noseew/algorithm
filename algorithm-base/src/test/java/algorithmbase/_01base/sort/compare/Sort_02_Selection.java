package algorithmbase._01base.sort.compare;

import org.junit.Test;

import java.util.Arrays;

/**
 * 选择排序: 在剩下的数组中, 选择最小(大)的依次放入已经排好序的数据中
 *
 * 1. 内层循环, 选择最小的数字放在最左边, 一轮之后最小的在最左
 * 2. 外层循环, 在剩下的数字中执行1
 */
public class Sort_02_Selection {

    @Test
    public void test() {
        int[] arr = new int[]{3, 0, 4, 2, 1};
        System.out.println(Arrays.toString(sort_01(arr)));
    }

    /**
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
