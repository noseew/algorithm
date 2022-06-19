package org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph;

/**
 * 数组存储
 * 邻接矩阵方式存储
 */
public class AdJacentMatrix extends MatrixGraph {
    /**
     * 顶点表
     */
    protected int[] vertexes;
    /**
     * 邻接矩阵表
     */
    protected int[][] edges;

    public AdJacentMatrix(int vertex) {
        super(vertex);
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
    public void buildEdge(int e1, int e2, int wight) {
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

    public String toMatrixString() {
        StringBuilder sb = new StringBuilder();
        sb.append("顶点:\r\n").append("[");
        for (int i = 0; i < vertexes.length; i++) {
            sb.append(vertexes[i]);
            if (i != vertexes.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]").append("\r\n").append("邻接矩阵:").append("\r\n");
        for (int i = 0; i < edges.length; i++) {
            sb.append("[");
            for (int j = 0; j < edges[i].length; j++) {
                int e = edges[i][j];
                e = e == maxInt ? 0 : e;
                sb.append(e);
                if (j != edges[i].length - 1) {
                    sb.append(",");
                }
            }
            sb.append("]").append("\r\n");
        }
        return sb.toString();
    }

    @Override
    public void addVertex(Object o) {

    }

    @Override
    public void addEdge(Object from, Object to) {

    }

    @Override
    public void addEdge(Object from, Object to, Object wight) {

    }

    @Override
    public void removeVertex(Object o) {

    }

    @Override
    public void removeEdge(Object from, Object to) {

    }

    @Override
    public int edgeSize() {
        return 0;
    }

    @Override
    public int vertices() {
        return 0;
    }
}
