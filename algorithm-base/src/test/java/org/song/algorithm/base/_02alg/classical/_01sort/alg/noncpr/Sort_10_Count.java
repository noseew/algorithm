package org.song.algorithm.base._02alg.classical._01sort.alg.noncpr;

import org.junit.Test;
import org.song.algorithm.base._02alg.classical._01sort.AbstractSort;

/*
计数排序
1. 找一个能覆盖左右值范围的数组, 例如, 待排序中的数据去重后总共10个, 那么数准备的组的长度就是10
2. 待排序的数据按照大小总能和数组的下标对应上, 
    比如按照值对应, 情况1
        计数数组: [0,1,2,3,4]
        数据的值1: [1,2,3,4,5]
        数据的值2: [91,92,93,94,95]
    比如按照差值, 情况2
        计数数组: [0,1,2,3,4]
        数据的值1: [1,3,5,7,9]
    其他情况, 情况3
        计数数组: [0,1,2,3,4]       计数数组, 表示该数据在排序数组中出现的次数, 下标表示数据, 值表示次数
        相差数组: [0,4,5,5,20]      相差数组, 表示该数据和下一个数据的差
        数据的值: [11,15,25,30,50]  待排序的数组
3. 得到第二部数据之后
    顺序遍历计数数组, 由于数组本身是有序的, 所以顺序遍历出来的就是有序的, 
    然后根据该数据出现的次数, 反向生成该数据
     
注意: 具体的实现可能不同, 总体思路如上, 得到每个数的计数信息
 */
public class Sort_10_Count {

    @Test
    public void test() {
        int[] build = AbstractSort.buildInt(10, 20, 20);

        new CountSort2().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    @Test
    public void test3() {
        int[] build = AbstractSort.buildInt(10, 20, 20);

        new CountSort3().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

//    public static class CountSort {
//
//        public int[] sort(int[] arr) {
//
//            // 计数数组
//            int[] count = new int[arr.length];
//
//            // 计数
//            for (int i = 0; i < arr.length; i++) {
//                count[arr[i]]++;
//            }
//
//            System.out.println(Arrays.toString(count));
//
////		for(int i=0, j=0; i<count.length; i++) {
////			
////			while(count[i]-- > 0) result[j++] = i;
////		}
//
//            for (int i = 1; i < count.length; i++) {
//                count[i] = count[i] + count[i - 1];
//            }
//
//            System.out.println(Arrays.toString(count));
//
//            int[] result = new int[arr.length];
//            for (int i = arr.length - 1; i >= 0; i--) {
//                result[--count[arr[i]]] = arr[i];
//            }
//
//            return result;
//        }
//    }

    public static class CountSort2 {

        protected void sort(int[] array) {
//         找出最值
            int max = array[0];
            int min = array[0];
            for (int i = 0; i < array.length; i++) {
                if (array[i] < min) min = array[i];
                if (array[i] > max) max = array[i];
            }
//         开辟内存空间，存储次数
            int[] counts = new int[max - min + 1];
//         统计每个整数出现的次数
            for (int i = 0; i < array.length; i++) {
                counts[array[i] - min]++;
            }
//         累加次数
            for (int i = 1; i < counts.length; i++) {
                counts[i] += counts[i - 1];
            }
//         从后往前遍历元素，将它放到有序数组中的合适位置
            int[] newArray = new int[array.length];
            for (int i = array.length - 1; i >= 0; i--) {
                newArray[--counts[array[i] - min]] = array[i];
            }
//         将有序数组赋值到array
            for (int i = 0; i < newArray.length; i++) {
                array[i] = newArray[i];
            }
        }
    }

    public static class CountSort3 {


        public void sort(int[] array) {
//         找出最大值
            int max = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
//         开辟内存空间
            int[] counts = new int[max + 1];
//         统计每个整数出现的次数
            for (int i = 0; i < array.length; i++) {
                counts[array[i]]++;
            }
//         根据整数出现的次数，对整数进行排序
            int index = 0;
            for (int i = 0; i < counts.length; i++) {
                while (counts[i]-- > 0) {
                    array[index++] = i;
                }
            }
        }
    }


}
