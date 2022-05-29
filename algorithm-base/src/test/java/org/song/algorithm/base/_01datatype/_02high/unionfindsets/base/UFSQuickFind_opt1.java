package org.song.algorithm.base._01datatype._02high.unionfindsets.base;

/*
采用 quick find 思路实现

这种思路实现不了
一种优化, union更快一些, 不过本质上还是O(n)的
 */
@Deprecated
public class UFSQuickFind_opt1 extends UFSQuickFind {
    
    protected UFSQuickFind_opt1(int capacity) {
        super(capacity);
    }


    /*
    union慢
    不过本质上还是O(n)的, 因为从当前n1节点向上, 并不能枚举到集合中所有的元素, 
    1. 因为你只有一条线路, 2. 你的直接父节点就是根节点
     */
    @Override
    public void union(Integer n1, Integer n2) {
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
