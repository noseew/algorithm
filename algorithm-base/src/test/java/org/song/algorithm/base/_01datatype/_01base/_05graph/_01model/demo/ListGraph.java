package org.song.algorithm.base._01datatype._01base._05graph._01model.demo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 采用邻接表实现的图
 * 注意不是严格意义上标准的邻接表实现, 这里是线上使用了一些更方便的实现写法;
 * 算法本身就这样, 理论上的实现只是一个模板, 不同语言不同功能的实现会有自己的调整
 *
 * @param <V>
 * @param <E>
 */
public class ListGraph<V, E> implements IGraph<V, E> {
    /**
     * 顶点的集合
     * key=顶点的值
     * val=顶点Node
     */
    private Map<V, Vertex<V, E>> vertices = new HashMap<>();
    /**
     * 边的集合
     * 元素=边的Node
     */
    private Set<Edge<V, E>> edges = new HashSet<>();

    /**
     * 添加一个顶点
     * 由于不用考虑顶点和边的关系, 所以直接添加到顶点集合就行
     * 
     * @param v
     */
    @Override
    public void addVertex(V v) {
        if (vertices.containsKey(v)) {
            return;
        }
        vertices.put(v, new Vertex<>(v));
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    /**
     * 添加一条边
     * 描述一条边最主要两个元素, 就是from顶点和to顶点
     * 
     * @param from
     * @param to
     * @param wight
     */
    @Override
    public void addEdge(V from, V to, E wight) {
        // 先找到连个顶点, 如果顶点不存在, 则创建
        
        Vertex<V, E> fromVertex = vertices.get(from);
        if (fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }
        Vertex<V, E> toVertex = vertices.get(to);
        if (toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }

        // 构建这条边
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        edge.wight = wight;

        // 更新这条边的信息(权值)
        
        if (fromVertex.outEdges.remove(edge)) {
            toVertex.inEdges.remove(edge);
            edges.remove(edge);
        }

        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeVertex(V v) {
    }

    @Override
    public void removeEdge(V from, V to) {

    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public int vertices() {
        return vertices.size();
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("顶点:");
        String vertexSet = vertices.keySet().stream().map(Object::toString).collect(Collectors.joining(","));
        sb.append("[").append(vertexSet).append("]\r\n");
        sb.append("边:\r\n");
        edges.forEach(e -> sb.append(e).append("\r\n"));

        return sb.toString();
    }

    /**
     * 顶点Node
     *
     * @param <V> 表示顶点存储的值
     * @param <E> 适用于边
     */
    class Vertex<V, E> {
        V value; // 顶点的值
        Set<Edge<V, E>> inEdges = new HashSet<>(); // 当前顶点的入度边
        Set<Edge<V, E>> outEdges = new HashSet<>(); // 当前顶点的出度边

        Vertex(V value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vertex)) return false;
            Vertex<?, ?> vertex = (Vertex<?, ?>) o;
            return Objects.equals(value, vertex.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    /**
     * 边Node
     *
     * @param <V> 适用于顶点
     * @param <E> 边权重
     */
    class Edge<V, E> {
        Vertex<V, E> from; // 表示该边出度的顶点
        Vertex<V, E> to; // 表示该边入度的顶点
        E wight; // 边的权重

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(from.value).append(" --(").append(wight).append(")--> ").append(to.value);
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;
            Edge<?, ?> edge = (Edge<?, ?>) o;
            return Objects.equals(from, edge.from) &&
                    Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

}
