package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree.BTreePrinter2;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree01_B_base;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree03_AVL_base;

public class B_test {

    @Test
    public void test_01_start() {
        Tree01_B_base<Integer> tree = new Tree01_B_base<>();
        for (int i = 0; i < 20; i++) {
            tree.push(i);
        }
        BTreePrinter2.print(tree.root);
    }
}
