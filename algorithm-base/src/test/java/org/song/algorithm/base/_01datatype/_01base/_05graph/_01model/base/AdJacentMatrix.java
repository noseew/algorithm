package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

import org.song.algorithm.base._01datatype._01base._02queue_stack._01model.queue.Queue_Link_01;
import org.song.algorithm.base._01datatype._01base._02queue_stack._01model.stack.Stack_Link_01;

import java.util.function.Predicate;

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
    public void buildEdge(int e1, int e2, int wight) {
        int i = locateVertex(e1);
        int j = locateVertex(e2);
        edges[i][j] = wight;
    }

    @Override
    public void dfs(Predicate<Integer> goon) {
        // 已访问过的顶点标记, 这里表示y
        int[] visited = new int[vertex];


    }

    @Override
    public void bfs(Predicate<Integer> goon) {
        // 已访问过的顶点标记, 这里表示y
        int[] visited = new int[vertex];
        bfs(goon, visited, 0);
    }

    protected void bfs(Predicate<Integer> goon, int[] visited, int i) {
        Stack_Link_01<Integer> q = new Stack_Link_01<>();
        visited[i] = 1;
        q.push(i);
        if (!goon.test(vertexes[i])) {
            // 遍历中断条件
            return;
        }

        stop:
        while (!q.isEmpty()) {
            i = q.pop();
            for (int j = 0; j < vertex; j++) {
                // 邻接节点
                if (isEdge(edges[i][j])
                        // 未被访问的节点, 由于是x轴遍历, 所以每次遍历对应的顶点下标都+1, 所以取j
                        && visited[j] != 1) {
                    if (!goon.test(vertexes[j])) {
                        // 遍历中断条件
                        break stop;
                    }
                    // 标记已访问, 这里取j是看其对应的x轴的顶点下标
                    visited[j] = 1;
                    q.push(j);
                }
            }
        }
    }

    protected boolean isEdge(int e) {
        return e > 0 && e != maxInt;
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
}
