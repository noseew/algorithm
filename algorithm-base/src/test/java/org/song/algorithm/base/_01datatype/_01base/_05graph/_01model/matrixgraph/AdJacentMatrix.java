package org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph;

/**
 * 数组存储
 * 邻接矩阵方式存储
 */
public class AdJacentMatrix<V, E> extends MatrixGraph<V, E> {

    public AdJacentMatrix(int vertex) {
        super(vertex);
    }

    /**
     * 返回元素的下标
     *
     * @param e
     * @return
     */
    protected int locateVertex(V e) {
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
                Edge<E> e = edges[i][j];
                e = e.wight == maxInt ? new Edge(null) : e;
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
    public void addVertex(V o) {

    }

    @Override
    public void addEdge(V from, V to) {

    }

    @Override
    public void addEdge(V from, V to, E wight) {
        int i = locateVertex(from);
        int j = locateVertex(to);
        edges[i][j] = new Edge(wight);

    }

    @Override
    public void removeVertex(V o) {

    }

    @Override
    public void removeEdge(V from, V to) {

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
