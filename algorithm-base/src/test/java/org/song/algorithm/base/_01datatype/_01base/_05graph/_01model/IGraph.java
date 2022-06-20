package org.song.algorithm.base._01datatype._01base._05graph._01model;

import java.util.function.Predicate;

/*

图的存储
数组
    邻接矩阵, 
    优点: 简单直观, 图像更好理解
    缺点: 空间占用大, 编写稍微复杂 
链表
    邻接表
    优点: 空间占用小, 实现稍微简单
    缺点: 图像不太好理解
    邻接表存储概念上比较复杂, 这里有简单的实现方式, 具体参见 IListGraph
    
    邻接表
    十字链表, 解决有向图邻接表度计算问题
    邻接多重表, 解决无向图邻接表重复边问题, 有点抽象

    链表存储
    邻接表方式存储
    注意具体的定义方式根据语言等有所区别, 能表达意思即可
    
    十字链表
    解决有向图邻接表/逆邻接表, 计算度不方便的问题
    将邻接表的出度和入度结合起来, 则每个链表节点都表示出度链表的一个节点和入度链表的一个节点, 从而形成十字形状链表
    从顶点出发形成两个链表, 一个是出度链表, 一个是入度链表
 */
public interface IGraph<V, E> {

    void addVertex(V v);

    void addEdge(V from, V to);

    void addEdge(V from, V to, E wight);

    void removeVertex(V v);

    void removeEdge(V from, V to);

    int edgeSize();

    int vertices();

    /**
     * 深度优先遍历
     * 
     * 深度优先遍历秘籍：后被访问的顶点，其邻接点先被访问。
     * 
     * @param goon 是否继续遍历
     */
    void dfs(Predicate<Integer> goon);

    /**
     * 广度优先遍历
     * 
     * 广度优先遍历秘籍：先被访问的顶点，其邻接点先被访问。
     * 
     * @param goon 是否继续遍历
     */
    void bfs(Predicate<Integer> goon);
}
