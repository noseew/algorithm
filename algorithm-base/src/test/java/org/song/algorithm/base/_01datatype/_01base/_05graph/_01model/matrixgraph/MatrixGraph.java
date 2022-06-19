package org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph;

import java.util.Objects;

/*
图的存储
数组
    邻接矩阵, 简单直观
链表
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
    
    邻接表存储概念上比较复杂, 这里有简单的实现方式, 具体参见 IListGraph

构建图的顺序
1. 规定定点数和边数: {@link Graph(int vertex, int edge)}
2. 构建顶点表: {@link buildVertexes(int[] vertexes)}
3. 构建顶点和边的关系: {@link build(int e1, int e2, int wight)}
 */
public abstract class MatrixGraph<V, E> implements IMatrixGraph<V, E> {
    
    // 最多顶点数
    protected int maxVertex;
    // 最多边数
    protected int maxEdge;
    // 顶点数
    protected int vertexSize;
    // 边数
    protected int edgeSize;

    // 极大值, 表示权值的正无穷, 如果是带权图的话
    protected E maxWight = null;
    /**
     * 顶点表
     */
    protected Vertex<V>[] vertexes;
    /**
     * 邻接矩阵表
     */
    protected Edge<E>[][] edges;

    /**
     * 初始化
     * 
     * @param vertexSize
     */
    public MatrixGraph(int vertexSize) {
        this.maxVertex = vertexSize;
        this.maxEdge = vertexSize * vertexSize;
        vertexes = new Vertex[vertexSize];
        // 邻接矩阵就是长宽都等于顶点长度的矩阵
        edges = new Edge[vertexSize][vertexSize];
        // 基本类型/无权图 不需要初始化

        // 带权图初始化成最大值
        for (int i = 0; i < vertexSize; i++) {
            for (int j = 0; j < vertexSize; j++) {
                edges[i][j] = new Edge(maxWight);
            }
        }
    }

    /**
     * 顶点Node
     *
     * @param <V> 表示顶点存储的值
     */
    class Vertex<V> {
        
        V value; // 顶点的值

        Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vertex)) return false;
            Vertex<?> vertex = (Vertex<?>) o;
            return Objects.equals(value, vertex.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * 边Node
     *
     * @param <E> 边权重
     */
    class Edge<E> {
        
        E wight; // 边的权重

        public Edge(E wight) {
            this.wight = wight;
        }

        @Override
        public String toString() {
            return String.valueOf(wight);
        }
    }
}
