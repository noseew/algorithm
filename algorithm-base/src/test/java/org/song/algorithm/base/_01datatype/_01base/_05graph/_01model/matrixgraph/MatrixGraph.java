package org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph;

import org.song.algorithm.base._01datatype._01base._05graph._01model.IGraph;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class MatrixGraph<V, E> implements IGraph<V, E> {
    
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
     * @param maxVertex
     */
    protected MatrixGraph(int maxVertex) {
        this.maxVertex = maxVertex;
        this.maxEdge = maxVertex * maxVertex;
        vertexes = new Vertex[maxVertex];
        // 邻接矩阵就是长宽都等于顶点长度的矩阵
        edges = new Edge[maxVertex][maxVertex];
        // 基本类型/无权图 不需要初始化

        // 带权图初始化成最大值
        for (int i = 0; i < maxVertex; i++) {
            for (int j = 0; j < maxVertex; j++) {
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
