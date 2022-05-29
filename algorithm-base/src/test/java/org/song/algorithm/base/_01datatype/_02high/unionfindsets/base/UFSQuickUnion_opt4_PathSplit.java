package org.song.algorithm.base._01datatype._02high.unionfindsets.base;

/*
采用 quick union 思路实现

一种优化
路径压缩: 
    针对find的优化
    执行find操作的时候, find路径上的所有节点都指向根节点, 从而降低树的高度
    降低了之后, 路径直接平铺成1层了, 成本比较高, 需要递归调用, 或需要额外的空间
路径分裂: 这里采用此方式
    针对find的优化
    执行find操作的时候, find路径上的所有节点都指向其祖父节点, 从而降低树的高度
    降低了之后, 路径高度变成了原来的一半, 同时变成了两条, 不需要要递归和额外的空间
路径减半:

 */
public class UFSQuickUnion_opt4_PathSplit extends UFSQuickUnion_opt2 {

    
    protected UFSQuickUnion_opt4_PathSplit(int capacity) {
        super(capacity);
    }

    /*
    递归查找父节点同时将当前父节点指向根节点
     */
    @Override
    public Integer findRoot(Integer n) {
        validRange(n);
        while (parents[n] != n) {
            int p = parents[n];
            parents[n] = parents[parents[n]];
            n = p;
        }
        // 返回最终的根节点
        return parents[n];
    }
    
}
