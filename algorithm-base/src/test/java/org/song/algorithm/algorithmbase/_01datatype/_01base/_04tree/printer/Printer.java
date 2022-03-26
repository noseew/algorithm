package org.song.algorithm.algorithmbase._01datatype._01base._04tree.printer;

import org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model.node.TreeNode;

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
