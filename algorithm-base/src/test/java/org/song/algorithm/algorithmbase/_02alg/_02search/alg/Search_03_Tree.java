package org.song.algorithm.algorithmbase._02alg._02search.alg;

import org.junit.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;
import org.song.algorithm.algorithmbase._02alg._01sort.AbstractSort;
import org.song.algorithm.algorithmbase._02alg._01sort.alg.Sort_06_Quick;
import org.song.algorithm.algorithmbase._02alg._02search.AbstractSearch;

import java.util.Comparator;

/*
树查找
也就是采用 链表存储, 二分法查找思路, 实现,
具体根据数的不同效率也会不同, 这里采用AVL数

平均效率 O(logn)
 */
public class Search_03_Tree {

    @Test
    public void test() {

        TreeSearch treeSearch = new TreeSearch();
        for (int i = 0; i < 10; i++) {
            Comparable comparable = treeSearch.get(i);
            System.out.println(comparable);
        }
    }

    public static class TreeSearch extends AbstractSearch {

        private Tree03_AVL_base<Integer> tree;

        public TreeSearch() {
            tree = new Tree03_AVL_base<>(Comparator.comparing(Integer::doubleValue));
            Comparable[] build = AbstractSort.build(0, 10, 10);
            for (int i = 0; i < build.length; i++) {
                tree.push((Integer) build[i]);
            }
        }

        @Override
        public Comparable get(Comparable v) {
            return tree.get((Integer) v);
        }
    }
}
