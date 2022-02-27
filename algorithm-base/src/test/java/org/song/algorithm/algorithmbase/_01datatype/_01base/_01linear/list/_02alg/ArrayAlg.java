package org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._02alg;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear.list._01model.ArrayBase01;

public class ArrayAlg {

    private static ArrayBase01<Integer> initData(int max) {
        ArrayBase01<Integer> list = new ArrayBase01<>(5);
        for (int i = 0; i < max; i++) {
            list.add(i);
        }
        return list;
    }

    /**
     * 合并两个有序数组
     */
    @Test
    public void test_merge() {
        ArrayBase01<Integer> array1 = initData(3);
        ArrayBase01<Integer> array2 = initData(5);

        int len = array1.length() + array2.length();

        ArrayBase01<Integer> list = new ArrayBase01<>(len);

        for (int i = 0, i1 = 0, i2 = 0; i < len; i++) {
            if (i1 > array1.length() - 1) {
                list.add(array2.get(i2++));
            } else if (i2 > array2.length() - 1) {
                list.add(array1.get(i1++));
            } else if (array1.get(i1) <= array2.get(i2)) {
                list.add(array1.get(i1++));
            } else {
                list.add(array2.get(i2++));
            }
        }
        System.out.println(list.toString());
    }
}
