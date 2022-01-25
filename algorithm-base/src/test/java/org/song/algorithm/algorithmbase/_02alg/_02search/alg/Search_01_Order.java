package org.song.algorithm.algorithmbase._02alg._02search.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._01linear_list.list._01model.Array_base_01;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;
import org.song.algorithm.algorithmbase._02alg._02search.AbstractSearch;

/*
顺序查找是最简单的查找方式, 以暴力穷举的方式依次将表中的关键字与待查找关键字比较
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

        private Array_base_01<Comparable> array;

        public OrderSearch() {
            array = new Array_base_01<>(10);
            for (Comparable c : AbstractSort.build(0, 10, 10)) {
                array.add(c);
            }
        }

        @Override
        public Comparable get(Comparable v) {
            for (int i = 0; i < array.length(); i++) {
                if (array.get(i).compareTo(v) == 0) {
                    return array.get(i);
                }
            }
            return null;
        }
    }
}
