package org.song.algorithm.base._01datatype._01base._05graph._01model.listgraph;

import lombok.Data;
import org.song.algorithm.base._01datatype._01base._05graph._01model.IGraph;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class ListGraph<V, E> implements IGraph<V, E> {
    
    protected EdgeOpr<E> edgeOpr;
    
    protected Comparator<Edge<V, E>> comparator;
    
    protected ListGraph(EdgeOpr<E> edgeOpr) {
        this.edgeOpr = edgeOpr;
        this.comparator = (e1, e2) -> edgeOpr.compare(e1.wight, e2.wight);
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
    @Data
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
