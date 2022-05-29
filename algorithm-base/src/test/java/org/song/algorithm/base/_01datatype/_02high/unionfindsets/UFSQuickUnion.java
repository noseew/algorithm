package org.song.algorithm.base._01datatype._02high.unionfindsets;

/*
采用 quick union 思路实现

quick find 的思路是 union快, quick慢

这里实现两个都是 O(logn)

 */
public class UFSQuickUnion extends UnionFindSets {
    
    protected UFSQuickUnion(int capacity) {
        super(capacity);
    }

    /*
    向上循环找到跟节点
     */
    @Override
    public int findRoot(int n) {
        validRange(n);
        // 向上循环找到跟节点
        while (n != parents[n]) {
            n = parents[n];
        }
        return n;
    }

    /*
    这里规定, 让n1的根节点的设置成n2的根节点
    这里操作的都是根节点
    
    特点: 数的高度可能会很高
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
        // 将n1的根节点设置成n2的根节点
        parents[p1] = p2;
    }
}
