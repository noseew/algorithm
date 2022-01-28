package org.song.algorithm.algorithmbase._01datatype._01base._04tree._01model;

import java.util.Comparator;

/*
红黑树
这里实现是 等价234树

红黑树定义
    1. 每个节点是红色或黑色的. 
    2. 根节点是黑色的. 
    3. 每个叶子节点是黑色的. 
    4. 如果一个节点为红色, 则其孩子节点必为黑色. 
    5. 从任一节点到其后代叶子的路径上, 均包含相同数目的黑节点. 
    
总结:
    1. 是红或黑, 根是黑, 叶子为空或黑, 红子必为黑, 黑高相等
    2. 新节点为红, 然后根据情况旋转和变色

 */
public class Tree05_RB234_base<V extends Comparable<V>> extends Tree05_RB23_base<V> {

    public Tree05_RB234_base(Comparator<V> comparator) {
        super(comparator);
    }

}
