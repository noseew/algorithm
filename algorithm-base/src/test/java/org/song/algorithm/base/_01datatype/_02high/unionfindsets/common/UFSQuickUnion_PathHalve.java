package org.song.algorithm.base._01datatype._02high.unionfindsets.common;

/*
采用 quick union 思路实现, UFSQuickUnion_opt4_PathHalve 的泛型版本

一种优化
路径减半: 这里采用此方式, 和路径分裂类似
    针对find的优化
    执行find操作的时候, find路径上的每隔一个节点都指向其祖父节点, 从而降低树的高度
    降低了之后, 路径高度变成了原来的一半, 同时变成了一条主分支, 多个小的分支, 不需要要递归和额外的空间

 */
public class UFSQuickUnion_PathHalve<T> extends AbstractUFS<T> {

    protected UFSQuickUnion_PathHalve(int capacity) {
        super(capacity);
    }

    /*
    递归查找父节点同时将当前父节点指向根节点
     */
    @Override
    public T findRoot(T n) {
        validRange(n);
        while (parentOf(n) != n) {
            setParent(n, parentOf(parentOf(n)));
            n = parentOf(n);
        }
        // 返回最终的根节点
        return parentOf(n);
    }

    @Override
    public void union(T n1, T n2) {
        validRange(n1);
        validRange(n2);
        T p1 = findRoot(n1);
        T p2 = findRoot(n2);
        if (p1 == p2) {
            // 属于同一个集合, 不用处理
            return;
        }

        //  哪边节点数少, 改哪边
        if (rankOf(p1) <rankOf(p2)) {
            setParent(p1, p2);
            // 注意: rank不需要增加, 因为: 高度低的加到高度高的树上, 总高度是不变的
        } else if(rankOf(p2) <rankOf(p1)) {
            setParent(p2, p1);
            // 注意: rank不需要增加, 因为: 高度低的加到高度高的树上, 总高度是不变的
        }else{
            setParent(p1, p2);
            // 注意: 只有此时rank高度才需要增加, 两个高度相等的数相加, 被增加的那个高度+1
            addRank(p2, 1);
        }
    }
    
}
