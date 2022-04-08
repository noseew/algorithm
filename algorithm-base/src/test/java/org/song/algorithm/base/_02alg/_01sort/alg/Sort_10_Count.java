package org.song.algorithm.base._02alg._01sort.alg;

import org.junit.Test;

import java.util.Arrays;

/*
计数排序
 */
public class Sort_10_Count {

    @Test
    public void test() {
//        Comparable[] build = AbstractSort.build(10);
//
//        new CountSort().sort(build);
//
//        AbstractSort.toString(build);
//
//        assert AbstractSort.isSorted(build);
    }

    /*
     */
    public static class CountSort {

        public static int[] sort(int[] arr) {
            int[] result = new int[arr.length];

            int[] count = new int[10];

            for (int i = 0; i < arr.length; i++) {
                count[arr[i]]++;
            }

            System.out.println(Arrays.toString(count));

//		for(int i=0, j=0; i<count.length; i++) {
//			
//			while(count[i]-- > 0) result[j++] = i;
//		}

            for (int i = 1; i < count.length; i++) {
                count[i] = count[i] + count[i - 1];
            }

            System.out.println(Arrays.toString(count));

            for (int i = arr.length - 1; i >= 0; i--) {
                result[--count[arr[i]]] = arr[i];
            }

            return result;
        }
    }

    public static class CountSort2 {
        protected int[] array;

        protected void sort() {
//         找出最值
            int max = array[0];
            int min = array[0];
            for (int i = 0; i < array.length; i++) {
                if (array[i] < min) {
                    min = array[i];
                }
                if (array[i] > max) {
                    max = array[i];
                }
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

        private void sort0() {
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
