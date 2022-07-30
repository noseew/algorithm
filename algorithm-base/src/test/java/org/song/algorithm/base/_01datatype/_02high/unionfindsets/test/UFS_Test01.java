package org.song.algorithm.base._01datatype._02high.unionfindsets.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.base._01datatype._02high.unionfindsets.UFS;
import org.song.algorithm.base._01datatype._02high.unionfindsets.common.UFSQuickUnion_PathHalve;
import org.song.algorithm.base._02alg.classical._01sort.AbstractSort;

import java.util.List;

public class UFS_Test01 {
    
    
    @Test
    public void test01() {
        UFS<Integer> ufs = new UFSQuickUnion_PathHalve<>(10);
        for (int i = 0; i < 10; i++) {
            ufs.add(i);
        }
        System.out.println(ufs);
        
        List<Integer> integers = AbstractSort.buildList(10);
        ufs = new UFSQuickUnion_PathHalve<>(integers);
        System.out.println(ufs);
    }
    
    @Test
    public void test02_auto() {
        int size = 100;
        int segment = 7;
        UFS<Integer> ufs = new UFSQuickUnion_PathHalve<>(size);
        for (int i = 0; i < size; i++) {
            ufs.add(i);
            assert ufs.findRoot(i) == i;
            if (i != 0) {
                assert !ufs.isSame(i, i - 1);
            }
        }
        System.out.println(ufs);

        for (int i = 0; i < size; i++) {
            if (i != 0) {
                ufs.union(i, i - 1);
            }
        }
        System.out.println(ufs);
        
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                assert ufs.isSame(i, i - 1);
            }
        }
    }
    
    @Test
    public void test03_auto() {
        int size = 100;
        int segment = 7;
        
        UFS<Integer> ufs = new UFSQuickUnion_PathHalve<>(size);
        for (int i = 0; i < size; i++) {
            ufs.add(i);
            assert ufs.findRoot(i) == i;
            if (i != 0) {
                assert !ufs.isSame(i, i - 1);
            }
        }
        System.out.println(ufs);

        for (int i = 0; i < size; i++) {
            if (i != 0 && i % segment != 0) {
                ufs.union(i, i - 1);
            }
        }
        System.out.println(ufs);

        for (int i = 0; i < size; i++) {
            if (i != 0) {
                if (i % segment != 0) {
                    assert ufs.isSame(i, i - 1);
                } else {
                    assert !ufs.isSame(i, i - 1);
                }
            }
        }
    }
}
