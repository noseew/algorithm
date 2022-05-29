package org.song.algorithm.base._01datatype._02high.unionfindsets;

import org.springframework.util.Assert;

import java.util.Arrays;

/*
并查集
UnionFindSets/DisjointSet

这里的集合并不是指固定的数据结构set, 而是一组数据的集合, 他可以是多种数据结构

数据结构需求分区
假设有n个节点,这些节点之间有的有连接有的没有连接
设计一个数据结构,能够快速执行如下两个操作
1. 查询：查询两个节点之间是否有连接(直接连接或者间接连接)
2. 连接：连接两个节点

如果使用 数组,链表,平衡二叉树,集合,
查询的连接两个操作都是O(n)的

并查集；专注于解决上述两个问题,查询和连接两个操作复杂度能达到O(α(n))n<5, 等价于O(n)
并：连接合并,
查：查找

实现思路
1. quickFind,查快,并慢, 查O(1),合并O(n)
2. quickUnion(常用),查慢,并快,查O(logn)可优化到O(α(n))n<5, 等价于O(n),并O(logn)可优化到O(α(n))n<5, 等价于O(n)




 */
public abstract class UnionFindSets {
    /*
    使用树状数组存储数据
    数组的下标表示元素节点
    数组的元素表示该元素的父节点
     */
    protected int[] parents;
    protected int capacity;

    protected UnionFindSets(int capacity) {
        validRange(capacity);
        this.capacity = capacity;
        parents = new int[capacity];
        for (int i = 0; i < parents.length; i++) {
            // 目的是, 数组中每个数据和自己都形成独立的集合
            parents[i] = i;
        }
    }
    
    /**
     * 查找一个节点所属的集合(根节点)
     *
     * @param n
     * @return
     */
    public abstract int findRoot(int n);

    /**
     * 合并两个节点所属的集合
     *
     * @param n1
     * @param n2
     */
    public abstract void union(int n1, int n2);

    /**
     * 判断两个节点是否在同一个集合中
     *
     * @param n1
     * @param n2
     * @return
     */
    public boolean isSame(int n1, int n2) {
        validRange(n1);
        validRange(n2);
        return findRoot(n1) == findRoot(n2);
    }

    protected void validRange(int n) {
        Assert.isTrue(n >= 0 && n < parents.length, "数组下标越界");
    }

}
