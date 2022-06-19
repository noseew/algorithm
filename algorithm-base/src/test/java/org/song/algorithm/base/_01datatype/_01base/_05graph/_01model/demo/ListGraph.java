package org.song.algorithm.base._01datatype._01base._05graph._01model.demo;

import java.util.*;
import java.util.stream.Collectors;

public class ListGraph<V, E> implements IGraph<V, E> {

    private Map<V, Vertex<V, E>> vertices = new HashMap<>();
    private Set<Edge<V, E>> edges = new HashSet<>();

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

    @Override
    public void addEdge(V from, V to, E wight) {
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

        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        edge.wight = wight;

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

    class Vertex<V, E> {
        V value;
        Set<Edge<V, E>> inEdges = new HashSet<>(); // 入度边
        Set<Edge<V, E>> outEdges = new HashSet<>(); // 出度边

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

    class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E wight;

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
