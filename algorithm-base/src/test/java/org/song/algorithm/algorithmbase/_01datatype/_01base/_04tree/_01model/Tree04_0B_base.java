package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

/*
B树
(平衡树, 包括2叉树和多叉树)
m阶B树
    根节点(1 <= x <= m-1)
    非根节点(floor(m/2) - 1 <= x <= m-1)
m=2, 每个节点最多1个值, 最多2个子节点
m=3, 每个节点最多2个值, 最多3个子节点, 就是23树
m=4, 每个节点最多3个值, 最多4个子节点, 就是234树, 简称24树
m=5, 就是35树

B树特点
1. 平衡, 高度相同
2. 一个节点存储的多个值, 可以用数组也可以用链表
 */
public class Tree04_0B_base<V extends Comparable<V>> {
}
