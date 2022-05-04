package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

/**
 * 数组存储
 * 邻接矩阵方式存储
 */
public class AdJacentMatrix extends Graph {
    /**
     * 顶点表
     */
    protected int[] vertexes;
    /**
     * 邻接矩阵表
     */
    protected int[][] edges;
    
    public AdJacentMatrix(int vertex, int edge) {
        super(vertex, edge);
        vertexes = new int[vertex];
        // 邻接矩阵就是长宽都等于顶点长度的矩阵
        edges = new int[vertex][vertex];
        // 基本类型/无权图 不需要初始化
        
        // 带权图初始化成最大值
        for (int i = 0; i < vertex; i++) {
            for (int j = 0; j < vertex; j++) {
                edges[i][j] = maxInt;
            }
        }
    }

    /**
     * 添加顶点元素
     * 
     * @param vertexes
     */
    public void buildVertexes(int[] vertexes) {
        for (int i = 0; i < this.vertexes.length; i++) {
            this.vertexes[i] = vertexes[i];
        }
    }

    /**
     * 构建连接矩阵
     * 通过 e1 到 e2 加上 权值 wight, 构建邻接矩阵
     * 
     * @param e1
     * @param e2
     * @param wight
     */
    public void build(int e1, int e2, int wight) {
        int i = locateVertex(e1);
        int j = locateVertex(e2);
        edges[i][j] = wight;
    }

    /**
     * 返回元素的下标
     * 
     * @param e
     * @return
     */
    protected int locateVertex(int e) {
        for (int i = 0; i < vertexes.length; i++) {
            if (vertexes[i] == e) {
                return i;
            }
        }
        return -1;
    }
}
