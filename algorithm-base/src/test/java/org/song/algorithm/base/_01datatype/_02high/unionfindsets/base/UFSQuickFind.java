package org.song.algorithm.base._01datatype._02high.unionfindsets.base;

/*
采用 quick find 思路实现

quick find 的思路是 find快 O(1), union慢O(n)

find依赖于union的实现
 */
public class UFSQuickFind extends UnionFindSets {
    
    protected UFSQuickFind(int capacity) {
        super(capacity);
    }

    /*
    find快
    由于union后,所有节点的直接父节点就是根节点, 所以直接返回父节点即可
     */
    @Override
    public Integer findRoot(Integer n) {
        validRange(n);
        return parents[n];
    }

    /*
    union慢
    这里规定, 将n1以及n1的所属集合的所有节点的父节点设置为n2
    如此修改之后, 则: n1中所有节点的直接父节点都是n2, 不存在多层节点的情况(历史n2节点有层级关系呢?)
    
    特点: 树的高度永远是2
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
        for (int i = 0; i < parents.length; i++) {
            // 遍历数组, p1所有子节点包括p1自己(n1所在集合的所有节点), 的根节点都设置成p2
            // 如果n1历史父节点有层级关系呢?
            if (parents[i] == p1) {
                parents[i] = p2;
            }
        }
    }
}
