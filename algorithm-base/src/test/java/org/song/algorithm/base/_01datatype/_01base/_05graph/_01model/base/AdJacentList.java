package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

import java.util.function.Predicate;

/**
 * 链表存储
 * 邻接表方式存储
 * 注意具体的定义方式根据语言等有所区别, 能表达意思即可
 */
public class AdJacentList extends Graph {
    /**
     * 顶点表
     */
    protected Edge[] vertexes;

    public AdJacentList(int vertex, int edge) {
        super(vertex, edge);
        vertexes = new Edge[vertex];
    }

    /**
     * 添加顶点元素
     *
     * @param vertexes
     */
    public void buildVertexes(int[] vertexes) {
        for (int i = 0; i < this.vertexes.length; i++) {
            this.vertexes[i] = new Edge().setEle(vertexes[i]);
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
        lastDegree(vertexes[i]).degree = new Edge().setDegree(vertexes[j]).setWight(wight);
    }

    @Override
    public void dfs(Predicate<Integer> goon) {
        
    }

    @Override
    public void bfs(Predicate<Integer> goon) {

    }

    /**
     * 返回元素的下标
     *
     * @param e
     * @return
     */
    protected int locateVertex(int e) {
        for (int i = 0; i < vertexes.length; i++) {
            if (vertexes[i].ele == e) {
                return i;
            }
        }
        return -1;
    }

    protected Edge lastDegree(Edge edge) {
        while (edge != null && edge.next != null) {
            edge = edge.next;
        }
        return edge;
    }

    /**
     * 图的边
     * 由两个顶点和边的权重组成
     * 第一个顶点, 由于是单向链表这里不记录, 下一个顶点 用字段 degree 记录
     * 额外信息, 下一条边
     */
    public static class Edge {
        int ele; // 顶点表用, 表示顶点值
        /**
         * 边用, 出度边所邻接的顶点指针, 连接表中默认出度, 如果表示入度那就是逆邻接表
         * 也可以只记录顶点的下标
         */
        Edge degree; // 
        int wight; // 边用, 表示边的权值
        Edge next; // 下一条边

        public Edge() {

        }

        public Edge setNext(Edge next) {
            this.next = next;
            return this;
        }

        public Edge setDegree(Edge degree) {
            this.degree = degree;
            return this;
        }

        public Edge setEle(int ele) {
            this.ele = ele;
            return this;
        }

        public Edge setWight(int wight) {
            this.wight = wight;
            return this;
        }
    }
}
