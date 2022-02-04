package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

/*
234树, 4阶B树
每个节点最多有3个v, 每个节点最多有4个子节点


等价红黑树
具体不做测试
 */
/*
234树
1. 平衡状态下, 一个节点最多可以保存3个v, 每个节点最多可以有4个子节点c, 其他和二叉树一样
    他们可以由数组方式存储, 提高访问效率, [v1, v2, v3] [c1, c2, c3, c4]
    2个v和3个c示例如下, 
        v1,v2,v3
       /  |  |  \
      c1  c2 c3  c4
   按照顺序排列
        c1 < v1 < c2 < v2 < c3 < v3 < c4
2. 平衡状态下, 左右高度相同, 在极端情况下, 假设root, 所有左子节点都有2个v和3个c, 所有右子节点都是满二叉树
    那么: 
    左子树, 每层节点数=4^h, 每层v数量=3*4^h
    右子树, 每层节点数=2^h, 每层v数量=2^h
    左子树每层v数量-右子树每层v数量=
        h=0, v多 4 - 1 = 3
        h=1, v多 14 - 2 = 14
        h=2, v多 64 - 4 = 60
    由于高度相同, 所以查找效率相同, 从而达到平衡的目的
3. 23树变成2叉树, 方式有很多, 这里采用变成红黑树的方式
        v1,v2,v3
       /  |  |  \
      c1  c2 c3  c4
       将左右节点分出, 转变为
         v1 == v2 == v3
        /  \        /  \
       c1  c2      c3  c4
       最终转变为
            v2
          //  \\
       v1(红)  c3(红)
        /  \    /  \
       c1  c2  c3  c4
    上图中, // 双线链指向的子节点表示为红色节点, 其他节点表示为黑色节点

下面的代码实现来自于网络, 未测试
 */
public class Tree04_234_base<V extends Comparable<V>> {
}
