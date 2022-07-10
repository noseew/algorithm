package org.song.algorithm.base._01datatype._01base._05graph._01model.matrixgraph;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 数组存储
 * 邻接矩阵方式存储
 */
public class AdjacencyMatrix<V, E> extends MatrixGraph<V, E> {

    public AdjacencyMatrix(int vertex) {
        super(vertex);
    }

    @Override
    public void addVertex(V value) {
        for (int i = 0; i < vertexSize; i++) {
            if (vertexes[i] != null && Objects.equals(vertexes[i].value, value)) {
                return;
            }
        }
        if (vertexSize == maxVertex) {
            throw new IllegalArgumentException("顶点容量超限");
        }
        vertexes[vertexSize++] = new Vertex(value);
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, maxWight);
    }

    @Override
    public void addEdge(V from, V to, E wight) {
        addVertex(from);
        addVertex(to);
        int i = locateVertex(from);
        int j = locateVertex(to);
        if (edges[i][j] != null) {
            edges[i][j] = new Edge(wight);
            return;
        }
        if (edgeSize == maxEdge) {
            throw new IllegalArgumentException("边容量超限");
        }
        
        if (edges[i][j] == null) {
            edgeSize++;
        }
        edges[i][j] = new Edge(wight);
    }

    @Override
    public void removeVertex(V value) {
        int index = locateVertex(value);
        if (index < 0) {
            return;
        }
        vertexes[index] = null;

        for (int i = 0; i < maxVertex; i++) {
            if (edges[i][index] != null) {
                edges[i][index] = null;
                edgeSize--;
            }
            if (edges[index][i] != null) {
                edges[index][i] = null;
                edgeSize--;
            }
        }
        vertexSize--;
    }

    @Override
    public void removeEdge(V from, V to) {
        int i = locateVertex(from);
        int j = locateVertex(to);
        if (edges[i][j] != null) {
            edges[i][j] = null;
            edgeSize--;
        }
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int vertices() {
        return vertexSize;
    }

    @Override
    public void dfs(V begin, Predicate<V> goon) {
        
    }

    @Override
    public void bfs(V begin, Predicate<V> goon) {

    }

    @Override
    public List<V> topologySort() {
        return null;
    }

    @Override
    public Set<EdgeInfo<V, E>> mst() {
        return null;
    }

    @Override
    public Map<V, E> shortestPathWight(V begin) {
        return null;
    }

    @Override
    public Map<V, PathInfo<V, E>> shortestPath(V begin) {
        return null;
    }

    @Override
    public Map<V, Map<V, PathInfo<V, E>>> shortestPath() {
        return null;
    }

    /**
     * 返回元素的下标
     *
     * @param value
     * @return
     */
    private int locateVertex(V value) {
        for (int i = 0; i < vertexes.length; i++) {
            if (vertexes[i] != null && Objects.equals(vertexes[i].value, value)) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("顶点:\r\n").append("[");
        for (int i = 0; i < vertexes.length; i++) {
            sb.append(i).append(":").append(vertexes[i] != null ? vertexes[i].value : " ");
            if (i != vertexes.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]").append("\r\n").append("邻接矩阵:").append("\r\n");
        for (int i = 0; i < edges.length; i++) {
            sb.append("[");
            for (int j = 0; j < edges[i].length; j++) {
                Edge<E> e = edges[i][j];
                e = e == null || e.wight == maxWight ? new Edge(null) : e;
                sb.append(e);
                if (j != edges[i].length - 1) {
                    sb.append(",\t");
                }
            }
            sb.append("]").append("\r\n");
        }
        return sb.toString();
    }
}
