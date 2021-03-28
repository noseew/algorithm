package algorithmbase.sort;

import org.junit.Test;

import java.util.Arrays;

/**
 * 冒泡排序
 */
public class Sort_Bubble {

    @Test
    public void test() {

        int[] arr = new int[]{1, 2, 3, 4, 5};

//        System.out.println(Arrays.toString(sort_01(arr)));
        System.out.println(Arrays.toString(sort_02(arr)));

    }

    /**
     * 冒泡排序
     * 基础版本
     */
    public static int[] sort_01(int[] data) {
        int count = 0;
        int countOuter = 0;
        for (int i = 0; i < data.length - 1; i++) {
            int countInner = 0;
            System.out.println(String.format("外部循环: 次数 %s, 结果: %s", countOuter++, Arrays.toString(data)));
            // 越冒泡, 需要对比的数据越少
            for (int j = 0; j < data.length - i - 1; j++) {
                // 依次和下一个元素进行对比
                if (data[j] > data[j + 1]) {
                    // 交换数据
                    int iData = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = iData;
                }
                count++;
                System.out.println(String.format("\t内部循环: 次数 %s, 结果: %s", countInner++, Arrays.toString(data)));
            }
        }
        System.out.println(String.format("总次数 %s", count));
        return data;
    }

    /**
     * 冒泡排序 2
     * 如果一次遍历中, 给定的数据已经排好序, 则无需重复排序比较, 时间复杂度 O(n^2) 降为 O(n)
     */
    public static int[] sort_02(int[] data) {
        int count = 0;
        int countOuter = 0;
        // 如果一次遍历中, 给定的数据已经排好序, 则无需重复排序比较, 时间复杂度 O(n^2) 降为 O(n)
        boolean change = true;
        for (int i = 0; i < data.length - 1 && change; i++) {
            int countInner = 0;
            change = false;
            System.out.println(String.format("外部循环: 次数 %s, 结果: %s", countOuter++, Arrays.toString(data)));
            // 越冒泡, 需要对比的数据越少
            for (int j = 0; j < data.length - i - 1; j++) {
                // 依次和下一个元素进行对比
                if (data[j] > data[j + 1]) {
                    // 交换数据
                    int iData = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = iData;
                    change = true;
                }
                count++;
                System.out.println(String.format("\t内部循环: 次数 %s, 结果: %s", countInner++, Arrays.toString(data)));
            }
        }
        System.out.println(String.format("总次数 %s", count));
        return data;
    }

    /**
     * 冒泡的变种
     * 时间空间复杂度一致
     */
    public static int[] sort(int[] data) {
        int count = 0;
        int countOuter = 0;
        for (int i = 0; i < data.length; i++) {
            int countInner = 0;
            System.out.println(String.format("外部循环: 次数 %s, 结果: %s", countOuter++, Arrays.toString(data)));
            for (int j = i + 1; j < data.length; j++) {
                if (data[i] > data[j]) {
                    int iData = data[i];
                    data[i] = data[j];
                    data[j] = iData;
                }
                count++;
                System.out.println(String.format("\t内部循环: 次数 %s, 结果: %s", countInner++, Arrays.toString(data)));
            }
        }
        System.out.println(String.format("总次数 %s", count));
        return data;
    }

}
