package org.song.algorithm.base._01datatype._02high.unionfindsets.base;

import java.util.ArrayList;
import java.util.List;

/*
采用 quick union 思路实现

一种优化
路径压缩: 这里采用此方式
    针对find的优化
    执行find操作的时候, find路径上的所有节点都指向根节点, 从而降低树的高度
    降低了之后, 路径直接平铺成1层了, 成本比较高, 需要递归调用, 或需要额外的空间
路径分裂:
路径减半:

 */
public class UFSQuickUnion_opt3_PathCompress extends UFSQuickUnion_opt2 {

    
    protected UFSQuickUnion_opt3_PathCompress(int capacity) {
        super(capacity);
    }

    /*
    递归查找父节点同时将当前父节点指向根节点
     */
    @Override
    public Integer findRoot(Integer n) {
        validRange(n);
        // 递归查找父节点同时将当前父节点指向根节点
        while (parents[n] != n) {
            parents[n] = findRoot(parents[n]);
        }
        // 返回最终的根节点
        return parents[n];
    }

    /**
     * 采用循环的方式实现
     * 
     * @param n
     * @return
     */
    public Integer findRoot2(Integer n) {
        validRange(n);
        // 向上循环找到跟节点
        List<Integer> list = new ArrayList<>(ranks[n]);
        while (n != parents[n]) {
            list.add(n);
            n = parents[n];
        }
        for (Integer num : list) {
            parents[num] = n;
        }
        return n;
    }
    
}
