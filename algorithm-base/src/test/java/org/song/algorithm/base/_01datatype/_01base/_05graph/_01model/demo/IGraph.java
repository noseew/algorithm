package org.song.algorithm.base._01datatype._01base._05graph._01model.demo;

public interface IGraph<V, E> {

    void addVertex(V v);

    void addEdge(V from, V to);
    
    void addEdge(V from, V to, E wight);

    void removeVertex(V v);

    void removeEdge(V from, V to);
    
    int edgeSize();

    int vertices();

}
