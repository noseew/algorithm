package org.song.algorithm.algorithmbase._02alg._01sort.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;

/**
 * 基数排序
 */
public class Sort_Radix {

    @Test
    public void test() {
        Comparable[] build = AbstractSort.build(10);

        new RadixSort().sort(build);

        AbstractSort.toString(build);

        assert AbstractSort.isSorted(build);
    }

    /*
     */
    public static class RadixSort extends AbstractSort {

        @Override
        public void sort(Comparable[] cs) {
        }
    }


}
