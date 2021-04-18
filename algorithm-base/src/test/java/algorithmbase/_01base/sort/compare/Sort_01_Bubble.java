package algorithmbase._01base.sort.compare;

import algorithmbase._01base.sort.SortUtils;
import org.junit.Test;

import java.util.Arrays;

/**
 * 冒泡排序: 两两比较和交换, 将最小(大)的移动到一边
 *
 * 1. 内层循环, 相邻两个两两对比, 对比出最大的逐渐右移, 一轮之后排在最后
 * 2. 外层循环, 在剩下的数字中执行1
 */
public class Sort_01_Bubble {

    @Test
    public void test() {

//        int[] arr = new int[]{1, 2, 3, 4, 5};
        int[] arr = new int[]{3, 0, 4, 2, 1};

//        System.out.println(Arrays.toString(sort_01(arr)));
//        System.out.println(Arrays.toString(sort_02(arr)));
//        System.out.println(Arrays.toString(sort_03(arr)));

        int[] ints = SortUtils.generateTailOrder(6, 3);
        System.out.println(Arrays.toString(sort_03(ints)));

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
     * <p>
     * 注意: 已经拍好序的数组概率比较低, 所以此算法只在特殊场景下有效
     */
    public static int[] sort_02(int[] data) {
        int count = 0;
        int countOuter = 0;
        // 如果一次遍历中, 给定的数据已经排好序, 则无需重复排序比较, 时间复杂度 O(n^2) 降为 O(n)
        boolean change = true;
        for (int i = 0; i < data.length - 1 && change; i++) {
            int countInner = 0;
            System.out.println(String.format("外部循环: 次数 %s, 结果: %s", countOuter++, Arrays.toString(data)));
            // 越冒泡, 需要对比的数据越少
            change = false;
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
     * 针对结尾数据已经排好序的, 进行优化
     *
     */
    public static int[] sort_03(int[] data) {
        int count = 0;
        int countOuter = 0;

        // 记录最后一次交换的索引, 将末尾已经排好序的
        int lastSwapIndex = -1;
        for (int i = 0; i < data.length - 1; i++) {
            int countInner = 0;
            System.out.println(String.format("外部循环: 次数 %s, 结果: %s", countOuter++, Arrays.toString(data)));

            int maxJ = lastSwapIndex < 0 ? data.length - i - 1 : lastSwapIndex;
            for (int j = 0; j < maxJ; j++) {
                if (data[j] > data[j + 1]) {
                    int iData = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = iData;
                    lastSwapIndex = j;
                }
                count++;
                System.out.println(String.format("\t内部循环: 次数 %s, 结果: %s", countInner++, Arrays.toString(data)));
            }
        }
        System.out.println(String.format("总次数 %s", count));
        return data;
    }

}
