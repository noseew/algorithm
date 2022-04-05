package org.song.algorithm.base._01datatype._01base._04tree.trie;

import org.junit.jupiter.api.Test;

/**
 *
 */
public class Trie02Test {
    @Test
    public void test01() {
        Trie02 dt = new Trie02();

        dt.add("interest");
        dt.add("interesting");
        dt.add("interested");
        dt.add("inside");
        dt.add("insert");
        dt.add("apple");
        dt.add("inter");
        dt.add("interesting");

        dt.print();

        boolean isFind = dt.find("inside");
        System.out.println("find inside : " + isFind);

        int count = dt.count("inter");
        System.out.println("count prefix inter : " + count);
    }
}
