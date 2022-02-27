package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.test;

import org.junit.jupiter.api.Test;
import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.Tree04B01;

public class B3_test {
    private Tree04B01<String> tree = new Tree04B01<String>(3);    // degree为3

    /**
     * 测试插入方法Case1
     */
    @Test
    public void test1() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.frontIterator(tree.root);
        System.out.println(tree.number);

    }

    /**
     * 测试插入方法Case2
     */
    @Test
    public void test2() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.insert("J");
        tree.insert("V");
        tree.insert("M");
        tree.insert("N");
        tree.insert("O");
        tree.frontIterator(tree.root);
        System.out.println(tree.number);

    }

    /**
     * 测试搜索方法Case1
     * <p>
     * 查找B tree中存在的结点
     */
    @Test
    public void test3() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.insert("J");
        tree.insert("V");
        tree.insert("M");
        tree.insert("N");
        tree.insert("O");
        tree.frontIterator(tree.root);
        System.out.println(tree.number);
        System.out.println(tree.search(tree.root, "M"));

    }

    /**
     * 测试搜索方法Case2
     * <p>
     * 查找B tree中不存在的结点
     */
    @Test
    public void test4() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.frontIterator(tree.root);
        System.out.println(tree.number);
        System.out.println(tree.search(tree.root, "F"));

    }

    /**
     * 测试删除方法Case1
     */
    @Test
    public void test5() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.frontIterator(tree.root);
        tree.remove("A");
        System.out.println("--------------RemoveResult--------------");
        tree.frontIterator(tree.root);

    }

    /**
     * 测试删除方法Case2
     */
    @Test
    public void test6() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.insert("J");

        tree.frontIterator(tree.root);
        tree.remove("D");
        System.out.println("--------------RemoveResult--------------");
        tree.frontIterator(tree.root);

    }

    /**
     * 测试删除方法Case3
     */
    @Test
    public void test7() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.insert("J");
        tree.frontIterator(tree.root);
        tree.remove("C");
        System.out.println("--------------RemoveResult--------------");
        tree.frontIterator(tree.root);

    }

    /**
     * 测试删除方法Case4
     */
    @Test
    public void test8() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.insert("J");
        tree.insert("V");
        tree.insert("M");
        tree.insert("N");
        tree.insert("H");
        tree.insert("B");
        tree.frontIterator(tree.root);
        tree.remove("J");
        System.out.println("--------------RemoveResult--------------");
        tree.frontIterator(tree.root);

    }

    /**
     * 测试删除方法Case5
     */
    @Test
    public void test9() {
        tree.insert("A");
        tree.insert("C");
        tree.insert("D");
        tree.insert("E");
        tree.insert("G");
        tree.insert("J");
        tree.insert("V");
        tree.insert("M");
        tree.insert("N");
        tree.frontIterator(tree.root);
        tree.remove("J");
        System.out.println("--------------RemoveResult--------------");
        tree.frontIterator(tree.root);

    }

    /**
     * 测试删除方法Case6
     */
    @Test
    public void test10() {
        tree.insert("C");
        tree.insert("G");
        tree.insert("P");
        tree.insert("T");
        tree.insert("X");
        tree.insert("M");

        tree.insert("A");
        tree.insert("B");
        tree.insert("D");
        tree.insert("E");
        tree.insert("J");
        tree.insert("V");

        tree.insert("L");
        tree.insert("N");
        tree.insert("O");
        tree.insert("Q");
        tree.insert("R");
        tree.insert("S");

        tree.insert("U");
        tree.insert("V");
        tree.insert("Y");
        tree.insert("Z");

        tree.frontIterator(tree.root);
        tree.remove("D");
        System.out.println("--------------RemoveResult--------------");
        tree.frontIterator(tree.root);

    }
}
