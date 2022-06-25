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
public interface IGraph<V, E extends Comparable<E>> {
    /**
     * 添加一个顶点
     * 
     * @param v
     */
    void addVertex(V v);

    /**
     * 添加一条边
     * 
     * @param from
     * @param to
     */
    void addEdge(V from, V to);

    /**
     * 添加一条边, 加权重
     * 
     * @param from
     * @param to
     * @param wight
     */
    void addEdge(V from, V to, E wight);

    /**
     * 删除一个顶点
     * 
     * @param v
     */
    void removeVertex(V v);

    /**
     * 删除一条边
     * 
     * @param from
     * @param to
     */
    void removeEdge(V from, V to);

    /**
     * 边的数量
     * 
     * @return
     */
    int edgeSize();

    /**
     * 顶点数量
     * 
     * @return
     */
    int vertices();

    /**
     * 深度优先遍历
     * 
     * 深度优先遍历秘籍：后被访问的顶点，其邻接点先被访问。
     * 
     * @param goon 是否继续遍历
     * @param begin 开始顶点
     */
    void dfs(V begin, Predicate<V> goon);

    /*
    广度优先搜索, 
    应用: 树中的层序遍历
    如何确定层呢?
        假设层数从0开始, 
        则顶点处在第0层, 
        顶点需要1步达到的点, 处在第1层;
        顶点需要2步到达的点, 处在第2层;
        以此类推, 图中思路也是这样
     */
    /**
     * 广度优先遍历
     * 
     * 广度优先遍历秘籍：先被访问的顶点，其邻接点先被访问。
     * 
     * @param goon 是否继续遍历
     * @param begin 开始顶点
     */
    void bfs(V begin, Predicate<V> goon);
}
