package org.song.algorithm.algorithmbase._02alg._02search.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;
import org.song.algorithm.algorithmbase._02alg._02search.AbstractSearch;

/*
顺序查找是最简单的查找方式, 以暴力穷举的方式依次将表中的关键字与待查找关键字比较

平均效率 O(n)
 */
public class Search_01_Order {

    @Test
    public void test() {

        OrderSearch orderSearch = new OrderSearch();
        for (int i = 0; i < 10; i++) {
            Comparable comparable = orderSearch.get(i);
            System.out.println(comparable);
        }
    }

    public static class OrderSearch extends AbstractSearch {

        private Comparable[] array;

        public OrderSearch() {
            array = new Comparable[10];
            Comparable[] build = AbstractSort.build(0, 10, 10);
            for (int i = 0; i < build.length; i++) {
                array[i] = build[i];
            }
        }

        @Override
        public Comparable get(Comparable v) {
            for (int i = 0; i < array.length; i++) {
                if (array[i].compareTo(v) == 0) {
                    return array[i];
                }
            }
            return null;
        }
    }
}
