package org.song.algorithm.base._02alg._02search.alg;

import org.junit.Test;
import org.song.algorithm.base._01datatype._02high.skiplist._02redis.AbstractSkipList;
import org.song.algorithm.base._01datatype._02high.skiplist._02redis.SkipListLinked03Rank;
import org.song.algorithm.base._02alg._01sort.AbstractSort;
import org.song.algorithm.base._02alg._02search.AbstractSearch;

/*
跳表 查找

平均效率 O(logn)
 */
public class Search_05_skipList {

    @Test
    public void test() {

        SkipListSearch skipListSearch = new SkipListSearch();
        for (int i = 0; i < 10; i++) {
            Comparable comparable = skipListSearch.get(i);
            System.out.println(comparable);
        }
    }

    public static class SkipListSearch extends AbstractSearch {

        private SkipListLinked03Rank<Integer, Integer> skipList;

        public SkipListSearch() {
            skipList = new SkipListLinked03Rank<>();
            Comparable[] build = AbstractSort.build(0, 10, 10);
            for (int i = 0; i < build.length; i++) {
                skipList.put((Integer) build[i], (Integer) build[i], (Integer) build[i]);
            }
        }

        @Override
        public Comparable get(Comparable v) {
            AbstractSkipList.Node<Integer, Integer> node = skipList.get((Integer) v);
            if (node != null) {
                return node.getK();
            }
            return null;
        }
    }
}
