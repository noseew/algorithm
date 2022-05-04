package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

/**
 * 十字链表
 * 解决有向图邻接表/逆邻接表, 计算度不方便的问题
 * <p>
 * 将邻接表的出度和入度结合起来, 则每个链表节点都表示出度链表的一个节点和入度链表的一个节点, 从而形成十字形状链表
 * 从顶点出发形成两个链表, 一个是出度链表, 一个是入度链表
 */
public class OrthogonalList extends AdJacentList {
    /**
     * 顶点表
     */
    protected Edge[] vertexes;

    public OrthogonalList(int vertex, int edge) {
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

    protected Edge lastOutDegree(Edge node) {
        while (node != null && node.nextOutDegree != null) {
            node = node.nextOutDegree;
        }
        return node;
    }

    protected Edge lastInDegree(Edge node) {
        while (node != null && node.nextInDegree != null) {
            node = node.nextInDegree;
        }
        return node;
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
        
        // 设置出度
        Edge outDegreeEdge = new Edge()
                .setDegree(vertexes[j])
                .setWight(wight);
        lastOutDegree(vertexes[i]).nextOutDegree = outDegreeEdge;

        // 设置入度
        Edge inDegreeEdge = new Edge()
                .setNextInDegree(vertexes[j])
                .setWight(wight);
        lastInDegree(vertexes[i]).nextInDegree = inDegreeEdge;
        
    }

    /**
     * 图的边
     * 由两个顶点和边的权重组成
     * 第一个顶点, 由于是单向链表这里不记录, 下一个顶点 用字段 degree 记录
     * 额外信息, 下一条边
     * 这里记录下一条出度边和下一条入度边
     * 
     */
    public static class Edge {
        int ele; // 顶点表用, 表示顶点值
        /**
         * 边用, 出度边所邻接的顶点指针, 连接表中默认出度, 如果表示入度那就是逆邻接表
         * 也可以只记录顶点的下标
         */
        Edge degree; // 
        int wight; // 边用, 表示边的权值
        Edge nextOutDegree; // 下一条出度边
        Edge nextInDegree; // 下一条入度边

        public Edge() {

        }

        public Edge setNextOutDegree(Edge nextOutDegree) {
            this.nextOutDegree = nextOutDegree;
            return this;
        }

        public Edge setNextInDegree(Edge nextInDegree) {
            this.nextInDegree = nextInDegree;
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
