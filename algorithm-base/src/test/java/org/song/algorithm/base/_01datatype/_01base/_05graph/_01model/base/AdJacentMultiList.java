package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

/**
 * 十字链表
 * 解决有无向图邻接表, 每条边存储两遍的问题
 * 
 * 有点抽象下次整
 */
public class AdJacentMultiList extends AdJacentList {

    public AdJacentMultiList(int vertex, int edge) {
        super(vertex, edge);
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
         * 当前顶点和该顶点组成的边
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
