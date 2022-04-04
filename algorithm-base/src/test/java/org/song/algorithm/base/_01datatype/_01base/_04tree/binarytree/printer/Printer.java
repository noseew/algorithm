package org.song.algorithm.base._01datatype._01base._04tree.binarytree.printer;

import org.song.algorithm.base._01datatype._01base._04tree.binarytree._01model.TreeNode;

public abstract class Printer {
    /**
     * 二叉树的基本信息
     */
    protected TreeNode tree;
    
    protected boolean printColor;

    public Printer(TreeNode tree, boolean printColor) {
        this.tree = tree;
        this.printColor = printColor;
    }

    /**
     * 生成打印的字符串
     */
    public abstract String printString();

    /**
     * 打印后换行
     */
    public void println() {
        print();
        System.out.println();
    }

    /**
     * 打印
     */
    public void print() {
        System.out.print(printString());
    }
}
