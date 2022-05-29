package org.song.algorithm.base._01datatype._02high.unionfindsets;

/*
采用 quick union 思路实现

一种优化
路径压缩: 针对find的优化
执行find操作的时候, find路径上的所有节点都指向根节点, 从而降低树的高度

 */
public class UFSQuickUnion_opt3_PathCompress extends UFSQuickUnion_opt2 {

    
    protected UFSQuickUnion_opt3_PathCompress(int capacity) {
        super(capacity);
    }

    /*
    递归查找父节点同时将当前父节点指向根节点
     */
    @Override
    public int findRoot(int n) {
        validRange(n);
        // 递归查找父节点同时将当前父节点指向根节点
        while (parents[n] != n) {
            parents[n] = findRoot(parents[n]);
        }
        // 返回最终的根节点
        return parents[n];
    }
    
}
