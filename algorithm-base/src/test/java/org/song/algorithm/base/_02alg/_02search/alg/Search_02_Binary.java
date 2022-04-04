package org.song.algorithm.base._02alg._02search.alg;

import org.junit.Test;
import org.song.algorithm.base._02alg._01sort.AbstractSort;
import org.song.algorithm.base._02alg._01sort.alg.Sort_06_Quick;
import org.song.algorithm.base._02alg._02search.AbstractSearch;

/*
二分查找
1. 二分查找要求数据是有序
2. 且线性定位是O(1)的, 比如数组. 如果数据不是数组的, 则建议使用树形查找或者使用跳表搜索, 详细查看二叉树/跳表

平均效率 O(logn)
 */
public class Search_02_Binary {

    @Test
    public void test() {

        BinarySearch binarySearch = new BinarySearch();
        for (int i = 0; i < 10; i++) {
            Comparable comparable = binarySearch.get(i);
            System.out.println(comparable);
        }
    }

    public static class BinarySearch extends AbstractSearch {

        private Comparable[] array;

        public BinarySearch() {
            array = new Comparable[10];
            Comparable[] build = AbstractSort.build(0, 10, 10);
            for (int i = 0; i < build.length; i++) {
                array[i] = build[i];
            }
            // 排序
            new Sort_06_Quick.QuickSort().sort(array);
        }

        @Override
        public Comparable get(Comparable v) {
            if (array == null || array.length == 0) {
                return null;
            }

            int left = 0, right = array.length - 1, ans = array.length;
            while (left <= right) {
                int middle = ((right - left) >> 1) + left;
                if (AbstractSort.lessEq(v, array[middle])) {
                    ans = middle;
                    // -1 正好越过临界值, 下一次循环可以直接取ans
                    right = middle - 1;
                } else {
                    // +1 正好越过临界值
                    left = middle + 1;
                }
            }
            return ans;
        }
    }
}
