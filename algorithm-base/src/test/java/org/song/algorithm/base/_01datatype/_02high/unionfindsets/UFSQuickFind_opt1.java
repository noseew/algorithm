package org.song.algorithm.base._01datatype._02high.unionfindsets;

/*
采用 quick find 思路实现

一种优化, union更快一些, 不过本质上还是O(n)的
 */
public class UFSQuickFind_opt1 extends UFSQuickFind {
    
    protected UFSQuickFind_opt1(int capacity) {
        super(capacity);
    }


    /*
    union慢
    不过本质上还是O(n)的
     */
    @Override
    public void union(int n1, int n2) {
        validRange(n1);
        validRange(n2);
        int p1 = findRoot(n1);
        int p2 = findRoot(n2);
        if (p1 == p2) {
            // 属于同一个集合, 不用处理
            return;
        }

        // 向上循环找到跟节点
        while (n1 != parents[n1]) {
            n1 = parents[n1];
            // 每一个父节点都将其父节点指向p2
            parents[n1] = p2;
        }
    }
}
