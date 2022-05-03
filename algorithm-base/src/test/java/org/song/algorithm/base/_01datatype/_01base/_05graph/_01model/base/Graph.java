package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

public abstract class Graph {
    
    // 顶点数
    protected int vertex;
    // 边数
    protected int edge;

    // 极大值, 表示权值的正无穷, 如果是带权图的话
    protected static final int maxInt = 32767;

    public Graph(int vertex, int edge) {
        this.vertex = vertex;
        this.edge = edge;
    }
    
}
