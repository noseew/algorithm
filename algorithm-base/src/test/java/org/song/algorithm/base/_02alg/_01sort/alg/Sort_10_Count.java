package org.song.algorithm.base._02alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.base._02alg._01sort.AbstractSort;

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


}
