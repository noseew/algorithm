package org.song.algorithm.algorithmbase._02alg._02search.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._02high.hashmap._01model.HashMap_base_03;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;
import org.song.algorithm.algorithmbase._02alg._02search.AbstractSearch;

/*
hash 查找

平均效率 O(1)
 */
public class Search_04_Hash {

    @Test
    public void test() {

        HashSearch hashSearch = new HashSearch();
        for (int i = 0; i < 10; i++) {
            Comparable comparable = hashSearch.get(i);
            System.out.println(comparable);
        }
    }

    public static class HashSearch extends AbstractSearch {

        private HashMap_base_03<Integer, Integer> map;

        public HashSearch() {
            map = new HashMap_base_03<>();
            Comparable[] build = AbstractSort.build(0, 10, 10);
            for (int i = 0; i < build.length; i++) {
                map.put((Integer) build[i], (Integer) build[i]);
            }
        }

        @Override
        public Comparable get(Comparable v) {
            return map.get((Integer) v);
        }
    }
}
