package org.song.algorithm.base._01datatype._01base._05graph._01model.base;

/**
 * 链表存储
 * 邻接表方式存储
 * 注意具体的定义方式根据语言等有所区别, 能表达意思即可
 */
public class AdJacentList extends Graph {
    /**
     * 顶点表
     */
    protected Node[] vertexes;

    public AdJacentList(int vertex, int edge) {
        super(vertex, edge);
        vertexes = new Node[vertex];
//        for (int i = 0; i < vertex; i++) {
//            vertexes[i] = new Node();
//        }
    }

    /**
     * 添加顶点元素
     *
     * @param vertexes
     */
    public void buildVertexes(int[] vertexes) {
        for (int i = 0; i < this.vertexes.length; i++) {
            this.vertexes[i] = new Node().setEle(vertexes[i]);
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
        last(vertexes[i]).next = new Node().setEdge(j).setWight(wight);
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

    protected Node last(Node node) {
        while (node != null && node.next != null) {
            node = node.next;
        }
        return node;
    }

    public static class Node {
        int ele; // 顶点表用, 表示顶点值
        int edge; // 边用, 表示对应的顶点下标, 也可以记录顶点的指针
        int wight; // 边用, 表示边的权值
        Node next;

        public Node() {

        }

        public Node setEdge(int edge) {
            this.edge = edge;
            return this;
        }

        public Node setEle(int ele) {
            this.ele = ele;
            return this;
        }

        public Node setWight(int wight) {
            this.wight = wight;
            return this;
        }
    }
}
