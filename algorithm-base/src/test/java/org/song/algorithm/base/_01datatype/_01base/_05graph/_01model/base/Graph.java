package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

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
public abstract class Graph {
    
    // 顶点数
    protected int vertex;
    // 边数
    protected int edge;

    // 极大值, 表示权值的正无穷, 如果是带权图的话
    protected static final int maxInt = 32767;

    /**
     * 初始化
     * 
     * @param vertex
     * @param edge
     */
    public Graph(int vertex, int edge) {
        this.vertex = vertex;
        this.edge = edge;
    }

    /**
     * 构建顶点表
     * 
     * @param vertexes
     */
    public abstract void buildVertexes(int[] vertexes);

    /**
     * 构建顶点和边的关系
     * 通过 e1 到 e2 加上 权值 wight, 构建邻接矩阵
     * 
     * @param e1
     * @param e2
     * @param wight
     */
    public abstract void buildEdge(int e1, int e2, int wight);

    /**
     * 深度优先遍历
     * 
     * @param goon 是否继续遍历
     */
    public abstract void dfs(Predicate<Integer> goon);

    /**
     * 广度优先遍历
     * 
     * 广度优先遍历秘籍：先被访问的顶点，其邻接点先被访问。
     * 
     * @param goon 是否继续遍历
     */
    public abstract void bfs(Predicate<Integer> goon);
}
