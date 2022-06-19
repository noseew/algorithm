package org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph;

import java.util.function.Predicate;

/*
图的存储
数组
    邻接矩阵, 简单直观
链表
    邻接表
    十字链表, 解决有向图邻接表度计算问题
    邻接多重表, 解决无向图邻接表重复边问题, 有点抽象


构建图的顺序
1. 规定定点数和边数: {@link Graph(int vertex, int edge)}
2. 构建顶点表: {@link buildVertexes(int[] vertexes)}
3. 构建顶点和边的关系: {@link build(int e1, int e2, int wight)}
 */
public interface IMatrixGraph<V, E> {

    void addVertex(V v);

    void addEdge(V from, V to);

    void addEdge(V from, V to, E wight);

    void removeVertex(V v);

    void removeEdge(V from, V to);

    int edgeSize();

    int vertices();

//    /**
//     * 深度优先遍历
//     * 
//     * 深度优先遍历秘籍：后被访问的顶点，其邻接点先被访问。
//     * 
//     * @param goon 是否继续遍历
//     */
//    public abstract void dfs(Predicate<Integer> goon);
//
//    /**
//     * 广度优先遍历
//     * 
//     * 广度优先遍历秘籍：先被访问的顶点，其邻接点先被访问。
//     * 
//     * @param goon 是否继续遍历
//     */
//    public abstract void bfs(Predicate<Integer> goon);
}
