package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

/*
红黑树
23树的等价表示
3. 23树变成2叉树, 方式有很多, 这里采用变成红黑树的方式
        v1,v2
        / | \
       c1 c2 c3
       将左节点分出, 转变为
         v1 == v2
        /  \    \
       c1  c2   c3
       最终转变为
           v2
          // \
         v1   c3
        /  \
       c1  c2
    上图中, // 双线链指向的子节点表示为红色节点, 其他节点表示为黑色节点

等价定义
    红链接均为左链接
    没有任何一个结点同时和两条红链接相连
    该树是完美黑色平衡的, 即任意空链接到根结点的路径上的黑链接数量相同


 */
public class Tree05_RB_base {
}
